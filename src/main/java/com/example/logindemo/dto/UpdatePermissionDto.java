package com.example.logindemo.dto;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Data
public class UpdatePermissionDto {
    private Integer roleId;
    private String permissionName1;
    private String permissionName2;
}
