package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.dto.RoleNameDto;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.logindemo.dto.ConstantValue.ROLE;

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
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RedisService redisService;

    public RoleEntity addRole(String roleName) {
        RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
        roleDao.save(roleEntity);
        return roleEntity;
    }

    public RoleIdNameDto findByRoleName(String roleName) {
        if (roleDao.findByRoleName(roleName) == null) {
            return null;
        } else {
            RoleIdNameDto roleIdNameDto = new RoleIdNameDto(roleDao.findByRoleName(roleName).getId(), roleName);
            return roleIdNameDto;
        }
    }

    public RoleIdNameDto findByRoleId(Integer roleId) {
        if (!roleDao.existsById(roleId)) {
            return null;
        } else {
            RoleIdNameDto roleIdNameDto = new RoleIdNameDto(roleId, roleDao.findById(roleId).get().getRoleName());
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

    public void updateRole(RoleIdNameDto roleBefore, RoleIdNameDto roleAfter) {
        Integer roleIdBefore = roleBefore.getRoleId();
        Integer roleIdAfter = roleAfter.getRoleId();
        List<Integer> employeeIdListBefore = employeeRoleDao.findEmployeeIdByRoleId(roleIdBefore);
        List<Integer> employeeIdListAfter = employeeRoleDao.findEmployeeIdByRoleId(roleIdAfter);
        List<Integer> permissionIdListBefore = rolePermissionDao.findPermissionIdByRoleId(roleIdBefore);
        List<Integer> permissionIdListAfter = rolePermissionDao.findPermissionIdByRoleId(roleIdAfter);
        deleteRole(roleIdBefore);
        deleteRole(roleIdAfter);
        String roleNameBefore = roleBefore.getRoleName();
        String roleNameAfter = roleAfter.getRoleName();
        roleBefore = new RoleIdNameDto(roleIdBefore, roleNameAfter);
        roleAfter = new RoleIdNameDto(roleIdAfter, roleNameBefore);
        RoleEntity roleEntityBefore = new RoleEntity(roleBefore, System.currentTimeMillis());
        RoleEntity roleEntityAfter = new RoleEntity(roleAfter, System.currentTimeMillis());
        roleDao.save(roleEntityAfter);
        roleDao.save(roleEntityBefore);
        employeeRoleService.addEmployeeRole(employeeIdListBefore, roleIdBefore);
        employeeRoleService.addEmployeeRole(employeeIdListAfter, roleIdAfter);
        permissionService.addPermissionId(roleIdBefore, permissionIdListAfter);
        permissionService.addPermissionId(roleIdAfter, permissionIdListBefore);
    }
}
