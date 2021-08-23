package com.example.logindemo.view;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.logindemo.entity.UserEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
@JSONType(orders = {"account", "passWord"})
public class LoginRequest {
    @NotBlank
    private String account;
    @NotBlank
    private String passWord;

    public LoginRequest(UserEntity userEntity) {
        this.setAccount(userEntity.getAccount());
        this.setPassWord(userEntity.getPassWord());
    }

    public LoginRequest() {

    }
}
