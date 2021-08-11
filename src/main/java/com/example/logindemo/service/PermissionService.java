package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.PermissionDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.RolePermissionDto;
import com.example.logindemo.entity.EmployeeRoleEntity;
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
    @Autowired(required = false)
    private RolePermissionService rolePermissionService;

    public boolean findIsPermission(String permissionName, Integer employeeId) {
        List<Integer> roleIdList = employeeRoleDao.findByEmployeeId(employeeId)
                .stream().map(EmployeeRoleEntity::getRoleId).collect(Collectors.toList());
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
        if (permissionDao.findByPermissionName(permissionName) == null) {
            savePermission(permissionName);
        }
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

    public void addPermission(Integer roleId, String permissionName) {
        if (permissionDao.findByPermissionName(permissionName) == null) {
            savePermission(permissionName);
        }
        Integer permissionId = permissionDao.findByPermissionName(permissionName).getId();
        RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, permissionId, System.currentTimeMillis());
        rolePermissionDao.save(rolePermissionEntity);
    }

    public void addPermissionId(Integer roleId, List<Integer> permissionIdList) {
        for (Integer integer : permissionIdList) {
            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, integer, System.currentTimeMillis());
            rolePermissionDao.save(rolePermissionEntity);
        }
    }

    public void addPermission(Integer roleId, List<String> permissionNameList) {
        for (String s : permissionNameList) {
            if (findRolePermission(roleId, s) == null) {
                addPermission(roleId, s);
            }
        }
    }


    public void deletePermission(Integer roleId, String permissionName) {
        if (permissionDao.findByPermissionName(permissionName) == null) {
            savePermission(permissionName);
        }
        Integer permissionId = permissionDao.findByPermissionName(permissionName).getId();
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.setRoleId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getRoleId());
        rolePermissionDto.setPermissionId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getPermissionId());
        rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId);

    }

    public void deletePermission(Integer roleId, List<String> permissionNameList) {
        for (String s : permissionNameList) {
            if (findRolePermission(roleId, s) != null) {
                deletePermission(roleId, s);
            }
        }
    }


}
