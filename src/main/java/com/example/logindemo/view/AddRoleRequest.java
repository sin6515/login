package com.example.logindemo.view;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class AddRoleRequest {
    @NotBlank
    String roleName;
}
