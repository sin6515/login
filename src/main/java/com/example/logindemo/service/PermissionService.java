package com.example.logindemo.service;

import com.example.logindemo.dao.PermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
