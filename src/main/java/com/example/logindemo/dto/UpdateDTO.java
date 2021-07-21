package com.example.logindemo.dto;

/**
 * @author hrh13
 * @date 2021/7/16
 */
public class UpdateDTO {
    private Integer id;
    private String  passWord;
    public Integer getId(){
        return id;
    }
    public void setPassWord(String passWord){
        this.passWord=passWord;
    }
    public String getPassWord(){
        return passWord;
    }
}
