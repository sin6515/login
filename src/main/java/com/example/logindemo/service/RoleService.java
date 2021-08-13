package com.example.logindemo.service;

import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RedisService redisService;

    public void addRole(String roleName) {
        RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
        roleDao.save(roleEntity);
    }

    public RoleEntity findByRoleName(String roleName) {
        if (roleDao.findByRoleName(roleName) == null) {
            return null;
        } else {
            return roleDao.findByRoleName(roleName);
        }
    }

    public RoleIdNameDto findByRoleId(Integer roleId) {
        if (roleDao.findById(roleId).isPresent()) {
            return new RoleIdNameDto(roleId, roleDao.findById(roleId).get().getRoleName());
        } else {
            return null;
        }
    }

    public RoleIdNameDto deleteRole(Integer roleId) {
        RoleIdNameDto roleIdNameDto = findByRoleId(roleId);
        roleDao.deleteById(roleId);
        if (!rolePermissionService.findPermissionIdByRoleId(roleId).isEmpty()) {
            rolePermissionService.deleteByRoleId(roleId);
        }
        List<Integer> employeeId = employeeRoleService.findEmployeeIdByRoleId(roleId);
        if (!employeeId.isEmpty()) {
            employeeRoleService.deleteByRoleId(roleId);
            redisService.updateEmployeeRedis(employeeId);
        }
        return roleIdNameDto;
    }

    public void updateRole(RoleIdNameDto roleBefore, RoleIdNameDto roleAfter) {
        Integer roleIdBefore = roleBefore.getRoleId();
        Integer roleIdAfter = roleAfter.getRoleId();
        List<Integer> employeeIdListBefore = employeeRoleService.findEmployeeIdByRoleId(roleIdBefore);
        List<Integer> employeeIdListAfter = employeeRoleService.findEmployeeIdByRoleId(roleIdAfter);
        List<Integer> permissionIdListBefore = rolePermissionService.findPermissionIdByRoleId(roleIdBefore);
        List<Integer> permissionIdListAfter = rolePermissionService.findPermissionIdByRoleId(roleIdAfter);
        roleDao.deleteById(roleIdBefore);
        if (!rolePermissionService.findPermissionIdByRoleId(roleIdBefore).isEmpty()) {
            rolePermissionService.deleteByRoleId(roleIdBefore);
        }
        roleDao.deleteById(roleIdAfter);
        if (!rolePermissionService.findPermissionIdByRoleId(roleIdAfter).isEmpty()) {
            rolePermissionService.deleteByRoleId(roleIdAfter);
        }
        String roleNameBefore = roleBefore.getRoleName();
        String roleNameAfter = roleAfter.getRoleName();
        roleBefore = new RoleIdNameDto(roleIdBefore, roleNameAfter);
        roleAfter = new RoleIdNameDto(roleIdAfter, roleNameBefore);
        RoleEntity roleEntityBefore = new RoleEntity(roleBefore, System.currentTimeMillis());
        RoleEntity roleEntityAfter = new RoleEntity(roleAfter, System.currentTimeMillis());
        roleDao.save(roleEntityAfter);
        roleDao.save(roleEntityBefore);
        rolePermissionService.addRolePermissionId(roleIdBefore, permissionIdListAfter);
        rolePermissionService.addRolePermissionId(roleIdAfter, permissionIdListBefore);
        if (!employeeIdListBefore.isEmpty()) {
            redisService.updateEmployeeRedis(employeeIdListBefore);
        }
        if (!employeeIdListAfter.isEmpty()) {
            redisService.updateEmployeeRedis(employeeIdListAfter);
        }
        redisService.updateRoleRedis(roleIdBefore);
        redisService.updateRoleRedis(roleIdAfter);
    }
}
