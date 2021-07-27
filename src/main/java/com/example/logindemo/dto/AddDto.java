package com.example.logindemo.dto;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Data
public class AddDto {
    private String account;
    private String passWord;
    private String nickname;
    private String email;
    private String phone;
}
