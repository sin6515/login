package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dto.EmployeeRoleDto;
import com.example.logindemo.entity.EmployeeRoleEntity;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Service
public class EmployeeRoleService {
    @Autowired
    private EmployeeRoleDao employeeRoleDao;
    @Autowired
    private RoleService roleService;

    public EmployeeRoleDto findEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleEntity employeeRoleEntity = employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId);
        if (employeeRoleEntity == null) {
            return null;
        } else {
            return new EmployeeRoleDto(employeeRoleEntity);
        }
    }

    public List<Integer> findRoleIdByEmployeeId(Integer employeeId) {
        return employeeRoleDao.findByEmployeeId(employeeId).stream().map(EmployeeRoleEntity::getRoleId).collect(Collectors.toList());
    }

    public List<String> findRoleNameByEmployeeId(Integer employeeId) {
        List<Integer> roleId = employeeRoleDao.findByEmployeeId(employeeId)
                .stream().map(EmployeeRoleEntity::getRoleId).collect(Collectors.toList());
        List<String> roleName = roleService.findByRoleId(roleId).stream().map(RoleEntity::getRoleName).collect(Collectors.toList());
        if (roleId.isEmpty()) {
            return null;
        } else {
            return roleName;
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
            if (findEmployeeRole(integer, roleId) == null) {
                addEmployeeRole(integer, roleId);
            }
        }
    }

    public EmployeeRoleDto deleteEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleDto employeeRoleDto = findEmployeeRole(employeeId, roleId);
        employeeRoleDao.deleteByEmployeeIdAndRoleId(employeeId, roleId);
        return employeeRoleDto;
    }
}
