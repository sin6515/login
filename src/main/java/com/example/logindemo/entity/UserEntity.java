package com.example.logindemo.entity;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/13
 */
@Entity
@Table(name = "login_user",uniqueConstraints = @UniqueConstraint(columnNames = "user_account"))
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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

    public Integer getId(){
        return id;
    }
    public void setAccount(String account){
        this.account=account;
    }
    public String getAccount(){
        return account;
    }

    public void setPassWord(String passWord){
        this.passWord=passWord;
    }
    public String getPassWord(){
        return passWord;
    }
    public void setNickname(String nickname){
        this.nickname=nickname;
    }
    public String getNickname(){
        return nickname;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public String getPhone(){
        return phone;
    }
    public void setGmt_Create(){
        this.gmt_create=System.currentTimeMillis();
    }
    public long getGmt_Create(){
        return gmt_create;
    }
    public void setGmt_Modified(){
        this.gmt_modified=System.currentTimeMillis();
    }
    public long getGmt_Modified(){
        return gmt_modified;
    }
}
