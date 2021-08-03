package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/3
 */
@Data
@JSONType(orders = {"roleId","roleName","permission"})
public class RolePermissionRedisDto<T> {
    private Integer roleId;
    private String roleName;
    private T permission;
    public  RolePermissionRedisDto(Integer roleId,String roleName,T permission){
        setRoleId(roleId);
        setRoleName(roleName);
        setPermission(permission);
    }
    public  RolePermissionRedisDto(Integer roleId){
        setRoleId(roleId);
    }
}
