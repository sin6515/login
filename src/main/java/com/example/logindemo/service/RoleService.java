package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.dto.RoleNameDto;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private EmployeeRoleDao employeeRoleDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    public RoleEntity addRole(String roleName) {
        RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
        roleDao.save(roleEntity);
        return roleEntity;
    }

    public RoleNameDto findByRoleName(String roleName) {
        if (roleDao.findByRoleName(roleName) == null) {
            return null;
        } else {
            RoleNameDto roleNameDto = new RoleNameDto();
            roleNameDto.setRoleName(roleName);
            return roleNameDto;
        }
    }

    public RoleIdNameDto findByRoleId(Integer roleId) {
        if (!roleDao.existsById(roleId)) {
            return null;
        } else {
            RoleIdNameDto roleIdNameDto = new RoleIdNameDto();
            roleIdNameDto.setRoleId(roleDao.findById(roleId).get().getId());
            roleIdNameDto.setRoleName(roleDao.findById(roleId).get().getRoleName());
            return roleIdNameDto;
        }
    }

    public RoleIdNameDto deleteRole(Integer roleId) {
        RoleIdNameDto roleIdNameDto = findByRoleId(roleId);
        roleDao.deleteById(roleId);
        employeeRoleDao.deleteByRoleId(roleId);
        rolePermissionDao.deleteByRoleId(roleId);
        return roleIdNameDto;
    }
}
