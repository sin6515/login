package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.service.PermissionService;
import com.example.logindemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.ConstantValue.DELETE;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Controller
@RestController
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/permissions")
    public ReturnValue addPermission(@RequestBody PermissionDto permissionDTO) {
        String permissionName = permissionDTO.getPermissionName();
        Integer roleId = permissionDTO.getRoleId();
        if (permissionName.equals(ADD) || permissionName.equals(UPDATE) ||
                permissionName.equals(Find) || permissionName.equals(DELETE)) {
            if (roleService.findByRoleId(roleId) == null) {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
            } else {
                RolePermissionDto rolePermissionDto = permissionService.findRolePermission(roleId, permissionName);
                if (null == rolePermissionDto) {
                    return ReturnValue.success(permissionService.addPermission(roleId, permissionName));
                } else {
                    return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, rolePermissionDto);
                }
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionDTO);
        }
    }

    @PutMapping("/permissions")
    public ReturnValue updatePermission(@RequestBody UpdatePermissionDto updatePermissionDTO) {
        String permissionName1 = updatePermissionDTO.getPermissionName1();
        String permissionName2 = updatePermissionDTO.getPermissionName2();
        Integer roleId = updatePermissionDTO.getRoleId();
        boolean flag1 = permissionName1.equals(ADD) || permissionName1.equals(UPDATE) ||
                permissionName1.equals(Find) || permissionName1.equals(DELETE);
        boolean flag2 = permissionName2.equals(ADD) || permissionName2.equals(UPDATE) ||
                permissionName2.equals(Find) || permissionName2.equals(DELETE);
        if (flag1 && flag2) {
            if (roleService.findByRoleId(roleId) == null) {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
            } else {
                RolePermissionDto rolePermissionDto1 = permissionService.findRolePermission(roleId, permissionName1);
                if (null == rolePermissionDto1) {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_PERMISSION, updatePermissionDTO);
                } else {
                    RolePermissionDto rolePermissionDto2 = permissionService.findRolePermission(roleId, permissionName2);
                    if (null == rolePermissionDto2) {
                        permissionService.deletePermission(roleId, permissionName1);
                        return ReturnValue.success(permissionService.addPermission(roleId, permissionName2));
                    } else {
                        return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, updatePermissionDTO);
                    }

                }
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, updatePermissionDTO);
        }
    }

    @DeleteMapping("/permissions")
    public ReturnValue deletePermission(@RequestBody PermissionDto permissionDTO) {
        String permissionName = permissionDTO.getPermissionName();
        Integer roleId = permissionDTO.getRoleId();
        if (permissionName.equals(ADD) || permissionName.equals(UPDATE) ||
                permissionName.equals(Find) || permissionName.equals(DELETE)) {
            if (roleService.findByRoleId(roleId) == null) {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
            } else {
                RolePermissionDto rolePermissionDto = permissionService.findRolePermission(roleId, permissionName);
                if (null == rolePermissionDto) {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, permissionDTO);
                } else {
                    return ReturnValue.success(permissionService.deletePermission(roleId, permissionName));
                }
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionDTO);
        }
    }
}
