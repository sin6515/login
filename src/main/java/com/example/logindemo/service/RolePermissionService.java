package com.example.logindemo.service;

import com.example.logindemo.dao.PermissionDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.UpdateDto;
import com.example.logindemo.entity.PermissionEntity;
import com.example.logindemo.entity.RolePermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.logindemo.dto.ConstantValue.ROLE;

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
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;

    public UpdateDto findPermissionIdDeleteAndAdd(Integer roleId, List<Integer> permissionIdPut) {
        List<Integer> permissionIdDelete = findPermissionIdByRoleId(roleId);
        List<Integer> permissionIdAdd = new ArrayList<>(permissionIdPut);
        permissionIdDelete.removeAll(permissionIdPut);
        permissionIdAdd.removeAll(findPermissionIdByRoleId(roleId));
        return new UpdateDto(permissionIdDelete, permissionIdAdd);
    }

    public Boolean existsRolePermission(Integer roleId, List<Integer> permissionId) {
        return rolePermissionDao.existsByRoleIdAndPermissionIdIn(roleId, permissionId);
    }

    public List<String> findPermissionNameByRoleId(List<Integer> roleId) {
        List<Integer> permissionId = rolePermissionService.findPermissionIdByRoleId(roleId);
        return permissionDao.findByIdIn(permissionId).stream().map(PermissionEntity::getPermissionName).collect(Collectors.toList());
    }

    public void addRolePermission(Integer roleId, Integer permissionId) {
        RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, permissionId, System.currentTimeMillis());
        rolePermissionDao.save(rolePermissionEntity);
    }

    public void addRolePermission(Integer roleId, List<Integer> permissionId) {
        if (redisService.addDateBaseLock(roleId, ROLE)) {
            try {
                for (Integer integer : permissionId) {
                    addRolePermission(roleId, integer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(roleId, ROLE);
            }
        }
        if (redisService.existsRedis(roleId, ROLE)) {
            roleService.updateRoleRedis(roleId);
        }
    }

    public void deleteByRoleIdAndPermissionId(Integer roleId, List<Integer> permissionId) {
        if (redisService.addDateBaseLock(roleId, ROLE)) {
            try {
                rolePermissionDao.deleteByRoleIdAndPermissionIdIn(roleId, permissionId);
                if (redisService.existsRedis(roleId, ROLE)) {
                    roleService.updateRoleRedis(roleId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(roleId, ROLE);
            }
        }
    }

    public void deleteByRoleId(Integer roleId) {
        rolePermissionDao.deleteByRoleId(roleId);
    }


    public List<Integer> findPermissionIdByRoleId(List<Integer> roleId) {
        return rolePermissionDao.findByRoleIdIn(roleId).stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
    }

    public List<Integer> findPermissionIdByRoleId(Integer roleId) {
        return rolePermissionDao.findByRoleId(roleId).stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
    }

    public void updateRolePermission(Integer roleId, List<Integer> permissionIdDelete, List<Integer> permissionIdAdd) {
        if (redisService.addDateBaseLock(roleId, ROLE)) {
            try {
                if (!permissionIdDelete.isEmpty()) {
                    rolePermissionDao.deleteByRoleIdAndPermissionIdIn(roleId, permissionIdDelete);
                }
                if (!permissionIdAdd.isEmpty()) {
                    for (Integer integer : permissionIdAdd) {
                        addRolePermission(roleId, integer);
                    }
                }
                if (redisService.existsRedis(roleId, ROLE)) {
                    roleService.updateRoleRedis(roleId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(roleId, ROLE);
            }
        }
    }

    public Boolean existsByRoleId(Integer roleId) {
        return rolePermissionDao.existsByRoleId(roleId);
    }
}
