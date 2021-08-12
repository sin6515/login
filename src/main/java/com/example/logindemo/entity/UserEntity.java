package com.example.logindemo.entity;


import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/13
 */
@Entity
@Data
@Table(name = "user")
@JSONType(orders = {"id", "account", "passWord", "nickname", "email", "phone", "gmtCreate", "gmtModified"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_account")
    private String account;

    @Column(name = "user_password")
    private String passWord;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_phone")
    private String phone;

    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;

    public UserEntity(String account, String passWord, String nickname, String email, String phone, long gmtCreate) {
        setAccount(account);
        setPassWord(passWord);
        setNickname(nickname);
        setEmail(email);
        setPhone(phone);
        setGmtCreate(gmtCreate);
    }

    public UserEntity() {

    }
}
