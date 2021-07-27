package com.example.logindemo.controller;

import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.UpdateDto;
import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.EmployeeService;
import com.example.logindemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.logindemo.dto.ConstantValue.SUCCEED;

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
    private EmployeeRoleService employeeRoleService;

    @PostMapping("/employees")
    public String add(@RequestBody AddDto addDto) {
        if (!addDto.getAccount().isEmpty() && !addDto.getPassWord().isEmpty() && !addDto.getNickname().isEmpty()) {
            employeeService.addEmployee(addDto);
            return employeeRoleService.registerEmployeeRole(addDto);

        } else {
            return "add error";
        }
    }

    @PostMapping(path = "/employees/login")
    public String login(@RequestBody LoginDto loginDTO) {
        String loginError;
        if (SUCCEED.equals(loginError = employeeService.loginEmployee(loginDTO))) {
            return redisService.addRedis(loginDTO, "employee");

        } else {
            return loginError;
        }
    }

    @GetMapping(path = "/employees/{employeeId}/users/{userId}")
    public Object find(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId) {
        return employeeService.findUser(employeeId, userId);
    }


    @DeleteMapping("/employees/{employeeId}/users/{userId}")
    public String delete(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId) {

        String deleteError;
        if (SUCCEED.equals(deleteError = employeeService.deleteUser(employeeId, userId))) {

            return "成功删除用户" + userId;
        } else {
            return deleteError;
        }

    }

    @PutMapping("/employees/{employeeId}/users/{userId}")
    public String update(@PathVariable("employeeId") Integer employeeId, @PathVariable("userId") Integer userId, @RequestBody UpdateDto updateDTO) {
        return employeeService.updateEmployee(employeeId, userId, updateDTO.getPassWord());
    }

}