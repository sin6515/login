package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
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
        setAccount(account);
        setPassWord(passWord);
        setGmt_creat(gmt_creat);
        setExpireTime(gmt_creat + TIME_OUT_MILLS);
    }

    public RedisDto(String id, String account, String passWord, String gmt_creat) {
        setId(Integer.valueOf(id));
        setAccount(account);
        setPassWord(passWord);
        setGmt_creat(Long.parseLong(gmt_creat));
        setExpireTime(Long.parseLong(gmt_creat) + 1000L * 60 * 60 * 24 * TIME_OUT);
    }

    public RedisDto(Integer employeeId, long gmt_creat) {
        setId(id);
        setGmt_creat(gmt_creat);
        setExpireTime(gmt_creat + TIME_OUT_MILLS);
    }
}