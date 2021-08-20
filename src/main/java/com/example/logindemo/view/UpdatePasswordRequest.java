package com.example.logindemo.view;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class UpdatePasswordRequest {
    @NotNull
    private Integer id;
    @NotBlank
    private String passWord;
}
