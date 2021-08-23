package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.logindemo.entity.EmployeeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

import static com.example.logindemo.dto.ConstantValue.TIME_OUT;
import static com.example.logindemo.dto.ConstantValue.TIME_OUT_MILLS;

/**
 * @author hrh13
 * @date 2021/7/30
 */
@Data
@JSONType(orders = {"id", "account", "passWord", "category", "roleId", "permissionCode", "gmt_creat", "expireTime", "token"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RedisDto {
    private Integer id;
    private String account;
    private String passWord;
    private String category;
    private List<Integer> roleId;
    private List<String> permissionCode;
    private long gmt_creat;
    private long expireTime;
    private String token;

    public RedisDto(String account, String passWord, long gmt_creat) {
        this.setAccount(account);
        this.setPassWord(passWord);
        this.setGmt_creat(gmt_creat);
        this.setExpireTime(gmt_creat + TIME_OUT_MILLS);
    }

    public RedisDto(String id, String account, String passWord, String gmt_creat) {
        this.setId(Integer.valueOf(id));
        this.setAccount(account);
        this.setPassWord(passWord);
        this.setGmt_creat(Long.parseLong(gmt_creat));
        this.setExpireTime(Long.parseLong(gmt_creat) + 1000L * 60 * 60 * 24 * TIME_OUT);
    }

    public RedisDto(EmployeeEntity employeeEntity, long gmt_creat) {
        this.setId(employeeEntity.getId());
        this.setAccount(employeeEntity.getAccount());
        this.setPassWord(employeeEntity.getPassWord());
        this.setCategory(employeeEntity.getCategory());
        this.setGmt_creat(gmt_creat);
        this.setExpireTime(gmt_creat + TIME_OUT_MILLS);
    }
}