package com.example.logindemo.controller;

import com.example.logindemo.dto.FindEmployeeRoleDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.RoleDto;
import com.example.logindemo.dto.UpdateEmployeeRoleDto;
import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.EmployeeService;
import com.example.logindemo.service.RedisService;
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
    private RedisService redisService;

    @PostMapping("/employees/{employeeId}/roles/{roleId}")
    public ReturnValue addEmployeeRole(@PathVariable("employeeId") Integer employeeId, @PathVariable("roleId") Integer roleId) {
        if (redisService.hasKey(employeeId)) {
            if (employeeService.findEmployeeById(employeeId) != null) {
                if (roleService.findByRoleId(roleId) != null) {
                    FindEmployeeRoleDto findEmployeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
                    if (null == findEmployeeRoleDto) {
                        return ReturnValue.success(employeeRoleService.addEmployeeRole(employeeId, roleId));
                    } else {
                        return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, findEmployeeRoleDto);
                    }
                }
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
            } else {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, employeeId);
            }
        } else {
            return ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId);
        }
    }

    @PutMapping("/employees-roles")
    public ReturnValue updatePermission(@RequestBody UpdateEmployeeRoleDto updateEmployeeRoleDto) {
        Integer employeeId = updateEmployeeRoleDto.getEmployeeId();
        Integer roleId1 = updateEmployeeRoleDto.getRoleId1();
        Integer roleId2 = updateEmployeeRoleDto.getRoleId2();
        if (redisService.hasKey(employeeId)) {
            if (employeeService.findEmployeeById(employeeId) != null) {
                if (roleService.findByRoleId(roleId1) != null && roleService.findByRoleId(roleId2) != null) {
                    FindEmployeeRoleDto findEmployeeRoleDto1 = employeeRoleService.findEmployeeRole(employeeId, roleId1);
                    FindEmployeeRoleDto findEmployeeRoleDto2 = employeeRoleService.findEmployeeRole(employeeId, roleId2);
                    if (null != findEmployeeRoleDto1) {
                        if (null == findEmployeeRoleDto2) {
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
        } else {
            return ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId);
        }
    }

    @DeleteMapping("/employees/{employeeId}/roles/{roleId}")
    public ReturnValue deleteEmployeeRole(@PathVariable("employeeId") Integer employeeId, @PathVariable("roleId") Integer roleId) {
        if (redisService.hasKey(employeeId)) {
            if (employeeService.findEmployeeById(employeeId) != null) {
                if (roleService.findByRoleId(roleId) != null) {
                    FindEmployeeRoleDto findEmployeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
                    if (null == findEmployeeRoleDto) {
                        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, null);
                    } else {
                        return ReturnValue.success(employeeRoleService.deleteEmployeeRole(employeeId, roleId));
                    }
                }
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);
            } else {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, employeeId);
            }
        } else {
            return ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId);
        }
    }
}
