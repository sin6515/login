package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.entity.EmployeeEntity;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.error.PasswordErrorException;
import com.example.logindemo.error.RepeatAskException;
import com.example.logindemo.service.*;
import com.example.logindemo.view.EmployeeRoleRequest;
import com.example.logindemo.view.LoginRequest;
import com.example.logindemo.view.RegisterRequest;
import com.example.logindemo.view.UpdatePasswordRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.PermissionConstantValue.*;
import static com.example.logindemo.error.ErrorConstantValue.NO_HAVE;

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
    public ReturnValue<EmployeeDto> registerEmployee(@RequestBody @Valid RegisterRequest request) throws RepeatAskException {
        if (employeeService.existsByAccount(request.getAccount())) {
            throw new RepeatAskException(ACCOUNT + " : " + request.getAccount());
        } else {
            return ReturnValue.success(employeeService.addEmployee(request));
        }
    }

    @PostMapping(path = "/employees/login")
    public ReturnValue<LoginRequest> loginEmployee(@RequestBody @Valid LoginRequest request) throws NotFoundException, PasswordErrorException {
        EmployeeEntity employeeFound = employeeService.findByEmployeeAccount(request.getAccount());
        if (null == employeeFound) {
            throw new NotFoundException(ACCOUNT + " : " + request.getAccount());
        } else if (!employeeFound.getPassWord().equals(DigestUtils.md5DigestAsHex(request.getPassWord().getBytes()))) {
            throw new PasswordErrorException(PASSWORD + " : " + request.getPassWord());
        } else {
            return ReturnValue.success(new LoginTokenDto(employeeService.updateEmployeeRedis(employeeFound.getId())));
        }
    }

    @RequiresPermissions(EMPLOYEE_ROLE_ADD)
    @PostMapping("/employees//roles")
    public ReturnValue<RedisDto> addEmployeeRole(@RequestBody @Valid EmployeeRoleRequest request) throws NotFoundException, RepeatAskException {
        if (employeeService.existsByEmployeeId(request.getEmployeeId())) {
            List<Integer> roleId = request.getRoleId().stream().distinct().collect(Collectors.toList());
            if (roleService.existsByRoleId(roleId)) {
                UpdateDto updateDto = employeeRoleService.findRoleIdDeleteAndAdd(request.getEmployeeId(), roleId);
                if (updateDto.getIdAdd().isEmpty()) {
                    throw new RepeatAskException(ROLE_ID + " : " + roleId);
                }
                employeeRoleService.addEmployeeRole(request.getEmployeeId(), updateDto.getIdAdd());
                return ReturnValue.success(employeeService.updateEmployeeRedis(request.getEmployeeId()));
            }
            throw new NotFoundException(ROLE_ID + " : " + request.getRoleId());
        } else {
            throw new NotFoundException(EMPLOYEE_ID + " : " + request.getEmployeeId());
        }
    }

    @RequiresPermissions(EMPLOYEE_ROLE_UPDATE)
    @PutMapping("/employees/roles")
    public ReturnValue<RedisDto> updateEmployeeRole(@RequestBody @Valid EmployeeRoleRequest request) throws NotFoundException {
        if (employeeService.existsByEmployeeId(request.getEmployeeId())) {
            List<Integer> roleId = request.getRoleId().stream().distinct().collect(Collectors.toList());
            if (roleService.existsByRoleId(roleId)) {
                UpdateDto updateDto = employeeRoleService.findRoleIdDeleteAndAdd(request.getEmployeeId(), roleId);
                if (!updateDto.getIdAdd().isEmpty() || !updateDto.getIdDelete().isEmpty()) {
                    employeeRoleService.updateEmployeeRole(request.getEmployeeId(), updateDto.getIdDelete(), updateDto.getIdAdd());
                }
                return ReturnValue.success(employeeService.updateEmployeeRedis(request.getEmployeeId()));

            } else {
                throw new NotFoundException(ROLE_ID + " : " + request.getRoleId());
            }
        } else {
            throw new NotFoundException(EMPLOYEE_ID + " : " + request.getEmployeeId());
        }
    }

    @RequiresPermissions(EMPLOYEE_ROLE_DELETE)
    @DeleteMapping("/employees/{employeeId}/roles/{roleId}")
    public ReturnValue<?> deleteEmployeeRole(@PathVariable(EMPLOYEE_ID) Integer employeeId, @PathVariable(ROLE_ID) Integer roleId) throws NotFoundException {
        if (employeeService.existsByEmployeeId(employeeId)) {
            if (roleService.existsByRoleId(roleId)) {
                if (employeeRoleService.existByEmployeeIdAndRoleId(employeeId, roleId)) {
                    employeeRoleService.deleteEmployeeRole(employeeId, roleId);
                    return ReturnValue.success();
                } else {
                    throw new NotFoundException(EMPLOYEE_ID + ":" + employeeId + " " + NO_HAVE + ROLE_ID + ": " + roleId);
                }
            }
            throw new NotFoundException(ROLE_ID + " : " + roleId);
        } else {
            throw new NotFoundException(EMPLOYEE_ID + " : " + employeeId);
        }
    }

    @RequiresPermissions(USER_FIND)
    @GetMapping(path = "/employees/users/{userId}")
    public ReturnValue<RedisDto> findUser(@PathVariable(USER_ID) Integer userId) throws NotFoundException {
        if (redisService.existsRedis(userId, USER)) {
            return ReturnValue.success(userService.findByRedis(userId));
        } else if (userService.existsById(userId)) {
            UserDto userDto = userService.findById(userId);
            LoginRequest request = new LoginRequest();
            request.setAccount(userDto.getAccount());
            request.setPassWord(userDto.getPassWord());
            userService.updateUserRedis(request);
            return ReturnValue.success(userService.findByRedis(userId));
        } else {
            throw new NotFoundException(USER_ID + " : " + userId);
        }

    }

    @RequiresPermissions(USER_DELETE)
    @DeleteMapping("/employees/users/{userId}")
    public ReturnValue<?> deleteUser(@PathVariable(USER_ID) Integer userId) throws NotFoundException {
        if (userService.existsById(userId)) {
            userService.deleteUser(userId);
            return ReturnValue.success();
        } else {
            throw new NotFoundException(USER_ID + " : " + userId);
        }
    }

    @RequiresPermissions(USER_UPDATE)
    @PutMapping("/employees/users")
    public ReturnValue<UserDto> updateUser(@RequestBody @Valid UpdatePasswordRequest request) throws NotFoundException {
        if (userService.existsById(request.getId())) {
            return ReturnValue.success(userService.updatePassword(request.getId(), request.getPassWord()));
        } else {
            throw new NotFoundException(USER_ID + " : " + request.getId());
        }
    }
}