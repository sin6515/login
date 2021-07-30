package com.example.logindemo.service;

import com.example.logindemo.dao.*;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.entity.PermissionEntity;
import com.example.logindemo.entity.RolePermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Service
public class PermissionService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ReturnValueService returnValueService;

    public String findIsPermission(String permissionName, Integer employeeId) {

        if (redisService.hasKey(employeeId)) {
            for (int i = 0; i < roleService.findRole(employeeId).size(); i++) {
                Integer roleId = roleService.findRole(employeeId).get(i);
                for (int j = 0; j < rolePermissionDao.findPermissionIdByRoleId(roleId).size(); j++) {
                    Integer permissionId = rolePermissionDao.findPermissionIdByRoleId(roleId).get(j);
                    if (!permissionDao.findByPermissionNameAndId(permissionName, permissionId).isEmpty()) {
                        return SUCCEED;
                    }
                }

            }
            return FAILED;
        }
        return NO_LOGIN;
    }

    public void savePermission(String name) {
        PermissionEntity permissionEntity = new PermissionEntity(name, System.currentTimeMillis());
        permissionDao.save(permissionEntity);
    }

    public String addPermission(Integer roleId, String permissionName) {
        if (permissionName.equals(ADD) || permissionName.equals(UPDATE) ||
                permissionName.equals(Find) || permissionName.equals(DELETE)) {
            if (roleDao.existsById(roleId)) {
                if (permissionDao.findByPermissionName(permissionName).isEmpty()) {
                    savePermission(permissionName);
                }
                Integer permissionId = permissionDao.findIdByPermissionName(permissionName);
                if (rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).isEmpty()) {
                    RolePermissionEntity rolePermissionEntity = new RolePermissionEntity(roleId, permissionId, System.currentTimeMillis());
                    rolePermissionDao.save(rolePermissionEntity);
                    ReturnDetailValue returnDetailValue = new ReturnDetailValue(permissionName);
                    return returnValueService.succeedState(ADD_SUCCEED,     returnDetailValue);
                } else {
                    return returnValueService.failState(PERMISSION, ADD_FAILED, permissionName, REPEAT_ASK_CODE);
                }
            } else {
                return returnValueService.failState(PERMISSION, ADD_FAILED, roleId, NOT_FOUND_CODE);
            }
        } else {
            return returnValueService.failState(PERMISSION, ADD_FAILED, permissionName, ERROR_INPUT_CODE);
        }

    }


    public String deletePermission(Integer roleId, String permissionName) {
        if (permissionName.equals(ADD) || permissionName.equals(UPDATE) ||
                permissionName.equals(Find) || permissionName.equals(DELETE)) {
            if (roleDao.existsById(roleId)) {
                Integer permissionId = permissionDao.findIdByPermissionName(permissionName);
                if (!rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId).isEmpty()) {
                    rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId);
                    ReturnDetailValue returnDetailValue = new ReturnDetailValue(permissionName);
                    return returnValueService.succeedState(DELETE_SUCCEED,     returnDetailValue);
                } else {
                    return returnValueService.failState(PERMISSION, DELETE_FAILED, permissionName, FORBIDDEN_CODE);

                }
            } else {
                return returnValueService.failState(PERMISSION, DELETE_FAILED, roleId, NOT_FOUND_CODE);
            }
        } else {
            return returnValueService.failState(PERMISSION, DELETE_FAILED, permissionName, ERROR_INPUT_CODE);
        }

    }


    public String updatePermission(Integer roleId, String permissionName1, String permissionName2) {
        boolean flag1 = permissionName1.equals(ADD) || permissionName1.equals(UPDATE) ||
                permissionName1.equals(Find) || permissionName1.equals(DELETE);
        boolean flag2 = permissionName2.equals(ADD) || permissionName2.equals(UPDATE) ||
                permissionName2.equals(Find) || permissionName2.equals(DELETE);
        if (flag1 && flag2) {
            if (roleDao.existsById(roleId)) {
                Integer permissionId1 = permissionDao.findIdByPermissionName(permissionName1);
                if (!rolePermissionDao.findByRoleIdAndPermissionId(roleId, permissionId1).isEmpty()) {
                    if (permissionDao.findByPermissionName(permissionName2).isEmpty()) {
                        savePermission(permissionName2);
                    }
                    Integer permissionId2 = permissionDao.findIdByPermissionName(permissionName2);
                    rolePermissionDao.updatePermissionId2ByRoleIdAndPermissionId1(permissionId2, System.currentTimeMillis(),
                            roleId, permissionId1);
                    ReturnDetailValue returnDetailValue = new ReturnDetailValue(permissionName2);
                    return returnValueService.succeedState(UPDATE_SUCCEED,     returnDetailValue);
                } else {
                    return returnValueService.failState(PERMISSION, UPDATE_FAILED, permissionName1, NOT_FOUND_CODE);
                }
            } else {
                return returnValueService.failState(PERMISSION, UPDATE_FAILED, roleId, NOT_FOUND_CODE);
            }
        } else {
            return returnValueService.failState(PERMISSION, UPDATE_FAILED, permissionName1 + " and " + permissionName2, ERROR_INPUT_CODE);
        }

    }


}
