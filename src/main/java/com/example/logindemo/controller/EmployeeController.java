package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.entity.UserEntity;
import com.example.logindemo.service.EmployeeService;

import com.example.logindemo.service.PermissionService;
import com.example.logindemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.ConstantValue.BAD_REQUEST_CODE;

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
    private PermissionService permissionService;

    @PostMapping("/employees")
    public ReturnValue add(@RequestBody AddDto addDto) {
        LoginDto loginDtoFound = employeeService.findEmployee(addDto.getAccount());
        if (null == loginDtoFound) {
            return ReturnValue.success(employeeService.addEmployee(addDto));
        } else {
            return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, loginDtoFound);
        }
    }

    @PostMapping(path = "/employees/login")
    public ReturnValue<LoginDto> login(@RequestBody LoginDto loginDto) {
        LoginDto loginDtoFound = employeeService.findEmployee(loginDto.getAccount());
        if (null == loginDtoFound) {
            return ReturnValue.fail(NOT_FOUND_CODE, LOGIN_ERROR_ACCOUNT, loginDto);
        } else if (!loginDtoFound.getPassWord().equals(DigestUtils.md5DigestAsHex(loginDto.getPassWord().getBytes()))) {
            return ReturnValue.fail(BAD_REQUEST_CODE, LOGIN_ERROR_PASSWORD, loginDto);
        } else {
            redisService.addRedis(loginDtoFound, EMPLOYEE);
            return ReturnValue.success(loginDtoFound);
        }
    }

    @GetMapping(path = "/employees/{employeeId}/users/{userId}")
    public ReturnValue find(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId) {
//        if (redisService.hasRedis(employeeId, EMPLOYEE)) {
            if (permissionService.findIsPermission(Find, employeeId)) {
                UserEntity userEntity = employeeService.findUser(userId);
                if (userEntity != null) {
                    if (redisService.hasRedis(userId, USER)) {
                        return ReturnValue.success(redisService.findRedis(userId, USER));
                    } else {
                        LoginDto loginDto = new LoginDto();
                        loginDto.setAccount(userEntity.getAccount());
                        loginDto.setPassWord(userEntity.getPassWord());
                        redisService.addRedis(loginDto, USER);
                        return ReturnValue.success(userEntity);
                    }

                } else {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, userId);
                }
            } else {
                return ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, employeeId + ":" + Find);
            }
//        } else {
//            return ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId);
//        }
    }


    @DeleteMapping("/employees/{employeeId}/users/{userId}")
    public ReturnValue delete(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId) {
        if (redisService.hasRedis(employeeId, EMPLOYEE)) {
            if (permissionService.findIsPermission(DELETE, employeeId)) {
                UserEntity userEntity = employeeService.findUser(userId);
                if (userEntity != null) {
                    if (redisService.hasRedis(userId, USER)) {
                        redisService.deleteRedis(userId, USER);
                    }
                    return ReturnValue.success(employeeService.deleteUser(userId));
                } else {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, userId);
                }
            } else {
                return ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, employeeId + ":" + DELETE);
            }
        } else {
            return ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId);
        }
    }

    @PutMapping("/employees/{employeeId}/users/{userId}")
    public ReturnValue update(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId, @RequestBody UpdatePassWordDto updatePassWordDTO) {
        if (redisService.hasRedis(employeeId, EMPLOYEE)) {
            if (permissionService.findIsPermission(UPDATE, employeeId)) {
                UserEntity userEntity = employeeService.findUser(userId);
                if (userEntity != null) {
                    if (redisService.hasRedis(userId, USER)) {
                        LoginDto loginDto = new LoginDto();
                        loginDto.setAccount(userEntity.getAccount());
                        loginDto.setPassWord(DigestUtils.md5DigestAsHex(updatePassWordDTO.getPassWord().getBytes()));
                        redisService.addRedis(loginDto, USER);
                    }
                    return ReturnValue.success(employeeService.updateUser(userId, updatePassWordDTO.getPassWord()));
                } else {
                    return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, userId);
                }
            } else {
                return ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, employeeId + ":" + UPDATE);
            }
        } else {
            return ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId);
        }
    }

}