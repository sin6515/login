package com.example.logindemo.view;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class UpdatePasswordRequest {
    private Integer id;
    private String passWord;
}
