package com.example.logindemo.controller;

import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.RedisService;
import com.example.logindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private EmployeeRoleService employeeRoleService;

    @PostMapping(path = "/users/login")
    public ReturnValue login(@RequestBody LoginDto loginDTO) {
        return userService.loginUser(loginDTO);
    }

    @PostMapping("/users")
    public ReturnValue add(@RequestBody AddDto addDto) {
        return userService.addUser(addDto);
    }
}


