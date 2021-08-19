package com.example.logindemo.view;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class RegisterRequest {
    private String account;
    private String passWord;
    private String nickname;
    private String email;
    private String phone;
}
