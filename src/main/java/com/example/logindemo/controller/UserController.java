package com.example.logindemo.controller;

import com.example.logindemo.dto.*;
import com.example.logindemo.entity.UserEntity;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.error.PasswordErrorException;
import com.example.logindemo.error.RepeatAskException;
import com.example.logindemo.service.RedisService;
import com.example.logindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.logindemo.dto.ConstantValue.ACCOUNT;
import static com.example.logindemo.dto.ConstantValue.PASSWORD;

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
    public ReturnValue<UserDto> add(@RequestBody AddDto addDto) throws RepeatAskException {
        UserEntity loginFound = userService.findByAccount(addDto.getAccount());
        if (null == loginFound) {
            return ReturnValue.success(userService.addUser(addDto));
        } else {
            throw new RepeatAskException(ACCOUNT + " : " + addDto.getAccount());
        }
    }

    @PostMapping(path = "/users/login")
    public ReturnValue<LoginDto> login(@RequestBody LoginDto loginDto) throws NotFoundException, PasswordErrorException {
        UserEntity loginFound = userService.findByAccount(loginDto.getAccount());
        if (null == loginFound) {
            throw new NotFoundException(ACCOUNT + " : " + loginDto.getAccount());
        } else if (!loginFound.getPassWord().equals(DigestUtils.md5DigestAsHex(loginDto.getPassWord().getBytes()))) {
            throw new PasswordErrorException(PASSWORD + " : " + loginDto.getPassWord());
        } else {
            return ReturnValue.success(new LoginTokenDto(redisService.updateUserRedis(new LoginDto(loginFound))));
        }
    }
}


