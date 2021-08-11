package com.example.logindemo.controller;

import com.example.logindemo.dto.EmployeeRoleDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.UpdateRoleDto;
import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.EmployeeService;
import com.example.logindemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/29
 */
@Controller
@RestController
public class EmployeeRoleController {
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/employees/roles/{roleId}")
    public ReturnValue addEmployeeRole(@RequestHeader(EMPLOYEE_ID) Integer employeeId, @PathVariable(ROLE_ID) Integer roleId) {
        if (roleService.findByRoleId(roleId) != null) {
            EmployeeRoleDto employeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
            if (null == employeeRoleDto) {
                return ReturnValue.success(employeeRoleService.addEmployeeRole(employeeId, roleId));
            } else {
                return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, employeeRoleDto);
            }
        }
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);

    }

    @PutMapping("/employees/roles")
    public ReturnValue updatePermission(@RequestHeader(EMPLOYEE_ID) Integer employeeId, @RequestBody UpdateRoleDto updateRoleDto) {
        Integer roleIdBefore = updateRoleDto.getRoleIdBefore();
        Integer roleIdAfter = updateRoleDto.getRoleIdAfter();
        if (roleService.findByRoleId(roleIdBefore) != null && roleService.findByRoleId(roleIdAfter) != null) {
            EmployeeRoleDto employeeRoleDtoBefore = employeeRoleService.findEmployeeRole(employeeId, roleIdBefore);
            EmployeeRoleDto employeeRoleDtoAfter = employeeRoleService.findEmployeeRole(employeeId, roleIdAfter);
            if (null != employeeRoleDtoBefore) {
                if (null == employeeRoleDtoAfter) {
                    employeeRoleService.deleteEmployeeRole(employeeId, roleIdBefore);
                    return ReturnValue.success(employeeRoleService.addEmployeeRole(employeeId, roleIdAfter));
                } else {
                    return ReturnValue.fail(REPEAT_ASK_CODE, HAVE_ROLE, roleIdAfter);
                }
            } else {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleIdBefore);
            }
        }
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleIdBefore + " or " + roleIdAfter);

    }

    @DeleteMapping("/employees/roles/{roleId}")
    public ReturnValue deleteEmployeeRole(@RequestHeader(EMPLOYEE_ID) Integer employeeId, @PathVariable(ROLE_ID) Integer roleId) {
        if (roleService.findByRoleId(roleId) != null) {
            EmployeeRoleDto employeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
            if (null == employeeRoleDto) {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, null);
            } else {
                return ReturnValue.success(employeeRoleService.deleteEmployeeRole(employeeId, roleId));
            }
        }
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);


    }
}
