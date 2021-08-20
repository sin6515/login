package com.example.logindemo.controller;

import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.dto.RolePermissionDto;
import com.example.logindemo.dto.RolePermissionRedisDto;
import com.example.logindemo.error.IllegalInputException;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.error.RepeatAskException;
import com.example.logindemo.service.PermissionService;
import com.example.logindemo.service.RolePermissionService;
import com.example.logindemo.service.RoleService;
import com.example.logindemo.view.AddRoleRequest;
import com.example.logindemo.view.RolePermissionRequest;
import com.example.logindemo.view.UpdateRoleNameRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.logindemo.dto.ConstantValue.*;
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
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;

    @RequiresPermissions(ROLE_ADD)
    @PostMapping("/roles")
    public ReturnValue<RolePermissionRedisDto> addRole(@RequestBody @Valid AddRoleRequest request) throws RepeatAskException {
        if (roleService.existsByRoleName(request.getRoleName())) {
            throw new RepeatAskException(ROLE_NAME + " : " + request.getRoleName());
        } else {
            roleService.addRole(request.getRoleName());
            return ReturnValue.success(roleService.updateRoleRedis(roleService.findByRoleName(request.getRoleName()).getId()));
        }
    }

    @RequiresPermissions(ROLE_DELETE)
    @DeleteMapping("/roles/{id}")
    public ReturnValue<?> deleteRole(@PathVariable("id") Integer roleId) throws NotFoundException {
        if (roleService.existsByRoleId(roleId)) {
            roleService.deleteRole(roleId);
            return ReturnValue.success();
        } else {
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        }
    }

    @RequiresPermissions(ROLE_UPDATE)
    @PutMapping("/roles")
    public ReturnValue<RoleIdNameDto> updateRole(@RequestBody @Valid UpdateRoleNameRequest request) throws NotFoundException, RepeatAskException {
        if (roleService.existsByRoleId(request.getRoleId())) {
            if (roleService.findByRoleId(request.getRoleId()).getRoleName().equals(request.getRoleName())) {
                throw new RepeatAskException(ROLE_ID + " : " + request.getRoleId() + "名称已为" + request.getRoleName());
            } else if (roleService.existsByRoleName(request.getRoleName())) {
                throw new RepeatAskException(ROLE_NAME + " : " + request.getRoleName());
            } else {
                return ReturnValue.success(roleService.updateRoleName(request.getRoleId(), request.getRoleName()));
            }
        } else {
            throw new NotFoundException(ROLE_ID + " : " + request.getRoleId());
        }

    }

    @RequiresPermissions(ROLE_PERMISSION_FIND)
    @GetMapping("/roles/{roleId}")
    public ReturnValue<RolePermissionRedisDto> findRole(@PathVariable(ROLE_ID) Integer roleId) throws NotFoundException {
        if (roleService.existsByRoleId(roleId)) {
            return ReturnValue.success(roleService.updateRoleRedis(roleId));
        } else {
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_ADD)
    @PostMapping("/roles/permissions")
    public ReturnValue<RolePermissionRedisDto> addRolePermission(@RequestBody @Valid RolePermissionRequest request) throws NotFoundException, RepeatAskException, IllegalInputException {
        List<String> permissionName = request.getPermissionName().stream().distinct().collect(Collectors.toList());
        Integer roleId = request.getRoleId();
        if (permissionService.existsPermission(permissionName)) {
            if (roleService.existsByRoleId(roleId)) {
                if (rolePermissionService.findRolePermission(roleId, permissionName) == null) {
                    rolePermissionService.addRolePermission(roleId, permissionName);
                    return ReturnValue.success(roleService.updateRoleRedis(roleId));
                } else {
                    throw new RepeatAskException(PERMISSION_NAME + " : " + permissionName);
                }
            } else {
                throw new NotFoundException(ROLE_ID + " : " + roleId);
            }
        } else {
            throw new IllegalInputException(PERMISSION_NAME + " : " + permissionName);
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_UPDATE)
    @PutMapping("/roles/permissions")
    public ReturnValue<RolePermissionRedisDto> updateRolePermission(@RequestBody @Valid RolePermissionRequest request) throws NotFoundException, RepeatAskException, IllegalInputException {
        List<String> permissionName = request.getPermissionName().stream().distinct().collect(Collectors.toList());
        Integer roleId = request.getRoleId();
        if (permissionService.existsPermission(permissionName)) {
            if (roleService.existsByRoleId(roleId)) {
                if (rolePermissionService.findRolePermission(roleId, permissionName) == null) {
                    rolePermissionService.deleteByRoleId(roleId);
                    rolePermissionService.addRolePermission(roleId, permissionName);
                    return ReturnValue.success(roleService.updateRoleRedis(roleId));
                } else {
                    throw new RepeatAskException(PERMISSION_NAME + " : " + permissionName);
                }
            } else {
                throw new NotFoundException(ROLE_ID + " : " + roleId);
            }
        } else {
            throw new IllegalInputException(PERMISSION_NAME + " : " + permissionName);
        }
    }

    @RequiresPermissions(ROLE_PERMISSION_DELETE)
    @DeleteMapping("/roles/permissions")
    public ReturnValue<?> deleteRolePermission(@RequestBody @Valid RolePermissionRequest request) throws NotFoundException, IllegalInputException {
        List<String> permissionName = request.getPermissionName().stream().distinct().collect(Collectors.toList());
        Integer roleId = request.getRoleId();
        if (permissionService.existsPermission(permissionName)) {
            if (roleService.existsByRoleId(roleId)) {
                RolePermissionDto rolePermissionDto = rolePermissionService.findRolePermission(roleId, permissionName);
                if (rolePermissionDto == null) {
                    throw new NotFoundException(PERMISSION_NAME + " : " + permissionName);
                } else {
                    rolePermissionService.deleteByRoleIdAndPermissionName(roleId, permissionName);
                    return ReturnValue.success();
                }
            } else {
                throw new NotFoundException(ROLE_ID + " : " + roleId);
            }
        } else {
            throw new IllegalInputException(PERMISSION_NAME + " : " + permissionName);
        }
    }
}

