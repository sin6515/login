package com.example.logindemo.entity;

import com.example.logindemo.dto.RoleIdNameDto;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Entity
@Table(name = "role")
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id")
    @GenericGenerator(name = "id", strategy = "com.example.logindemo.generator.ManulInsertGenerator")
    private Integer id;
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "permission_lv")
    private Integer permissionLv;
    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;

    public RoleEntity(String roleName, long gmtCreate) {
        setRoleName(roleName);
        setGmtCreate(gmtCreate);
    }

    public RoleEntity(RoleIdNameDto roleIdNameDto, long gmtCreate) {
        setId(roleIdNameDto.getRoleId());
        setRoleName(roleIdNameDto.getRoleName());
        setGmtCreate(gmtCreate);
    }

    public RoleEntity() {

    }

}
