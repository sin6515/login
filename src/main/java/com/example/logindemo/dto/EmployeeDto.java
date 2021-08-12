package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.logindemo.entity.EmployeeEntity;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/6
 */
@Data
@JSONType(orders = {"id","account","passWord","nickname"})
public class EmployeeDto {
    private Integer id;
    private String account;
    private String passWord;
    private String nickname;

    public EmployeeDto(EmployeeEntity employeeEntity) {
        setId(employeeEntity.getId());
        setAccount(employeeEntity.getAccount());
        setPassWord(employeeEntity.getPassWord());
        setNickname(employeeEntity.getNickname());

    }
}
