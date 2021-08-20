package com.example.logindemo.service;

import com.example.logindemo.dao.PermissionDao;
import com.example.logindemo.entity.PermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hrh13
 * @date 2021/8/12
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    public boolean existsPermission(List<String> permissionName) {
        return permissionDao.existsByPermissionNameIn(permissionName);
    }

    public List<String> findPermissionNameById(List<Integer> permissionId) {
        return permissionDao.findByIdIn(permissionId).stream().map(PermissionEntity::getPermissionName).collect(Collectors.toList());
    }

    public List<Integer> findIdByPermissionName(List<String> permissionName) {
        return permissionDao.findByPermissionNameIn(permissionName).stream().map(PermissionEntity::getId).collect(Collectors.toList());
    }
}
