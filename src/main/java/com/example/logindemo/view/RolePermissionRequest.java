package com.example.logindemo.view;

import lombok.Data;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class RolePermissionRequest {
    private Integer roleId;
    private List<String> permissionName;
}
