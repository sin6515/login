package com.example.logindemo.controller;

import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.service.RedisService;
import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author hrh13
 * @date 2021/7/12
 */

@Controller
@RestController
public class UserController {
    private static final String DELETE_SUCCEED = "succeed";
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private EmployeeRoleService employeeRoleService;

    @PostMapping(path = "/users/login")
    public String login(@RequestBody LoginDto loginDTO) {
        String LOGIN_SUCCEED = "succeed";
        String loginError;
        if (LOGIN_SUCCEED.equals(loginError = userService.loginUser(loginDTO))) {
            return redisService.addRedis(loginDTO, "user");
        } else {
            return loginError;
        }
    }

    @PostMapping("/users")
    public String add(@RequestBody AddDto addDto) {
        if (!addDto.getAccount().isEmpty() && !addDto.getPassWord().isEmpty() && !addDto.getNickname().isEmpty()) {
            return userService.addUser(addDto);
        } else {
            return "add error";
        }
    }


}


