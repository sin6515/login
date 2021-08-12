package com.example.logindemo.entity;

import lombok.Data;

import javax.persistence.*;

import static com.example.logindemo.dto.ConstantValue.NORMAL;

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
    @Column(name = "employee_category")
    private String category;
    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;

    public EmployeeEntity(String account, String passWord, String nickname, long gmtCreate) {
        setAccount(account);
        setPassWord(passWord);
        setNickname(nickname);
        setGmtCreate(gmtCreate);
        setCategory(NORMAL);
    }

    public EmployeeEntity() {

    }
}