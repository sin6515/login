package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.entity.EmployeeRoleEntity;
import com.example.logindemo.entity.RoleEntity;
import com.example.logindemo.entity.RolePermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private RolePermissionService rolePermissionService;

    public void addRole(String roleName) {
        RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
        roleDao.save(roleEntity);
    }

    public RoleIdNameDto findByRoleName(String roleName) {
        if (roleDao.findByRoleName(roleName) == null) {
            return null;
        } else {
            return new RoleIdNameDto(roleDao.findByRoleName(roleName).getId(), roleName);
        }
    }

    public RoleIdNameDto findByRoleId(Integer roleId) {
        if (roleDao.findById(roleId).isPresent()) {
            return new RoleIdNameDto(roleId, roleDao.findById(roleId).get().getRoleName());
        } else {
            return null;
        }
    }

    public List<RoleEntity> findByRoleId(List<Integer> roleId) {
        return roleDao.findByIdIn(roleId);
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
        List<Integer> employeeIdListBefore = employeeRoleDao.findByRoleId(roleIdBefore)
                .stream().map(EmployeeRoleEntity::getEmployeeId).collect(Collectors.toList());
        List<Integer> employeeIdListAfter = employeeRoleDao.findByRoleId(roleIdAfter)
                .stream().map(EmployeeRoleEntity::getEmployeeId).collect(Collectors.toList());
        List<Integer> permissionIdListBefore = rolePermissionDao.findByRoleId(roleIdBefore)
                .stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
        List<Integer> permissionIdListAfter = rolePermissionDao.findByRoleId(roleIdAfter)
                .stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
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
        rolePermissionService.addRolePermissionId(roleIdBefore, permissionIdListAfter);
        rolePermissionService.addRolePermissionId(roleIdAfter, permissionIdListBefore);
    }
}
