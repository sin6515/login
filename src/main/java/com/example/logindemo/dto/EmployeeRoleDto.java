package com.example.logindemo.dto;

import com.example.logindemo.entity.EmployeeRoleEntity;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/2
 */
@Data
public class EmployeeRoleDto {
    Integer employeeId;
    Integer roleId;

    public EmployeeRoleDto(EmployeeRoleEntity employeeRoleEntity) {
        setEmployeeId(employeeRoleEntity.getEmployeeId());
        setRoleId(employeeRoleEntity.getRoleId());
    }
}
