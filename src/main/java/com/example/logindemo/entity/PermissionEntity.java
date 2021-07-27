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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_lv")
    private Integer permissionLv;
    @Column(name = "gmt_create")
    private long gmt_create;
    @Column(name = "gmt_modified")
    private long gmt_modified;
    public PermissionEntity(String permissionName,long gmt_create){
        setPermissionName(permissionName);
        setGmt_create(gmt_create);
    }

    public PermissionEntity() {

    }
}
