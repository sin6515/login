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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "permission_id")
    private Integer permissionId;
    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;

    public RolePermissionEntity(Integer roleId, Integer permissionId, long gmtCreate) {
        setRoleId(roleId);
        setPermissionId(permissionId);
        setGmtCreate(gmtCreate);
    }

    public RolePermissionEntity() {

    }
}
