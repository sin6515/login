package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.error.IllegalInputException;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.error.RepeatAskException;
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
import static com.example.logindemo.dto.ErrorConstantValue.NO_HAVE;
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
    public ReturnValue<RolePermissionRedisDto> addRole(@RequestBody RoleNameDto roleNameDTO) throws RepeatAskException {
        if (null == roleService.findByRoleName(roleNameDTO.getRoleName())) {
            roleService.addRole(roleNameDTO.getRoleName());
            return ReturnValue.success(redisService.updateRoleRedis(roleService.findByRoleName(roleNameDTO.getRoleName()).getId()));
        } else {
            throw new RepeatAskException(ROLE_NAME + " : " + roleNameDTO.getRoleName());
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
    public ReturnValue<RolePermissionRedisDto> addPermission(@RequestBody PermissionNameRoleIdDto permissionNameRoleIdDTO) throws NotFoundException, RepeatAskException, IllegalInputException {
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
                    throw new RepeatAskException(PERMISSION_NAME + " : " + permissionName);
                }
            }
        } else {
            throw new IllegalInputException(PERMISSION_NAME + " : " + permissionName);
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_UPDATE)
    @PutMapping("/roles/permissions")
    public ReturnValue<RolePermissionRedisDto> updatePermission(@RequestBody UpdatePermissionDto updatePermissionDTO) throws NotFoundException, RepeatAskException, IllegalInputException {
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
                            throw new RepeatAskException(PERMISSION_NAME + " : " + permissionNameAfter);
                        }
                    }
                }
            } else {
                throw new IllegalInputException(PERMISSION_NAME + " : " + permissionNameAfter.toString());
            }
        } else {
            throw new IllegalInputException(PERMISSION_NAME + " : " + permissionNameBefore.toString());
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_DELETE)
    @DeleteMapping("/roles/permissions")
    public ReturnValue<RolePermissionRedisDto> deletePermission(@RequestBody PermissionNameRoleIdDto permissionNameRoleIdDTO) throws NotFoundException, IllegalInputException {
        List<String> permissionName = permissionNameRoleIdDTO.getPermissionName().stream().distinct().collect(Collectors.toList());
        Integer roleId = permissionNameRoleIdDTO.getRoleId();
        if (permissionService.hasPermission(permissionName)) {
            if (roleService.findByRoleId(roleId) == null) {
                throw new NotFoundException(ROLE_ID + " : " + roleId);
            } else {
                RolePermissionDto rolePermissionDto = rolePermissionService.findRolePermission(roleId, permissionName);
                if (null == rolePermissionDto) {
                    throw new NotFoundException(PERMISSION_NAME + " : " + permissionName);
                } else {
                    rolePermissionService.deleteByRoleIdAndPermissionName(roleId, permissionName);
                    return ReturnValue.success(redisService.updateRoleRedis(roleId));
                }
            }
        } else {
            throw new IllegalInputException(PERMISSION_NAME + " : " + permissionName);
        }
    }
}

