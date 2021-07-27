package com.example.logindemo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Entity
@Table(name = "role_permission")
@Data
public class RolePermissionEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "permission_id")
    private Integer permissionId;
    private long gmt_create;
    private long gmt_modified;
    public RolePermissionEntity(Integer roleId,Integer permissionId,long gmt_create){
        setRoleId(roleId);
        setPermissionId(permissionId);
        setGmt_create(gmt_create);
    }

    public RolePermissionEntity() {

    }
}
