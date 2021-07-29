package com.example.logindemo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Entity
@Data
@Table(name = "employee")

public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employee_account")
    private String account;

    @Column(name = "employee_password")
    private String passWord;

    @Column(name = "employee_nickname")
    private String nickname;

    private long gmt_create;
    private long gmt_modified;

    public EmployeeEntity(String account, String passWord, String nickname, long gmt_create) {
        setAccount(account);
        setPassWord(passWord);
        setNickname(nickname);
        setGmt_create(gmt_create);
    }

    public EmployeeEntity() {

    }
}