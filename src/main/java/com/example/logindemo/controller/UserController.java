package com.example.logindemo.controller;

import com.example.logindemo.dto.LoginTokenDto;
import com.example.logindemo.dto.RedisDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.UserDto;
import com.example.logindemo.entity.LoginRecordEntity;
import com.example.logindemo.entity.UserEntity;
import com.example.logindemo.error.NotFoundException;
import com.example.logindemo.error.PasswordErrorException;
import com.example.logindemo.error.RepeatAskException;
import com.example.logindemo.service.UserService;
import com.example.logindemo.view.LoginRequest;
import com.example.logindemo.view.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.logindemo.dto.ConstantValue.ACCOUNT;
import static com.example.logindemo.dto.ConstantValue.PASSWORD;

/**
 * @author hrh13
 * @date 2021/7/12
 */

@Controller
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StreamBridge streamBridge;

    @PostMapping("/users")
    public ReturnValue<UserDto> add(@RequestBody @Valid RegisterRequest request) throws RepeatAskException {
        if (userService.existsByAccount(request.getAccount())) {
            throw new RepeatAskException(ACCOUNT + " : " + request.getAccount());
        } else {
            return ReturnValue.success(userService.addUser(request));
        }
    }

    @PostMapping("/users/login")
    public ReturnValue<LoginRequest> login(@RequestBody @Valid LoginRequest request) throws NotFoundException, PasswordErrorException {
        UserEntity loginFound = userService.findByAccount(request.getAccount());
        if (null == loginFound) {
            streamBridge.send("loginRecord-in-0", new LoginRecordEntity(request));
            throw new NotFoundException(ACCOUNT + " : " + request.getAccount());
        } else if (!loginFound.getPassWord().equals(DigestUtils.md5DigestAsHex(request.getPassWord().getBytes()))) {
            streamBridge.send("loginRecord-in-0", new LoginRecordEntity(request));
            throw new PasswordErrorException(PASSWORD + " : " + request.getPassWord());
        } else {
            RedisDto redisDto = userService.updateUserRedis(new LoginRequest(loginFound));
            log.info("用户" + request.getAccount() + "登录成功");
            streamBridge.send("loginRecord-in-0", new LoginRecordEntity(loginFound));
            return ReturnValue.success(new LoginTokenDto(redisDto));
        }
    }
}


