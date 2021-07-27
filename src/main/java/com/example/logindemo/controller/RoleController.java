package com.example.logindemo.controller;

import com.example.logindemo.dto.AddEmployeeRoleDto;
import com.example.logindemo.dto.RoleDto;
import com.example.logindemo.dto.UpdateRoleDto;
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
    public String addRole(@RequestBody RoleDto roleDTO) {
        return roleService.addRole(roleDTO.getRoleName());
    }
    @PostMapping("/roles/add")
    public String addEmployeeRole(@RequestBody AddEmployeeRoleDto addEmployeeRoleDto) {
        return employeeRoleService.addEmployeeRole(addEmployeeRoleDto.getEmployeeId(),addEmployeeRoleDto.getRoleId());
    }

    @PutMapping("/roles")
    public String updatePermission(@RequestBody UpdateRoleDto updateRoleDto) {
        return roleService.updateRole(updateRoleDto.getEmployeeId(),updateRoleDto.getRoleId1(),updateRoleDto.getRoleId2());
    }

    @DeleteMapping("/roles/{id}")
    public String deletePermission(@PathVariable("id") Integer roleId) {
        return roleService.deleteRole(roleId);
    }
}
