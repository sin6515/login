package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private EmployeeRoleDao employeeRoleDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private ReturnValueService returnValueService;

    public String addRole(String roleName) {
        if (!roleDao.existsByRoleName(roleName)) {
            RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
            roleDao.save(roleEntity);
            Integer roleId = roleDao.findIdByRoleName(roleName);
            return returnValueService.succeedState(ROLE, ADD_SUCCEED, roleId, OK_CODE);
        } else {
            return returnValueService.failState(ROLE, ADD_FAILED, roleName, BAD_REQUEST_CODE);
        }
    }

    public String deleteRole(Integer roleId) {
        if (roleDao.existsById(roleId)) {
            roleDao.deleteById(roleId);
            employeeRoleDao.deleteByRoleId(roleId);
            rolePermissionDao.deleteByRoleId(roleId);
            return returnValueService.succeedState(ROLE, DELETE_SUCCEED, roleId, OK_CODE);
        } else {
            return returnValueService.failState(ROLE, DELETE_FAILED, roleId, NOT_FOUND_CODE);

        }


    }
}
