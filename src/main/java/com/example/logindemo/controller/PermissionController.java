package com.example.logindemo.controller;

import com.example.logindemo.dto.PermissionDto;
import com.example.logindemo.dto.UpdatePermissionDto;
import com.example.logindemo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Controller
@RestController
public class PermissionController {
    @Autowired
    private PermissionService permissionService;


    @PostMapping("/permissions")
    public String addPermission(@RequestBody PermissionDto permissionDTO) {
        return permissionService.addPermission(permissionDTO.getRoleId(), permissionDTO.getPermissionName());
    }

    @PutMapping("/permissions")
    public String updatePermission(@RequestBody UpdatePermissionDto updatePermissionDTO) {
        return permissionService.updatePermission(updatePermissionDTO.getRoleId(), updatePermissionDTO.getPermissionName1(),
                updatePermissionDTO.getPermissionName2());
    }

    @DeleteMapping("/permissions")
    public String deletePermission(@RequestBody PermissionDto permissionDTO) {
        return permissionService.deletePermission(permissionDTO.getRoleId(), permissionDTO.getPermissionName());
    }
}
