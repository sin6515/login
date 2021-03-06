package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.dto.RolePermissionRedisDto;
import com.example.logindemo.entity.RoleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.logindemo.dto.ConstantValue.ROLE;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PermissionService permissionService;

    public void addRole(String roleName) {
        RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
        roleDao.save(roleEntity);
    }

    public RoleEntity findByRoleName(String roleName) {
        return roleDao.findByRoleName(roleName);
    }

    public RoleIdNameDto findByRoleId(Integer roleId) {
        if (roleDao.findById(roleId).isPresent()) {
            return new RoleIdNameDto(roleId, roleDao.findById(roleId).get().getRoleName());
        }
        return null;
    }

    public RolePermissionRedisDto updateRoleRedis(Integer roleId) {
        List<Integer> permissionIdList = rolePermissionService.findPermissionIdByRoleId(roleId);
        List<String> permissionNameList = permissionService.findPermissionNameById(permissionIdList);
        String roleName = findByRoleId(roleId).getRoleName();
        RolePermissionRedisDto redisDto = new RolePermissionRedisDto(roleId, roleName, permissionNameList);
        String key = redisService.returnKey(roleId, ROLE);
        redisService.updateRedis(key, JSON.toJSONString(redisDto));
        employeeService.updateEmployeeRedis(employeeRoleService.findEmployeeIdByRoleId(roleId));
        return redisDto;

    }

    public Boolean existsByRoleName(String roleName) {
        return roleDao.existsByRoleName(roleName);
    }

    public Boolean existsByRoleId(Integer roleId) {
        return roleDao.existsById(roleId);
    }

    public Boolean existsByRoleId(List<Integer> roleId) {
        for (Integer integer : roleId) {
            if (!existsByRoleId(integer)) {
                return false;
            }
        }
        return true;
    }

    public void deleteRole(Integer roleId) {
        if (redisService.addDateBaseLock(roleId, ROLE)) {
            try {
                roleDao.deleteById(roleId);
                if (rolePermissionService.existsByRoleId(roleId)) {
                    rolePermissionService.deleteByRoleId(roleId);
                }
                if (redisService.existsRedis(roleId, ROLE)) {
                    redisService.deleteRedis(roleId, ROLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(roleId, ROLE);
            }
        }
        List<Integer> employeeId = employeeRoleService.findEmployeeIdByRoleId(roleId);
        if (!employeeId.isEmpty()) {
            employeeRoleService.deleteByRoleId(roleId);
            employeeService.updateEmployeeRedis(employeeId);
        }
    }

    public RoleIdNameDto updateRoleName(Integer roleId, String roleName) {
        if (redisService.addDateBaseLock(roleId, ROLE)) {
            try {
                roleDao.updateRoleName(roleName, System.currentTimeMillis(), roleId);
                updateRoleRedis(roleId);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisService.deleteDataLock(roleId, ROLE);
            }
        }
        log.info("??????" + roleId + "??????????????????" + roleName);
        return findByRoleId(roleId);
    }
}
