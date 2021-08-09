package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/2
 */
@Data
@JSONType(orders = {"roleId", "roleName"})
public class RoleIdNameDto {
    private Integer roleId;
    private String roleName;

    public RoleIdNameDto(Integer roleId, String roleName) {
        setRoleId(roleId);
        setRoleName(roleName);
    }
}
