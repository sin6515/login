package com.example.logindemo.controller;

import com.example.logindemo.dto.EmployeeRoleDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.UpdateRoleDto;
import com.example.logindemo.interceptor.LoginHandlerInterceptor;
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
    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;
    private Integer employeeId;

    @PostMapping("/employees/roles/{roleId}")
    public ReturnValue addEmployeeRole(@PathVariable("roleId") Integer roleId) {
        employeeId = loginHandlerInterceptor.getEmployeeId();
        if (employeeService.hasEmployeeById(employeeId)) {
            if (roleService.findByRoleId(roleId) != null) {
                EmployeeRoleDto employeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
                if (null == employeeRoleDto) {
                    return ReturnValue.success(employeeRoleService.addEmployeeRole(employeeId, roleId));
                } else {
                    return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, employeeRoleDto);
                }
            }
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
        } else {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, employeeId);
        }

    }

    @PutMapping("/employees/roles")
    public ReturnValue updatePermission(@RequestBody UpdateRoleDto updateRoleDto) {
        employeeId = loginHandlerInterceptor.getEmployeeId();
        Integer roleId1 = updateRoleDto.getRoleId1();
        Integer roleId2 = updateRoleDto.getRoleId2();
        if (employeeService.hasEmployeeById(employeeId)) {
            if (roleService.findByRoleId(roleId1) != null && roleService.findByRoleId(roleId2) != null) {
                EmployeeRoleDto employeeRoleDto1 = employeeRoleService.findEmployeeRole(employeeId, roleId1);
                EmployeeRoleDto employeeRoleDto2 = employeeRoleService.findEmployeeRole(employeeId, roleId2);
                if (null != employeeRoleDto1) {
                    if (null == employeeRoleDto2) {
                        employeeRoleService.deleteEmployeeRole(employeeId, roleId1);
                        return ReturnValue.success(employeeRoleService.addEmployeeRole(employeeId, roleId2));
                    } else {
                        return ReturnValue.fail(REPEAT_ASK_CODE, HAVE_ROLE, roleId2);
                    }
                } else {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId1);
                }
            }
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId1 + " or " + roleId2);
        } else {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, employeeId);
        }

    }

    @DeleteMapping("/employees/roles/{roleId}")
    public ReturnValue deleteEmployeeRole(@PathVariable("roleId") Integer roleId) {
        employeeId = loginHandlerInterceptor.getEmployeeId();
        if (employeeService.hasEmployeeById(employeeId)) {
            if (roleService.findByRoleId(roleId) != null) {
                EmployeeRoleDto employeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
                if (null == employeeRoleDto) {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, null);
                } else {
                    return ReturnValue.success(employeeRoleService.deleteEmployeeRole(employeeId, roleId));
                }
            }
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
        } else {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, employeeId);
        }

    }
}
