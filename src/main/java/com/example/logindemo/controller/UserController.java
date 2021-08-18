package com.example.logindemo.controller;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.LoginTokenDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.entity.UserEntity;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.service.RedisService;
import com.example.logindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.logindemo.dto.ErrorConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/12
 */

@Controller
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/users")
    public ReturnValue add(@RequestBody AddDto addDto) {
        UserEntity loginFound = userService.findByAccount(addDto.getAccount());
        if (null == loginFound) {
            return ReturnValue.success(userService.addUser(addDto));
        } else {
            return ReturnValue.fail(REPEAT_ASK_CODE, ADD_EXISTS, new LoginDto(loginFound));
        }
    }

    @PostMapping(path = "/users/login")
    public ReturnValue<LoginDto> login(@RequestBody LoginDto loginDto) throws NotFoundException {
        UserEntity loginFound = userService.findByAccount(loginDto.getAccount());
        if (null == loginFound) {
            throw new NotFoundException(JSON.toJSONString(loginDto));
        } else if (!loginFound.getPassWord().equals(DigestUtils.md5DigestAsHex(loginDto.getPassWord().getBytes()))) {
            return ReturnValue.fail(BAD_REQUEST_CODE, LOGIN_ERROR_PASSWORD, loginDto);
        } else {
            return ReturnValue.success(new LoginTokenDto(redisService.updateUserRedis(new LoginDto(loginFound))));
        }
    }
}


