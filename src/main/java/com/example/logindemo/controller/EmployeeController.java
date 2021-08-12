package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.PermissionConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Controller
@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/employees")
    public ReturnValue add(@RequestBody AddDto addDto) {
        LoginDto loginDtoFound = employeeService.findLoginDtoByEmployeeAccount(addDto.getAccount());
        if (null == loginDtoFound) {
            return ReturnValue.success(employeeService.addEmployee(addDto));
        } else {
            return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, loginDtoFound);
        }
    }

    @PostMapping(path = "/employees/login")
    public ReturnValue<LoginDto> login(@RequestBody LoginDto loginDto) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginDto.getAccount(), loginDto.getPassWord());
        LoginDto loginDtoFound = employeeService.findLoginDtoByEmployeeAccount(loginDto.getAccount());
        if (null == loginDtoFound) {
            return ReturnValue.fail(NOT_FOUND_CODE, LOGIN_ERROR_ACCOUNT, loginDto);
        } else if (!loginDtoFound.getPassWord().equals(DigestUtils.md5DigestAsHex(loginDto.getPassWord().getBytes()))) {
            return ReturnValue.fail(BAD_REQUEST_CODE, LOGIN_ERROR_PASSWORD, loginDto);
        } else {
            subject.login(token);
            redisService.addRedis(loginDtoFound, EMPLOYEE);
            return ReturnValue.success(loginDtoFound);
        }
    }

    @RequiresPermissions(EMPLOYEE_ROLE_ADD)
    @PostMapping("/employees/roles/{roleId}")
    public ReturnValue addEmployeeRole(@RequestHeader(EMPLOYEE_ID) Integer employeeId, @PathVariable(ROLE_ID) Integer roleId) {
        if (roleService.findByRoleId(roleId) != null) {
            EmployeeRoleDto employeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
            if (null == employeeRoleDto) {
                employeeRoleService.addEmployeeRole(employeeId, roleId);
                return ReturnValue.success(redisService.updateEmployeeRedis(employeeId));
            } else {
                return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, employeeRoleDto);
            }
        }
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);

    }

    @RequiresPermissions(EMPLOYEE_ROLE_UPDATE)
    @PutMapping("/employees/roles")
    public ReturnValue updateEmployeeRole(@RequestHeader(EMPLOYEE_ID) Integer employeeId, @RequestBody UpdateRoleDto updateRoleDto) {
        Integer roleIdBefore = updateRoleDto.getRoleIdBefore();
        Integer roleIdAfter = updateRoleDto.getRoleIdAfter();
        if (roleService.findByRoleId(roleIdBefore) != null && roleService.findByRoleId(roleIdAfter) != null) {
            EmployeeRoleDto employeeRoleDtoBefore = employeeRoleService.findEmployeeRole(employeeId, roleIdBefore);
            EmployeeRoleDto employeeRoleDtoAfter = employeeRoleService.findEmployeeRole(employeeId, roleIdAfter);
            if (null != employeeRoleDtoBefore) {
                if (null == employeeRoleDtoAfter) {
                    employeeRoleService.deleteEmployeeRole(employeeId, roleIdBefore);
                    employeeRoleService.addEmployeeRole(employeeId, roleIdAfter);
                    return ReturnValue.success(redisService.updateEmployeeRedis(employeeId));
                } else {
                    return ReturnValue.fail(REPEAT_ASK_CODE, HAVE_ROLE, roleIdAfter);
                }
            } else {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleIdBefore);
            }
        }
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleIdBefore + " or " + roleIdAfter);

    }

    @RequiresPermissions(EMPLOYEE_ROLE_DELETE)
    @DeleteMapping("/employees/roles/{roleId}")
    public ReturnValue deleteEmployeeRole(@RequestHeader(EMPLOYEE_ID) Integer employeeId, @PathVariable(ROLE_ID) Integer roleId) {
        if (roleService.findByRoleId(roleId) != null) {
            EmployeeRoleDto employeeRoleDto = employeeRoleService.findEmployeeRole(employeeId, roleId);
            if (null == employeeRoleDto) {
                return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, null);
            } else {
                employeeRoleService.deleteEmployeeRole(employeeId, roleId);
                return ReturnValue.success(redisService.updateEmployeeRedis(employeeId));

            }
        }
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, roleId);


    }

    @RequiresPermissions(USER_FIND)
    @GetMapping(path = "/employees/users/{userId}")
    public ReturnValue find(@PathVariable(USER_ID) Integer userId) {
        UserDto userDto = userService.findUser(userId);
        if (userDto != null) {
            if (redisService.hasRedis(userId, USER)) {
                return ReturnValue.success(redisService.findUserRedis(userId));
            } else {
                LoginDto loginDto = new LoginDto();
                loginDto.setAccount(userDto.getAccount());
                loginDto.setPassWord(userDto.getPassWord());
                redisService.addRedis(loginDto, USER);
                return ReturnValue.success(userDto
                );
            }
        } else {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, userId);
        }

    }

    @RequiresPermissions(USER_DELETE)
    @DeleteMapping("/employees/users/{userId}")
    public ReturnValue delete(@PathVariable(USER_ID) Integer userId) {
        UserDto userDto = userService.findUser(userId);
        if (userDto != null) {
            if (redisService.hasRedis(userId, USER)) {
                redisService.deleteRedis(userId, USER);
            }
            return ReturnValue.success(userService.deleteUser(userId));
        } else {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, userId);
        }
    }

    @RequiresPermissions(USER_UPDATE)
    @PutMapping("/employees/users/{userId}")
    public ReturnValue update(@PathVariable(USER_ID) Integer userId, @RequestBody UpdatePassWordDto updatePassWordDTO) {
        UserDto userDto = userService.findUser(userId);
        if (userDto != null) {
            if (redisService.hasRedis(userId, USER)) {
                LoginDto loginDto = new LoginDto();
                loginDto.setAccount(userDto.getAccount());
                loginDto.setPassWord(DigestUtils.md5DigestAsHex(updatePassWordDTO.getPassWord().getBytes()));
                redisService.addRedis(loginDto, USER);
            }
            return ReturnValue.success(userService.updateUser(userId, updatePassWordDTO.getPassWord()));
        } else {
            return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, userId);
        }
    }

}