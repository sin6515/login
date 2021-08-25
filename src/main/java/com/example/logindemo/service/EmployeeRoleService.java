package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dto.UpdateDto;
import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.logindemo.dto.ConstantValue.EMPLOYEE;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Service
public class EmployeeRoleService {
    @Autowired
    private EmployeeRoleDao employeeRoleDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private EmployeeService employeeService;

    public Boolean existByEmployeeIdAndRoleId(Integer employeeId, Integer roleId) {
        return employeeRoleDao.existsByEmployeeIdAndRoleId(employeeId, roleId);
    }

    public UpdateDto findRoleIdDeleteAndAdd(Integer employeeId, List<Integer> roleIdPut) {
        List<Integer> roleIdDelete = findRoleIdByEmployeeId(employeeId);
        List<Integer> roleIdAdd = new ArrayList<>(roleIdPut);
        roleIdDelete.removeAll(roleIdPut);
        roleIdAdd.removeAll(findRoleIdByEmployeeId(employeeId));
        return new UpdateDto(roleIdDelete, roleIdAdd);
    }

    public List<Integer> findRoleIdByEmployeeId(Integer employeeId) {
        return employeeRoleDao.findByEmployeeId(employeeId).stream().map(EmployeeRoleEntity::getRoleId).collect(Collectors.toList());
    }

    public List<Integer> findEmployeeIdByRoleId(Integer roleId) {
        return employeeRoleDao.findByRoleId(roleId).stream().map(EmployeeRoleEntity::getEmployeeId).collect(Collectors.toList());
    }

    public void addEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleEntity employeeRoleEntity = new EmployeeRoleEntity(employeeId, roleId, System.currentTimeMillis());
        employeeRoleDao.save(employeeRoleEntity);
    }

    public void addEmployeeRole(Integer employeeId, List<Integer> roleId) {
        if (redisService.addDateBaseLock(employeeId, EMPLOYEE)) {
            try {
                for (Integer integer : roleId) {
                    addEmployeeRole(employeeId, integer);
                }
                if (redisService.existsRedis(employeeId, EMPLOYEE)) {
                    employeeService.updateEmployeeRedis(employeeId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(employeeId, EMPLOYEE);
            }
        }
    }

    public void deleteEmployeeRole(Integer employeeId, Integer roleId) {
        if (redisService.addDateBaseLock(employeeId, EMPLOYEE)) {
            try {
                employeeRoleDao.deleteByEmployeeIdAndRoleId(employeeId, roleId);
                if (redisService.existsRedis(employeeId, EMPLOYEE)) {
                    employeeService.updateEmployeeRedis(employeeId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(employeeId, EMPLOYEE);
            }
        }
    }

    public void deleteByRoleId(Integer roleId) {
        employeeRoleDao.deleteByRoleId(roleId);
    }

    public void updateEmployeeRole(Integer employeeId, List<Integer> roleIdDelete, List<Integer> roleIdAdd) {
        if (redisService.addDateBaseLock(employeeId, EMPLOYEE)) {
            try {
                if (!roleIdDelete.isEmpty()) {
                    employeeRoleDao.deleteByEmployeeIdAndRoleIdIn(employeeId, roleIdDelete);
                }
                if (!roleIdAdd.isEmpty()) {
                    for (Integer integer : roleIdAdd) {
                        addEmployeeRole(employeeId, integer);
                    }
                }
                if (redisService.existsRedis(employeeId, EMPLOYEE)) {
                    employeeService.updateEmployeeRedis(employeeId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(employeeId, EMPLOYEE);
            }
        }
    }
}
