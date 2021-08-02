package com.example.logindemo.dto;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/8/2
 */
@Data
public class FindEmployeeRoleDto {
    Integer employeeId;
    Integer roleId;

    public FindEmployeeRoleDto( Integer employeeId, Integer roleId) {
        setEmployeeId(employeeId);
        setRoleId(roleId);
    }
}
