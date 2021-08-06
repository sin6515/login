package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.logindemo.entity.UserEntity;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/6
 */
@Data
@JSONType(orders = {"id","account","passWord","nickname","email","phone"})
public class UserDto {
    private Integer id;
    private String account;
    private String passWord;
    private String nickname;
    private String email;
    private String phone;

    public UserDto(UserEntity userEntity) {
        setId(userEntity.getId());
        setAccount(userEntity.getAccount());
        setPassWord(userEntity.getPassWord());
        setNickname(userEntity.getNickname());
        setEmail(userEntity.getEmail());
        setPhone(userEntity.getPhone());
    }

}
