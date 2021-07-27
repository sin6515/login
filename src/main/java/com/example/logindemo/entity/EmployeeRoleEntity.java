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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "role_id")
    private Integer roleId;
    private long gmt_create;
    private long gmt_modified;
    public EmployeeRoleEntity(Integer employeeId, Integer roleId, long gmt_create){
        setEmployeeId(employeeId);
        setRoleId(roleId);
        setGmt_create(gmt_create);
    }

    public EmployeeRoleEntity() {

    }
}
