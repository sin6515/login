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
public class RolePermissionRequest {
    @NotNull
    private Integer roleId;
    @NotEmpty
    private List<String> permissionName;
}
