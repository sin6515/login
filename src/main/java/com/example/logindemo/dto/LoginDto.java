package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.logindemo.entity.EmployeeEntity;
import com.example.logindemo.entity.UserEntity;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/16
 */
@Data
@JSONType(orders = {"account","passWord"})
public class LoginDto {
    private String account;
    private String passWord;
    public LoginDto(UserEntity userEntity) {
        setAccount(userEntity.getAccount());
        setPassWord(userEntity.getPassWord());
    }
    public LoginDto(EmployeeEntity employeeEntity) {
        setAccount(employeeEntity.getAccount());
        setPassWord(employeeEntity.getPassWord());
    }
    public LoginDto() {

    }

}
