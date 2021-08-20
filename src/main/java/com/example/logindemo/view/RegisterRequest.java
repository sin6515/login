package com.example.logindemo.view;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class RegisterRequest {
    @NotBlank
    private String account;
    @NotBlank
    private String passWord;
    @NotBlank
    private String nickname;
    private String email;
    private String phone;
}
