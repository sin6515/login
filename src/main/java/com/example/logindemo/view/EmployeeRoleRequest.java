package com.example.logindemo.view;

import lombok.Data;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class EmployeeRoleRequest {
    Integer employeeId;
    List<Integer> roleId;

    public EmployeeRoleRequest(Integer employeeId, List<Integer> roleId) {
        this.setEmployeeId(employeeId);
        this.setRoleId(roleId);
    }
}
