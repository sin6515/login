package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.entity.EmployeeEntity;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.error.PasswordErrorException;
import com.example.logindemo.error.RepeatAskException;
import com.example.logindemo.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.ErrorConstantValue.NO_HAVE;
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
    public ReturnValue<EmployeeDto> add(@RequestBody AddDto addDto) throws RepeatAskException {
        EmployeeEntity loginFound = employeeService.findByEmployeeAccount(addDto.getAccount());
        if (null == loginFound) {
            return ReturnValue.success(employeeService.addEmployee(addDto));
        } else {
            throw new RepeatAskException(ACCOUNT + " : " + addDto.getAccount());
        }
    }

    @PostMapping(path = "/employees/login")
    public ReturnValue<LoginDto> login(@RequestBody LoginDto loginDto) throws NotFoundException, PasswordErrorException {
        EmployeeEntity employeeFound = employeeService.findByEmployeeAccount(loginDto.getAccount());
        if (null == employeeFound) {
            throw new NotFoundException(ACCOUNT + " : " + loginDto.getAccount());
        } else if (!employeeFound.getPassWord().equals(DigestUtils.md5DigestAsHex(loginDto.getPassWord().getBytes()))) {
            throw new PasswordErrorException(PASSWORD + " : " + loginDto.getPassWord());
        } else {
            return ReturnValue.success(new LoginTokenDto(redisService.updateEmployeeRedis(employeeFound.getId())));
        }
    }

    @RequiresPermissions(EMPLOYEE_ROLE_ADD)
    @PostMapping("/employees/{employeeId}/roles/{roleId}")
    public ReturnValue<RedisDto> addEmployeeRole(@PathVariable(EMPLOYEE_ID) Integer employeeId, @PathVariable(ROLE_ID) Integer roleId) throws NotFoundException, RepeatAskException {
        if (employeeService.findByEmployeeId(employeeId) != null) {
            if (roleService.findByRoleId(roleId) != null) {
                EmployeeRoleDto employeeRoleDto = employeeRoleService.findByEmployeeIdAndRoleId(employeeId, roleId);
                if (null == employeeRoleDto) {
                    employeeRoleService.addEmployeeRole(employeeId, roleId);
                    return ReturnValue.success(redisService.updateEmployeeRedis(employeeId));
                } else {
                    throw new RepeatAskException(ROLE_ID + " : " + roleId);
                }
            }
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        } else {
            throw new NotFoundException(EMPLOYEE_ID + " : " + employeeId);
        }
    }

    @RequiresPermissions(EMPLOYEE_ROLE_UPDATE)
    @PutMapping("/employees/{employeeId}/roles")
    public ReturnValue<RedisDto> updateEmployeeRole(@PathVariable(EMPLOYEE_ID) Integer employeeId, @RequestBody UpdateRoleDto updateRoleDto) throws NotFoundException, RepeatAskException {
        Integer roleIdBefore = updateRoleDto.getRoleIdBefore();
        Integer roleIdAfter = updateRoleDto.getRoleIdAfter();
        if (employeeService.findByEmployeeId(employeeId) != null) {
            if (roleService.findByRoleId(roleIdBefore) != null) {
                EmployeeRoleDto employeeRoleDtoBefore = employeeRoleService.findByEmployeeIdAndRoleId(employeeId, roleIdBefore);
                EmployeeRoleDto employeeRoleDtoAfter = employeeRoleService.findByEmployeeIdAndRoleId(employeeId, roleIdAfter);
                if (roleService.findByRoleId(roleIdAfter) != null) {
                    if (null != employeeRoleDtoBefore) {
                        if (null == employeeRoleDtoAfter) {
                            employeeRoleService.deleteEmployeeRole(employeeId, roleIdBefore);
                            employeeRoleService.addEmployeeRole(employeeId, roleIdAfter);
                            return ReturnValue.success(redisService.updateEmployeeRedis(employeeId));
                        } else {
                            throw new RepeatAskException(ROLE_ID + " : " + roleIdAfter);
                        }
                    } else {
                        throw new NotFoundException(EMPLOYEE_ID + ":" + employeeId + " " + NO_HAVE + ROLE_ID + ": " + roleIdBefore);
                    }
                } else {
                    throw new NotFoundException(ROLE_ID + " : " + roleIdAfter);
                }
            } else {
                throw new NotFoundException(ROLE_ID + " : " + roleIdBefore);
            }

        } else {
            throw new NotFoundException(EMPLOYEE_ID + " : " + employeeId);
        }
    }

    @RequiresPermissions(EMPLOYEE_ROLE_DELETE)
    @DeleteMapping("/employees/{employeeId}/roles/{roleId}")
    public ReturnValue<RedisDto> deleteEmployeeRole(@PathVariable(EMPLOYEE_ID) Integer employeeId, @PathVariable(ROLE_ID) Integer roleId) throws NotFoundException {
        if (employeeService.findByEmployeeId(employeeId) != null) {
            if (roleService.findByRoleId(roleId) != null) {
                EmployeeRoleDto employeeRoleDto = employeeRoleService.findByEmployeeIdAndRoleId(employeeId, roleId);
                if (null == employeeRoleDto) {
                    throw new NotFoundException(EMPLOYEE_ID + ":" + employeeId + " " + NO_HAVE + ROLE_ID + ": " + roleId);
                } else {
                    employeeRoleService.deleteEmployeeRole(employeeId, roleId);
                    return ReturnValue.success(redisService.updateEmployeeRedis(employeeId));
                }
            }
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        } else {
            throw new NotFoundException(EMPLOYEE_ID + " : " + employeeId);
        }
    }

    @RequiresPermissions(USER_FIND)
    @GetMapping(path = "/employees/users/{userId}")
    public ReturnValue<RedisDto> find(@PathVariable(USER_ID) Integer userId) throws NotFoundException {
        UserDto userDto = userService.findById(userId);
        if (userDto != null) {
            if (!redisService.hasRedis(userId, USER)) {
                LoginDto loginDto = new LoginDto();
                loginDto.setAccount(userDto.getAccount());
                loginDto.setPassWord(userDto.getPassWord());
                redisService.updateUserRedis(loginDto);
            }
            return ReturnValue.success(redisService.findUserRedis(userId));
        } else {
            throw new NotFoundException(USER_ID + " : " + userId);
        }

    }

    @RequiresPermissions(USER_DELETE)
    @DeleteMapping("/employees/users/{userId}")
    public ReturnValue<UserDto> delete(@PathVariable(USER_ID) Integer userId) throws NotFoundException {
        UserDto userDto = userService.findById(userId);
        if (userDto != null) {
            if (redisService.hasRedis(userId, USER)) {
                redisService.deleteRedis(userId, USER);
            }
            return ReturnValue.success(userService.deleteUser(userId));
        } else {
            throw new NotFoundException(USER_ID + " : " + userId);
        }
    }

    @RequiresPermissions(USER_UPDATE)
    @PutMapping("/employees/users/{userId}")
    public ReturnValue<UserDto> update(@PathVariable(USER_ID) Integer userId, @RequestBody UpdatePassWordDto updatePassWordDTO) throws NotFoundException {
        UserDto userDto = userService.findById(userId);
        if (userDto != null) {
            if (redisService.hasRedis(userId, USER)) {
                LoginDto loginDto = new LoginDto();
                loginDto.setAccount(userDto.getAccount());
                loginDto.setPassWord(DigestUtils.md5DigestAsHex(updatePassWordDTO.getPassWord().getBytes()));
                redisService.updateUserRedis(loginDto);
            }
            return ReturnValue.success(userService.updateUser(userId, updatePassWordDTO.getPassWord()));
        } else {
            throw new NotFoundException(USER_ID + " : " + userId);
        }
    }

}