package com.example.logindemo.controller;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dto.*;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.service.PermissionService;
import com.example.logindemo.service.RedisService;
import com.example.logindemo.service.RolePermissionService;
import com.example.logindemo.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.ErrorConstantValue.*;
import static com.example.logindemo.dto.PermissionConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/27
 */
@Controller
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;

    @RequiresPermissions(ROLE_ADD)
    @PostMapping("/roles")
    public ReturnValue addRole(@RequestBody RoleNameDto roleNameDTO) {
        if (null == roleService.findByRoleName(roleNameDTO.getRoleName())) {
            roleService.addRole(roleNameDTO.getRoleName());
            return ReturnValue.success(redisService.updateRoleRedis(roleService.findByRoleName(roleNameDTO.getRoleName()).getId()));
        } else {
            return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, roleNameDTO);
        }
    }

    @RequiresPermissions(ROLE_DELETE)
    @DeleteMapping("/roles/{id}")
    public ReturnValue<RoleIdNameDto> deleteRole(@PathVariable("id") Integer roleId) throws NotFoundException {
        RoleIdNameDto foundRoleDto = roleService.findByRoleId(roleId);
        if (null == foundRoleDto) {
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        } else {
            redisService.deleteRedis(roleId, ROLE);
            return ReturnValue.success(roleService.deleteRole(roleId));
        }
    }

    @RequiresPermissions(ROLE_UPDATE)
    @PutMapping("/roles")
    public ReturnValue<String> updateRole(@RequestBody UpdateRoleDto updateRoleDto) throws NotFoundException {
        RoleIdNameDto foundRoleDtoBefore = roleService.findByRoleId(updateRoleDto.getRoleIdBefore());
        RoleIdNameDto foundRoleDtoAfter = roleService.findByRoleId(updateRoleDto.getRoleIdAfter());
        if (foundRoleDtoBefore != null && foundRoleDtoAfter != null) {
            roleService.updateRole(foundRoleDtoBefore, foundRoleDtoAfter);
            return ReturnValue.success(redisService.findRoleRedis(updateRoleDto.getRoleIdBefore(), updateRoleDto.getRoleIdAfter()));
        } else if (foundRoleDtoBefore == null) {
            throw new NotFoundException(ROLE_ID + " : " + updateRoleDto.getRoleIdBefore());
        } else {
            throw new NotFoundException(ROLE_ID + " : " + updateRoleDto.getRoleIdAfter());
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_FIND)
    @GetMapping("/roles/{roleId}")
    public ReturnValue<RolePermissionRedisDto> findRole(@PathVariable(ROLE_ID) Integer roleId) throws NotFoundException {
        if (null == roleService.findByRoleId(roleId)) {
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        } else {
            return ReturnValue.success(redisService.updateRoleRedis(roleId));
        }
    }


    @RequiresPermissions(ROLE_PERMISSION_ADD)
    @PostMapping("/roles/permissions")
    public ReturnValue addPermission(@RequestBody PermissionNameRoleIdDto permissionNameRoleIdDTO) throws NotFoundException {
        List<String> permissionName = permissionNameRoleIdDTO.getPermissionName().stream().distinct().collect(Collectors.toList());
        Integer roleId = permissionNameRoleIdDTO.getRoleId();
        if (permissionService.hasPermission(permissionName)) {
            if (roleService.findByRoleId(roleId) == null) {
                throw new NotFoundException(ROLE_ID + " : " + roleId);
            } else {
                if (rolePermissionService.findRolePermission(roleId, permissionName) == null) {
                    rolePermissionService.addRolePermission(roleId, permissionName);
                    return ReturnValue.success(redisService.updateRoleRedis(roleId));
                } else {
                    return ReturnValue.fail(REPEAT_ASK_CODE, REPEAT_ASK_STATE, permissionName);
                }
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionNameRoleIdDTO);
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_UPDATE)
    @PutMapping("/roles/permissions")
    public ReturnValue updatePermission(@RequestBody UpdatePermissionDto updatePermissionDTO) throws NotFoundException {
        List<String> permissionNameBefore = updatePermissionDTO.getPermissionNameBefore().stream().distinct().collect(Collectors.toList());
        List<String> permissionNameAfter = updatePermissionDTO.getPermissionNameAfter().stream().distinct().collect(Collectors.toList());
        Integer roleId = updatePermissionDTO.getRoleId();
        if (permissionService.hasPermission(permissionNameBefore)) {
            if (permissionService.hasPermission(permissionNameAfter)) {
                if (roleService.findByRoleId(roleId) == null) {
                    throw new NotFoundException(ROLE_ID + " : " + roleId);
                } else {
                    RolePermissionDto rolePermissionDtoBefore = rolePermissionService.findRolePermission(roleId, permissionNameBefore);
                    if (null == rolePermissionDtoBefore) {
                        throw new NotFoundException(ROLE_ID + ":" + roleId + " " + NO_HAVE + PERMISSION_NAME + " : " + permissionNameBefore);
                    } else {
                        RolePermissionDto rolePermissionDtoAfter = rolePermissionService.findRolePermission(roleId, permissionNameAfter);
                        if (null == rolePermissionDtoAfter) {
                            rolePermissionService.deleteByRoleIdAndPermissionName(roleId, permissionNameBefore);
                            rolePermissionService.addRolePermission(roleId, permissionNameAfter);
                            return ReturnValue.success(redisService.updateRoleRedis(roleId));
                        } else {
                            return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, permissionNameAfter);
                        }
                    }
                }
            } else {
                return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionNameAfter);
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionNameBefore);
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_DELETE)
    @DeleteMapping("/roles/permissions")
    public ReturnValue deletePermission(@RequestBody PermissionNameRoleIdDto permissionNameRoleIdDTO) throws NotFoundException {
        List<String> permissionName = permissionNameRoleIdDTO.getPermissionName().stream().distinct().collect(Collectors.toList());
        Integer roleId = permissionNameRoleIdDTO.getRoleId();
        if (permissionService.hasPermission(permissionName)) {
            if (roleService.findByRoleId(roleId) == null) {
                throw new NotFoundException(ROLE_ID + " : " + roleId);
            } else {
                RolePermissionDto rolePermissionDto = rolePermissionService.findRolePermission(roleId, permissionName);
                if (null == rolePermissionDto) {
                    throw new NotFoundException(JSON.toJSONString(permissionNameRoleIdDTO));
                } else {
                    rolePermissionService.deleteByRoleIdAndPermissionName(roleId, permissionName);
                    return ReturnValue.success(redisService.updateRoleRedis(roleId));
                }
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionNameRoleIdDTO);
        }
    }
}

