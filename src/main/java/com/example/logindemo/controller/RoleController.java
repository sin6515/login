package com.example.logindemo.controller;

import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.RoleDto;
import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private EmployeeRoleService employeeRoleService;
    @PostMapping("/roles")
    public ReturnValue addRole(@RequestBody RoleDto roleDTO) {
        return roleService.addRole(roleDTO.getRoleName());
    }

    @DeleteMapping("/roles/{id}")
    public ReturnValue deletePermission(@PathVariable("id") Integer roleId) {
        return roleService.deleteRole(roleId);
    }
}
