package com.example.logindemo.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/13
 */
@Entity
@Data
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "user_account"))
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

    private long gmt_create;
    private long gmt_modified;

    public UserEntity(String account, String passWord, String nickname, String email, String phone, long gmt_create) {
        setAccount(account);
        setPassWord(passWord);
        setNickname(nickname);
        setEmail(email);
        setPhone(phone);
        setGmt_create(gmt_create);
    }

    public UserEntity() {

    }
}
