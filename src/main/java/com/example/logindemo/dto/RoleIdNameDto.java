package com.example.logindemo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hrh13
 * @date 2021/8/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleIdNameDto extends RoleNameDto {
    Integer roleId;
}
