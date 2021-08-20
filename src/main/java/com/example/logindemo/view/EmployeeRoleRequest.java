package com.example.logindemo.view;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hrh13
 * @date 2021/8/19
 */
@Data
public class EmployeeRoleRequest {
    @NotNull
    Integer employeeId;
    @NotEmpty
    List<Integer> roleId;

    public EmployeeRoleRequest(Integer employeeId, List<Integer> roleId) {
        this.setEmployeeId(employeeId);
        this.setRoleId(roleId);
    }
}
