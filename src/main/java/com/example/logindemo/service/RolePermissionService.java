package com.example.logindemo.service;

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
 * @date 2021/8/10
 */
@Service
public class RolePermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionService rolePermissionService;

    public RolePermissionDto findRolePermission(Integer roleId, String permissionName) {
        Integer permissionId = permissionDao.findByPermissionName(permissionName).getId();
        if (rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId) == null) {
            return null;
        } else {
            RolePermissionDto rolePermissionDto = new RolePermissionDto();
            rolePermissionDto.setRoleId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getRoleId());
            rolePermissionDto.setPermissionId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getPermissionId());
            return rolePermissionDto;
        }
    }

    public RolePermissionDto findRolePermission(Integer roleId, List<String> permissionNameList) {
        for (String s : permissionNameList) {
            if (findRolePermission(roleId, s) == null) {
                return null;
            }
        }
        return new RolePermissionDto();
    }

    public List<String> findPermissionNameByRoleId(List<Integer> roleId) {
        List<Integer> permissionId = rolePermissionService.findPermissionIdByRoleId(roleId);
        return permissionDao.findByIdIn(permissionId).stream().map(PermissionEntity::getPermissionName).collect(Collectors.toList());
    }

    public void addRolePermission(Integer roleId, String permissionName) {
        Integer permissionId = permissionDao.findByPermissionName(permissionName).getId();
        RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, permissionId, System.currentTimeMillis());
        rolePermissionDao.save(rolePermissionEntity);
    }

    public void addRolePermissionId(Integer roleId, List<Integer> permissionIdList) {
        for (Integer integer : permissionIdList) {
            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, integer, System.currentTimeMillis());
            rolePermissionDao.save(rolePermissionEntity);
        }
    }

    public void addRolePermission(Integer roleId, List<String> permissionNameList) {
        for (String s : permissionNameList) {
            if (findRolePermission(roleId, s) == null) {
                addRolePermission(roleId, s);
            }
        }
    }


    public void deleteRolePermission(Integer roleId, String permissionName) {
        Integer permissionId = permissionDao.findByPermissionName(permissionName).getId();
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.setRoleId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getRoleId());
        rolePermissionDto.setPermissionId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getPermissionId());
        rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId);

    }

    public void deleteRolePermission(Integer roleId, List<String> permissionNameList) {
        for (String s : permissionNameList) {
            if (findRolePermission(roleId, s) != null) {
                deleteRolePermission(roleId, s);
            }
        }
    }


    public List<Integer> findPermissionIdByRoleId(List<Integer> roleId) {
        return rolePermissionDao.findByRoleIdIn(roleId).stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
    }
}
