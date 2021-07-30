package com.example.logindemo.controller;

import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.dto.UpdateDto;
import com.example.logindemo.service.EmployeeService;
import com.example.logindemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/employees")
    public ReturnValue add(@RequestBody AddDto addDto) {
        return employeeService.addEmployee(addDto);
    }

    @PostMapping(path = "/employees/login")
    public ReturnValue login(@RequestBody LoginDto loginDTO) {
        return employeeService.loginEmployee(loginDTO);
    }

    @GetMapping(path = "/employees/{employeeId}/users/{userId}")
    public Object find(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId) {
        return employeeService.findUser(employeeId, userId);
    }


    @DeleteMapping("/employees/{employeeId}/users/{userId}")
    public ReturnValue delete(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId) {
        return employeeService.deleteUser(employeeId, userId);
    }

    @PutMapping("/employees/{employeeId}/users/{userId}")
    public ReturnValue update(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId, @RequestBody UpdateDto updateDTO) {
        return employeeService.updateUser(employeeId, userId, updateDTO.getPassWord());
    }

}