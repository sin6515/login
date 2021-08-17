package com.example.logindemo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hrh13
 * @date 2021/8/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginTokenDto extends LoginDto{
    private String token;
    public LoginTokenDto(RedisDto redisDto){
        setAccount(redisDto.getAccount());
        setPassWord(redisDto.getPassWord());
        setToken(redisDto.getToken());
    }
}
