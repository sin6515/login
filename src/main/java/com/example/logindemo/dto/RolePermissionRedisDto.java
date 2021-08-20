package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/8/3
 */
@Data
@JSONType(orders = {"roleId", "roleName", "permission"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RolePermissionRedisDto {
    private Integer roleId;
    private String roleName;
    private List<String> permissionName;

    public RolePermissionRedisDto(Integer roleId, String roleName, List<String> permissionName) {
        this.setRoleId(roleId);
        this.setRoleName(roleName);
        this.setPermissionName(permissionName);
    }
}
