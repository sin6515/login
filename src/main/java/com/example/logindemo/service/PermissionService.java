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

    public boolean hasPermission(List<String> permissionName) {
        for (String s : permissionName) {
            if (permissionDao.findByPermissionName(s) == null) {
                return false;
            }
        }
        return true;
    }
    public List<String> findPermissionNameById(List<Integer> permissionId){
        return permissionDao.findByIdIn(permissionId).stream().map(PermissionEntity::getPermissionName).collect(Collectors.toList());
    }
}
