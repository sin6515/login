package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.PermissionDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.RolePermissionDto;
import com.example.logindemo.entity.PermissionEntity;
import com.example.logindemo.entity.RolePermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private EmployeeRoleDao employeeRoleDao;

    public boolean findIsPermission(String permissionName, Integer employeeId) {
        List<Integer> roleIdList = employeeRoleDao.findRoleIdByEmployeeId(employeeId);
        if (roleIdList == null) {
            return false;
        }
        List<Integer> permissionIdList = rolePermissionDao.findByRoleIdIn(roleIdList)
                .stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());

        List<String> permissionNameList = permissionDao.findByIdIn(permissionIdList)
                .stream().map(PermissionEntity::getPermissionName).collect(Collectors.toList());
        return permissionNameList.contains(permissionName);
//        if (redisService.hasRedis(roleId, ROLE)) {
//            if (redisService.findPermissionRedis(roleId, ROLE).contains(permissionName)) {
//                return true;
//            }
    }

    public void savePermission(String name) {
        PermissionEntity permissionEntity = new PermissionEntity(name, System.currentTimeMillis());
        permissionDao.save(permissionEntity);
    }

    public RolePermissionDto findRolePermission(Integer roleId, String permissionName) {
        if (permissionDao.findByPermissionName(permissionName).isEmpty()) {
            savePermission(permissionName);
        }
        Integer permissionId = permissionDao.findIdByPermissionName(permissionName);
        if (rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId) == null) {
            return null;
        } else {
            RolePermissionDto rolePermissionDto = new RolePermissionDto();
            rolePermissionDto.setRoleId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getRoleId());
            rolePermissionDto.setPermissionId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getPermissionId());
            return rolePermissionDto;
        }
    }

    public void addPermission(Integer roleId, String permissionName) {
        Integer permissionId = permissionDao.findIdByPermissionName(permissionName);
        RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, permissionId, System.currentTimeMillis());
        rolePermissionDao.save(rolePermissionEntity);
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.setRoleId(rolePermissionEntity.getRoleId());
        rolePermissionDto.setPermissionId(rolePermissionEntity.getPermissionId());
    }


    public void deletePermission(Integer roleId, String permissionName) {
        Integer permissionId = permissionDao.findIdByPermissionName(permissionName);
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.setRoleId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getRoleId());
        rolePermissionDto.setPermissionId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getPermissionId());
        rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId);

    }


}
