package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/30
 */
@Data
@JSONType(orders = {"id", "account", "passWord", "gmt_creat"})
public class RedisDto {
    private Integer id;
    private String account;
    private String passWord;
    private long gmt_creat;

    public RedisDto(String account, String passWord, long gmt_creat) {
        setAccount(account);
        setPassWord(passWord);
        setGmt_creat(gmt_creat);
    }

    public RedisDto(String id, String account, String passWord, String gmt_creat) {
        setId(Integer.valueOf(id));
        setAccount(account);
        setPassWord(passWord);
        setGmt_creat(Long.parseLong(gmt_creat));
    }
}