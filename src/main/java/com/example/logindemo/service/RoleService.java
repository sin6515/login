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

    public void updateRole(RoleIdNameDto role1, RoleIdNameDto role2) {
        Integer roleId1 = role1.getRoleId();
        Integer roleId2 = role2.getRoleId();
        String roleName1 = role1.getRoleName();
        String roleName2 = role2.getRoleName();
        List<Integer> employeeIdList1 = employeeRoleDao.findEmployeeIdByRoleId(roleId1);
        List<Integer> employeeIdList2 = employeeRoleDao.findEmployeeIdByRoleId(roleId2);
        List<Integer> permissionIdList1 = rolePermissionDao.findPermissionIdByRoleId(roleId1);
        List<Integer> permissionIdList2 = rolePermissionDao.findPermissionIdByRoleId(roleId2);
        role1 = new RoleIdNameDto(roleId2, roleName2);
        role2 = new RoleIdNameDto(roleId1, roleName1);
        deleteRole(roleId1);
        deleteRole(roleId2);
        RoleEntity roleEntity2 = new RoleEntity(role2, System.currentTimeMillis());
        RoleEntity roleEntity1 = new RoleEntity(role1, System.currentTimeMillis());
        roleDao.save(roleEntity1);
        roleDao.save(roleEntity2);

        employeeRoleService.addEmployeeRole(employeeIdList1, roleId2);
        employeeRoleService.addEmployeeRole(employeeIdList2, roleId1);
//
//        permissionService.addPermission(roleId1, permissionIdList2);
//        permissionService.addPermission(roleId2, permissionIdList1);
    }
}
