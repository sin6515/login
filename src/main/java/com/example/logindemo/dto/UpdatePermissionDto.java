package com.example.logindemo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Data
public class UpdatePermissionDto {
    private Integer roleId;
    private List<String> permissionNameBefore;
    private List<String> permissionNameAfter;
}
