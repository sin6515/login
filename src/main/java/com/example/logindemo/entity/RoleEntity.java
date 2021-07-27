package com.example.logindemo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = "role_name"))
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "permission_lv")
    private Integer permissionLv;
    private long gmt_create;
    private long gmt_modified;

    public RoleEntity(String roleName, long gmt_create) {
        setRoleName(roleName);
        setGmt_create(gmt_create);
    }

    public RoleEntity() {

    }
}
