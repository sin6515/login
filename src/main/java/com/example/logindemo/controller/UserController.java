package com.example.logindemo.controller;

import com.example.logindemo.dto.LoginDTO;
import com.example.logindemo.dto.UpdateDTO;
import com.example.logindemo.entity.UserEntity;
import com.example.logindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author hrh13
 * @date 2021/7/12
 */
@Controller
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path="/user/login" )
    public String loginController(@RequestBody LoginDTO loginDTO) {
        return userService.loginUser(loginDTO);
//        return  userService.loginUser(loginDTO.getId(),loginDTO.getAccount(), loginDTO.getPassWord());
    }

    @GetMapping(path="/user/{id}" )
    public Optional<UserEntity> findController(@PathVariable("id") Integer id) {
        return  userService.findUser(id);
    }

    @PostMapping("/user" )
    public String addController( @RequestBody UserEntity userEntity) {
        if(!userEntity.getAccount().isEmpty()&&!userEntity.getPassWord().isEmpty()&&!userEntity.getNickname().isEmpty()){
            return  userService.addUser(userEntity);
        }
        else{
            return "add error";
        }
    }

    @DeleteMapping("/user/{id}")
    public String deleteController(@PathVariable("id") Integer id) {
        return  userService.deleteUser(id);
    }

    @PutMapping("/user")
    public String updateController(@RequestBody UpdateDTO updateDTO) {
        return  userService.updateUser(updateDTO.getId(), updateDTO.getPassWord());
    }
}


