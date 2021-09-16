package com.example.logindemo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Entity
@Table(name = "permission")
@Data
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "permission_code")
    private String permissionName;
    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;

    public PermissionEntity() {

    }
}
