package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import static com.example.logindemo.dto.ConstantValue.TIME_OUT;
import static com.example.logindemo.dto.ConstantValue.TIME_OUT_MILLS;

/**
 * @author hrh13
 * @date 2021/7/30
 */
@Data
@JSONType(orders = {"id", "account", "passWord", "gmt_creat","expireTime","token"})
public class RedisDto {
    private Integer id;
    private String account;
    private String passWord;
    private long gmt_creat;
    private String token;
    private long expireTime;
    public RedisDto(String account, String passWord, long gmt_creat) {
        setAccount(account);
        setPassWord(passWord);
        setGmt_creat(gmt_creat);
        setExpireTime(gmt_creat+TIME_OUT_MILLS);
    }

    public RedisDto(String id, String account, String passWord, String gmt_creat) {
        setId(Integer.valueOf(id));
        setAccount(account);
        setPassWord(passWord);
        setGmt_creat(Long.parseLong(gmt_creat));
        setExpireTime(Long.parseLong(gmt_creat)+1000*60*60*24*TIME_OUT);
    }
}