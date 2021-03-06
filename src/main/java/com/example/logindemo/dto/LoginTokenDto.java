package com.example.logindemo.dto;

import com.example.logindemo.view.LoginRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hrh13
 * @date 2021/8/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginTokenDto extends LoginRequest {
    private String token;

    public LoginTokenDto(RedisDto redisDto) {
        this.setAccount(redisDto.getAccount());
        this.setPassWord(redisDto.getPassWord());
        this.setToken(redisDto.getToken());
    }
}
