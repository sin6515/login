package com.example.logindemo.dto;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/29
 */
@Data
public class UpdateEmployeeRoleDto {
    private Integer employeeId;
    private Integer roleId1;
    private Integer roleId2;
}
