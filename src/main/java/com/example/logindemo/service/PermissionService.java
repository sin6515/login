package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.PermissionDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.RolePermissionDto;
import com.example.logindemo.entity.PermissionEntity;
import com.example.logindemo.entity.RolePermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.ROLE;

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
    @Autowired
    private RedisService redisService;

    public boolean findIsPermission(String permissionName, Integer employeeId) {
        for (int i = 0; i < employeeRoleDao.findRoleIdByEmployeeId(employeeId).size(); i++) {
            Integer roleId = employeeRoleDao.findRoleIdByEmployeeId(employeeId).get(i);
            if (redisService.hasRedis(roleId, ROLE)) {
                if (redisService.findPermissionRedis(roleId, ROLE) != null) {
                    if (redisService.findPermissionRedis(roleId, ROLE).contains(permissionName)) {
                        return true;
                    }
                }

            } else {
                for (int j = 0; j < rolePermissionDao.findPermissionIdByRoleId(roleId).size(); j++) {
                    Integer permissionId = rolePermissionDao.findPermissionIdByRoleId(roleId).get(j);
                    if (!permissionDao.findByPermissionNameAndId(permissionName, permissionId).isEmpty()) {
                        return true;
                    }
                }
            }

        }
        return false;
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

    public RolePermissionDto addPermission(Integer roleId, String permissionName) {
        Integer permissionId = permissionDao.findIdByPermissionName(permissionName);
        RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, permissionId, System.currentTimeMillis());
        rolePermissionDao.save(rolePermissionEntity);
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.setRoleId(rolePermissionEntity.getRoleId());
        rolePermissionDto.setPermissionId(rolePermissionEntity.getPermissionId());
        return rolePermissionDto;
    }


    public RolePermissionDto deletePermission(Integer roleId, String permissionName) {
        Integer permissionId = permissionDao.findIdByPermissionName(permissionName);
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.setRoleId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getRoleId());
        rolePermissionDto.setPermissionId(rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).getPermissionId());
        rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId);
        return rolePermissionDto;

    }


}
