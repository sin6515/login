package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ReturnValue addRole(String roleName) {
        if (!roleDao.existsByRoleName(roleName)) {
            RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
            roleDao.save(roleEntity);
            Integer roleId = roleDao.findIdByRoleName(roleName);
            ReturnDetailValue returnDetailValue = new ReturnDetailValue(roleId);
            return returnValueService.succeedState(ADD_SUCCEED, returnDetailValue);
        } else {
            return returnValueService.failState(ROLE, ADD_FAILED, roleName, REPEAT_ASK_CODE);
        }
    }

    public List<Integer> findRole(Integer employeeId) {
        return employeeRoleDao.findRoleIdByEmployeeId(employeeId);
    }

    public ReturnValue deleteRole(Integer roleId) {
        if (roleDao.existsById(roleId)) {
            roleDao.deleteById(roleId);
            employeeRoleDao.deleteByRoleId(roleId);
            rolePermissionDao.deleteByRoleId(roleId);
            ReturnDetailValue returnDetailValue = new ReturnDetailValue(roleId);
            return returnValueService.succeedState(DELETE_SUCCEED, returnDetailValue);
        } else {
            return returnValueService.failState(ROLE, DELETE_FAILED, roleId, NOT_FOUND_CODE);

        }


    }
}
