package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/16
 */
@Data
@JSONType(orders = {"id","account","passWord","gmtCreate"})
 public class LoginDto {
    private Integer id;
    private String account;
    private String passWord;
    private long gmtCreate;

}
