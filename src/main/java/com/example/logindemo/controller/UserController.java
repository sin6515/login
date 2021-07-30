package com.example.logindemo.controller;

import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.service.ReturnValueService;
import com.example.logindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.logindemo.dto.ConstantValue.*;

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
    private ReturnValueService returnValueService;

    @PostMapping("/users")
    public ReturnValue add(@RequestBody AddDto addDto) {
        return userService.addUser(addDto);
    }

    @PostMapping(path = "/users/login")
    public ReturnValue login(@RequestBody LoginDto loginDto) {
        LoginDto loginDtoFound=userService.findUser(loginDto.getAccount());
        if (null==loginDtoFound) {
            return returnValueService.failState(USER, LOGIN_ERROR_ACCOUNT, loginDto.getAccount(), BAD_REQUEST_CODE);
        }
        else if(!loginDtoFound.getPassWord().equals(DigestUtils.md5DigestAsHex(loginDto.getPassWord().getBytes()))){
            return returnValueService.failState(USER, LOGIN_ERROR_PASSWORD, loginDto.getAccount(), BAD_REQUEST_CODE);
        }
        else {
            ReturnDetailValue returnDetailValue = new ReturnDetailValue(loginDto.getAccount());
            return returnValueService.succeedState(LOGIN_SUCCEED, returnDetailValue);
        }
    }
}


