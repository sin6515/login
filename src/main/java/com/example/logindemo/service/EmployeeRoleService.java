package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    /**
     * description:判断用户是否已拥有该角色list的一个角色，存在为true
     * @return {@link Boolean}
     * @author hrh
     * @date 2021/8/20
     */
    public Boolean existByEmployeeIdAndRoleId(Integer employeeId, List<Integer> roleId) {
        for (Integer integer : roleId) {
            if (existByEmployeeIdAndRoleId(employeeId, integer)) {
               return true;
            }
        }
        return false;
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
            for (Integer integer : roleId) {
                addEmployeeRole(employeeId, integer);
            }
            if (redisService.existsRedis(employeeId, EMPLOYEE)) {
                employeeService.updateEmployeeRedis(employeeId);
            }
            redisService.deleteDataLock(employeeId, EMPLOYEE);
        }
    }

    public void deleteEmployeeRole(Integer employeeId, Integer roleId) {
        if (redisService.addDateBaseLock(employeeId, EMPLOYEE)) {
            employeeRoleDao.deleteByEmployeeIdAndRoleId(employeeId, roleId);
            if (redisService.existsRedis(employeeId, EMPLOYEE)) {
                employeeService.updateEmployeeRedis(employeeId);
            }
            redisService.deleteDataLock(employeeId, EMPLOYEE);
        }
    }

    public void deleteByRoleId(Integer roleId) {
        employeeRoleDao.deleteByRoleId(roleId);
    }

    public void deleteByEmployeeId(Integer employeeId) {
        if (redisService.addDateBaseLock(employeeId, EMPLOYEE)) {
            employeeRoleDao.deleteByEmployeeId(employeeId);
            if (redisService.existsRedis(employeeId, EMPLOYEE)) {
                employeeService.updateEmployeeRedis(employeeId);
            }
            redisService.deleteDataLock(employeeId, EMPLOYEE);
        }
    }
}
