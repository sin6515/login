package com.example.logindemo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Entity
@Table(name = "employee_role")
@Data
public class EmployeeRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;

    public EmployeeRoleEntity(Integer employeeId, Integer roleId, long gmtCreate) {
        this.setEmployeeId(employeeId);
        this.setRoleId(roleId);
        this.setGmtCreate(gmtCreate);
    }

    public EmployeeRoleEntity() {

    }
}
