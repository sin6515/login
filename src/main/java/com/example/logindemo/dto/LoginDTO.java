package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * @author hrh13
 * @date 2021/7/16
 */
@JSONType(orders = {"id","account","passWord","gmtCreate"})
 public class LoginDTO {
    private String account;
    private String passWord;
    private long gmtCreate;
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
    public void setGmtCreate(){
        this.gmtCreate=System.currentTimeMillis();
    }
    public long getGmtCreate(){
        return gmtCreate;
    }
}
