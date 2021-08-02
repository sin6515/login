package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dto.FindEmployeeRoleDto;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Service
public class EmployeeRoleService {
    @Autowired
    private EmployeeRoleDao employeeRoleDao;

    public FindEmployeeRoleDto findEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleEntity employeeRoleEntity = employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId);
        if (employeeRoleEntity == null) {
            return null;
        } else {
            FindEmployeeRoleDto findEmployeeRoleDto = new FindEmployeeRoleDto( employeeRoleEntity.getEmployeeId(),
                    employeeRoleEntity.getRoleId());
            return findEmployeeRoleDto;

        }

    }

    public FindEmployeeRoleDto addEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleEntity employeeRoleEntity = new EmployeeRoleEntity(employeeId,
                roleId, System.currentTimeMillis());
        employeeRoleDao.save(employeeRoleEntity);
        return findEmployeeRole(employeeId, roleId);


    }

    public FindEmployeeRoleDto deleteEmployeeRole(Integer employeeId, Integer roleId) {
        FindEmployeeRoleDto findEmployeeRoleDto = findEmployeeRole(employeeId, roleId);
        employeeRoleDao.deleteByEmployeeIdAndRoleId(employeeId, roleId);
        return findEmployeeRoleDto;


    }


}
