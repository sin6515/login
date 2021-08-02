package com.example.logindemo.controller;

import com.example.logindemo.dto.RoleIdNameDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.RoleNameDto;
import com.example.logindemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/roles")
    public ReturnValue addRole(@RequestBody RoleNameDto roleNameDTO) {
        RoleNameDto foundRoleNameDto = roleService.findByRoleName(roleNameDTO.getRoleName());
        if (null == foundRoleNameDto) {
            return ReturnValue.success(roleService.addRole(roleNameDTO.getRoleName()));
        } else {
            return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, foundRoleNameDto);
        }
    }

    @DeleteMapping("/roles/{id}")
    public ReturnValue deletePermission(@PathVariable("id") Integer roleId) {
        RoleIdNameDto foundRoleDto = roleService.findByRoleId(roleId);
        if (null == foundRoleDto) {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
        } else {
            return ReturnValue.success(roleService.deleteRole(roleId));
        }
    }
}
