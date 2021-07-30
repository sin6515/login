package com.example.logindemo.controller;

import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.UpdateEmployeeRoleDto;
import com.example.logindemo.service.EmployeeRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author hrh13
 * @date 2021/7/29
 */
@Controller
@RestController
public class EmployeeRoleController {
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @PostMapping("/employees/{employeeId}/roles/{roleId}")
    public ReturnValue addEmployeeRole(@PathVariable("employeeId") Integer employeeId, @PathVariable("roleId") Integer roleId) {
        return employeeRoleService.addEmployeeRole(employeeId,roleId);
    }
    @PutMapping("/employees-roles")
    public ReturnValue updatePermission(@RequestBody UpdateEmployeeRoleDto updateEmployeeRoleDto) {
        return employeeRoleService.updateEmployeeRole(updateEmployeeRoleDto.getEmployeeId(),updateEmployeeRoleDto.getRoleId1(),
                updateEmployeeRoleDto.getRoleId2());
    }
    @DeleteMapping("/employees/{employeeId}/roles/{roleId}")
    public ReturnValue deleteEmployeeRole(@PathVariable("employeeId") Integer employeeId, @PathVariable("roleId") Integer roleId) {
        return employeeRoleService.deleteEmployeeRole(employeeId,roleId);
    }
}
