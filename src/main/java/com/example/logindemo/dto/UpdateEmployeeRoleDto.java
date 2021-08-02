package com.example.logindemo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hrh13
 * @date 2021/7/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEmployeeRoleDto extends UpdateRoleDto {
    private Integer employeeId;
}
