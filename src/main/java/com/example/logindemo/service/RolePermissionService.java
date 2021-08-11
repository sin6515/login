package com.example.logindemo.service;

import com.example.logindemo.dao.RolePermissionDao;
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
    public List<Integer> findPermissionIdByRoleId(List<Integer> roleId){
        return rolePermissionDao.findByRoleIdIn(roleId).stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
    }
}
