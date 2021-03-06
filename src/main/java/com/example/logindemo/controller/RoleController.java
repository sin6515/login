package com.example.logindemo.controller;

import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.dto.RolePermissionRedisDto;
import com.example.logindemo.dto.UpdateDto;
import com.example.logindemo.error.IllegalInputException;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.error.RepeatAskException;
import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.PermissionService;
import com.example.logindemo.service.RolePermissionService;
import com.example.logindemo.service.RoleService;
import com.example.logindemo.view.AddRoleRequest;
import com.example.logindemo.view.RolePermissionRequest;
import com.example.logindemo.view.UpdateRoleNameRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private EmployeeRoleService employeeRoleService;

    @RabbitListener(queues = "registerEmployeeQueue")
    public void addEmployeeInitialRole(Integer employeeId) {
        employeeRoleService.addEmployeeRole(employeeId, 1);
    }

    @RequiresPermissions(ROLE_ADD)
    @PostMapping("/roles")
    public ReturnValue<RolePermissionRedisDto> addRole(@RequestBody @Valid AddRoleRequest request) throws RepeatAskException {
        if (roleService.existsByRoleName(request.getRoleName())) {
            throw new RepeatAskException(ROLE_NAME + " : " + request.getRoleName());
        } else {
            roleService.addRole(request.getRoleName());
            log.info("??????" + request.getRoleName() + "????????????");
            Integer roleId = roleService.findByRoleName(request.getRoleName()).getId();
            return ReturnValue.success(roleService.updateRoleRedis(roleId));
        }
    }

    @RequiresPermissions(ROLE_DELETE)
    @DeleteMapping("/roles/{id}")
    public ReturnValue<?> deleteRole(@PathVariable("id") Integer roleId) throws NotFoundException, UnauthorizedException {
        if (roleService.existsByRoleId(roleId)) {
            if (roleId.equals(1)) {
                throw new UnauthorizedException();
            }
            roleService.deleteRole(roleId);
            log.info("???????????????" + roleId);
            return ReturnValue.success();
        } else {
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        }
    }

    @RequiresPermissions(ROLE_UPDATE)
    @PutMapping("/roles")
    public ReturnValue<RoleIdNameDto> updateRole(@RequestBody @Valid UpdateRoleNameRequest request) throws NotFoundException, RepeatAskException {
        if (roleService.existsByRoleId(request.getRoleId())) {
            if (request.getRoleId().equals(1)) {
                throw new UnauthorizedException();
            }
            if (roleService.findByRoleId(request.getRoleId()).getRoleName().equals(request.getRoleName())) {
                throw new RepeatAskException(ROLE_ID + " : " + request.getRoleId() + "????????????" + request.getRoleName());
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
            log.info("??????????????????" + roleId);
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
        if (roleId.equals(1)) {
            throw new UnauthorizedException();
        }
        if (permissionService.existsPermission(permissionName)) {
            if (roleService.existsByRoleId(roleId)) {
                List<Integer> permissionId = permissionService.findIdByPermissionName(permissionName);
                UpdateDto updateDto = rolePermissionService.findPermissionIdDeleteAndAdd(roleId, permissionId);
                if (updateDto.getIdAdd().isEmpty()) {
                    throw new RepeatAskException(PERMISSION_NAME + " : " + permissionName);
                } else {
                    rolePermissionService.addRolePermission(roleId, updateDto.getIdAdd());
                    log.info("????????????" + roleId + "?????????" + request.getPermissionName() + "??????");
                    return ReturnValue.success(roleService.updateRoleRedis(roleId));
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
    public ReturnValue<RolePermissionRedisDto> updateRolePermission(@RequestBody @Valid RolePermissionRequest request) throws NotFoundException, IllegalInputException {
        List<String> permissionName = request.getPermissionName().stream().distinct().collect(Collectors.toList());
        Integer roleId = request.getRoleId();
        if (roleId.equals(1)) {
            throw new UnauthorizedException();
        }
        if (permissionService.existsPermission(permissionName)) {
            if (roleService.existsByRoleId(roleId)) {
                List<Integer> permissionId = permissionService.findIdByPermissionName(permissionName);
                UpdateDto updateDto = rolePermissionService.findPermissionIdDeleteAndAdd(roleId, permissionId);
                if (!updateDto.getIdAdd().isEmpty() || !updateDto.getIdDelete().isEmpty()) {
                    rolePermissionService.updateRolePermission(roleId, updateDto.getIdDelete(), updateDto.getIdAdd());
                }
                log.info("????????????" + roleId + "?????????" + request.getPermissionName() + "??????");
                return ReturnValue.success(roleService.updateRoleRedis(roleId));
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
        if (roleId.equals(1)) {
            throw new UnauthorizedException();
        }
        if (permissionService.existsPermission(permissionName)) {
            if (roleService.existsByRoleId(roleId)) {
                List<Integer> permissionId = permissionService.findIdByPermissionName(permissionName);
                if (rolePermissionService.existsRolePermission(roleId, permissionId)) {
                    rolePermissionService.deleteByRoleIdAndPermissionId(roleId, permissionId);
                    log.info("????????????" + roleId + "?????????" + request.getPermissionName() + "??????");
                    return ReturnValue.success();
                } else {
                    throw new NotFoundException(PERMISSION_NAME + " : " + permissionName);
                }
            } else {
                throw new NotFoundException(ROLE_ID + " : " + roleId);
            }
        } else {
            throw new IllegalInputException(PERMISSION_NAME + " : " + permissionName);
        }
    }
}

