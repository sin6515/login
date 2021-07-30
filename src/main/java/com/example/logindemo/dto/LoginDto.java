package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
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

    public LoginDto(String account, String passWord) {
        setAccount(account);
        setPassWord(passWord);
    }
    public LoginDto() {
    }
}
