package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dto.EmployeeRoleDto;
import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Service
public class EmployeeRoleService {
    @Autowired
    private EmployeeRoleDao employeeRoleDao;

    public EmployeeRoleDto findEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleEntity employeeRoleEntity = employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId);
        if (employeeRoleEntity == null) {
            return null;
        } else {
            EmployeeRoleDto employeeRoleDto = new EmployeeRoleDto(employeeRoleEntity.getEmployeeId(),
                    employeeRoleEntity.getRoleId());
            return employeeRoleDto;

        }
    }

    public EmployeeRoleDto addEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleEntity employeeRoleEntity = new EmployeeRoleEntity(employeeId,
                roleId, System.currentTimeMillis());
        employeeRoleDao.save(employeeRoleEntity);
        return findEmployeeRole(employeeId, roleId);
    }

    public void addEmployeeRole(List<Integer> employeeIdList, Integer roleId) {
        for (Integer integer : employeeIdList) {
            addEmployeeRole(integer, roleId);
        }
    }

    public EmployeeRoleDto deleteEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleDto employeeRoleDto = findEmployeeRole(employeeId, roleId);
        employeeRoleDao.deleteByEmployeeIdAndRoleId(employeeId, roleId);
        return employeeRoleDto;
    }

    public void deleteEmployeeRole(List<Integer> employeeIdList, Integer roleId) {
        for (Integer i = 0; i < employeeIdList.size(); i++) {
            deleteEmployeeRole(employeeIdList.get(i), roleId);
        }
    }


}
