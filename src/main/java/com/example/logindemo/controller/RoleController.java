package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.service.PermissionService;
import com.example.logindemo.service.RedisService;
import com.example.logindemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.logindemo.dto.ConstantValue.*;

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
    private PermissionService permissionService;

    @PostMapping("/roles")
    public ReturnValue addRole(@RequestBody RoleNameDto roleNameDTO) {
        RoleIdNameDto foundRoleNameDto = roleService.findByRoleName(roleNameDTO.getRoleName());
        if (null == foundRoleNameDto) {
            roleService.addRole(roleNameDTO.getRoleName());
            return ReturnValue.success(redisService.registerRoleRedis(roleNameDTO)
            );
        } else {
            return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, foundRoleNameDto);
        }
    }
    @GetMapping("/roles/{roleId}")
    public ReturnValue findRole(@PathVariable(ROLE_ID) Integer roleId) {
        if (null == roleService.findByRoleId(roleId)) {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
        } else {
            return ReturnValue.success(redisService.updatePermissionRedis(roleId));
        }
    }

    @PutMapping("/roles")
    public ReturnValue updateRole(@RequestBody UpdateRoleDto updateRoleDto) {
        RoleIdNameDto foundRoleDtoBefore = roleService.findByRoleId(updateRoleDto.getRoleIdBefore());
        RoleIdNameDto foundRoleDtoAfter = roleService.findByRoleId(updateRoleDto.getRoleIdAfter());
        if (foundRoleDtoBefore != null && foundRoleDtoAfter != null) {
            roleService.updateRole(foundRoleDtoBefore, foundRoleDtoAfter);
//            redisService.updatePermissionRedis(foundRoleDtoAfter.getRoleId());
            return ReturnValue.success(redisService.updatePermissionRedis(foundRoleDtoBefore.getRoleId()));

        } else {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, foundRoleDtoBefore.getRoleId() + " or" + foundRoleDtoAfter.getRoleId());
        }
    }

    @DeleteMapping("/roles/{id}")
    public ReturnValue deletePermission(@PathVariable("id") Integer roleId) {
        RoleIdNameDto foundRoleDto = roleService.findByRoleId(roleId);
        if (null == foundRoleDto) {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
        } else {
            redisService.deleteRedis(roleId, ROLE);
            return ReturnValue.success(roleService.deleteRole(roleId));
        }
    }

    @PostMapping("/roles/permissions")
    public ReturnValue addPermission(@RequestBody PermissionNameRoleIdDto permissionNameRoleIdDTO) {
        List<String> permissionName = permissionNameRoleIdDTO.getPermissionName().stream().distinct().collect(Collectors.toList());
        boolean permissionNameFlag = permissionName.stream().allMatch(s -> s.equals(ADD) || s.equals(UPDATE) ||
                s.equals(Find) || s.equals(DELETE));
        Integer roleId = permissionNameRoleIdDTO.getRoleId();
        if (permissionNameFlag) {
            if (roleService.findByRoleId(roleId) == null) {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
            } else {
                if (permissionService.findRolePermission(roleId, permissionName) == null) {
                    permissionService.addPermission(roleId, permissionName);
                    return ReturnValue.success(redisService.updatePermissionRedis(roleId));
                } else {
                    return ReturnValue.fail(REPEAT_ASK_CODE, REPEAT_ASK_STATE, permissionName);
                }
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionNameRoleIdDTO);
        }
    }

    @PutMapping("/roles/permissions")
    public ReturnValue updatePermission(@RequestBody UpdatePermissionDto updatePermissionDTO) {
        List<String> permissionNameBefore = updatePermissionDTO.getPermissionNameBefore().stream().distinct().collect(Collectors.toList());
        List<String> permissionNameAfter = updatePermissionDTO.getPermissionNameAfter().stream().distinct().collect(Collectors.toList());
        Integer roleId = updatePermissionDTO.getRoleId();
        boolean permissionNameFlag = permissionNameBefore.stream().allMatch(s -> s.equals(ADD) || s.equals(UPDATE) ||
                s.equals(Find) || s.equals(DELETE));
        if (permissionNameFlag) {
            permissionNameFlag = permissionNameAfter.stream().allMatch(s -> s.equals(ADD) || s.equals(UPDATE) ||
                    s.equals(Find) || s.equals(DELETE));
            if (permissionNameFlag) {
                if (roleService.findByRoleId(roleId) == null) {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
                } else {
                    RolePermissionDto rolePermissionDtoBefore = permissionService.findRolePermission(roleId, permissionNameBefore);
                    if (null == rolePermissionDtoBefore) {
                        return ReturnValue.fail(NOT_FOUND_CODE, NO_PERMISSION, permissionNameBefore);
                    } else {
                        RolePermissionDto rolePermissionDtoAfter = permissionService.findRolePermission(roleId, permissionNameAfter);
                        if (null == rolePermissionDtoAfter) {
                            permissionService.deletePermission(roleId, permissionNameBefore);
                            permissionService.addPermission(roleId, permissionNameAfter);
                            return ReturnValue.success(redisService.updatePermissionRedis(roleId));
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


    @DeleteMapping("/roles/permissions")
    public ReturnValue deletePermission(@RequestBody PermissionNameRoleIdDto permissionNameRoleIdDTO) {
        List<String> permissionName = permissionNameRoleIdDTO.getPermissionName().stream().distinct().collect(Collectors.toList());
        boolean permissionNameFlag = permissionName.stream().allMatch(s -> s.equals(ADD) || s.equals(UPDATE) ||
                s.equals(Find) || s.equals(DELETE));
        Integer roleId = permissionNameRoleIdDTO.getRoleId();
        if (permissionNameFlag) {
            if (roleService.findByRoleId(roleId) == null) {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
            } else {
                RolePermissionDto rolePermissionDto = permissionService.findRolePermission(roleId, permissionName);
                if (null == rolePermissionDto) {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, permissionNameRoleIdDTO);
                } else {
                    permissionService.deletePermission(roleId, permissionName);
                    return ReturnValue.success(redisService.updatePermissionRedis(roleId));
                }
            }
        } else {
            return ReturnValue.fail(ERROR_INPUT_CODE, ERROR_INPUT_STATE, permissionNameRoleIdDTO);
        }
    }
}

