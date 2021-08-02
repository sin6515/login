package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.DeleteRoleDto;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.RoleDto;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.*;

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

    public RoleDto findByRoleName(String roleName) {
        if (roleDao.findByRoleName(roleName) == null) {
            return null;
        } else {
            RoleDto roleDto = new RoleDto();
            roleDto.setRoleName(roleName);
            return roleDto;
        }
    }

    public DeleteRoleDto findByRoleId(Integer roleId) {
        if (!roleDao.existsById(roleId)) {
            return null;
        } else {
            DeleteRoleDto deleteRoleDto = new DeleteRoleDto();
            deleteRoleDto.setRoleId(roleDao.findById(roleId).get().getId());
            deleteRoleDto.setRoleName(roleDao.findById(roleId).get().getRoleName());
            return deleteRoleDto;
        }
    }

    public DeleteRoleDto deleteRole(Integer roleId) {
        DeleteRoleDto deleteRoleDto = findByRoleId(roleId);
        roleDao.deleteById(roleId);
        employeeRoleDao.deleteByRoleId(roleId);
        rolePermissionDao.deleteByRoleId(roleId);
        return deleteRoleDto;
    }
}
