package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Service
public class EmployeeRoleService {
    @Autowired
    private EmployeeRoleDao employeeRoleDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ReturnValueService returnValueService;

    public String addEmployeeRole(Integer employeeId, Integer roleId) {
        if (redisService.hasKey(employeeId)) {
            if (roleDao.existsById(roleId)) {
                if (employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId).isEmpty()) {
                    EmployeeRoleEntity employeeRoleEntity = new EmployeeRoleEntity(employeeId,
                            roleId, System.currentTimeMillis());
                    employeeRoleDao.save(employeeRoleEntity);
                    return returnValueService.succeedState(ROLE, ADD_SUCCEED, employeeId, OK_CODE);
                } else {
                    return returnValueService.failState(ROLE, ADD_FAILED, roleId, BAD_REQUEST_CODE);
                }
            } else {
                return returnValueService.failState(ROLE, ADD_FAILED, roleId, NOT_FOUND_CODE);
            }
        } else {
            return returnValueService.failState(ROLE, ADD_FAILED, employeeId, NO_LOGIN_CODE);
        }
    }

    public String deleteEmployeeRole(Integer employeeId, Integer roleId) {
        if (redisService.hasKey(employeeId)) {
            if (!employeeRoleDao.findByEmployeeId(employeeId).isEmpty()) {
                if (!employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId).isEmpty()) {
                    employeeRoleDao.deleteByEmployeeIdAndRoleId(employeeId, roleId);
                    return returnValueService.succeedState(EMPLOYEE, DELETE_SUCCEED, employeeId, OK_CODE);
                } else {
                    return returnValueService.failState(EMPLOYEE, DELETE_FAILED, roleId, NOT_FOUND_CODE);
                }
            } else {
                return returnValueService.failState(EMPLOYEE, DELETE_FAILED, employeeId, NOT_FOUND_CODE);
            }
        } else {
            return returnValueService.failState(EMPLOYEE, DELETE_FAILED, employeeId, NO_LOGIN_CODE);
        }
    }

    public String updateEmployeeRole(Integer employeeId, Integer roleId1, Integer roleId2) {
        if (redisService.hasKey(employeeId)) {
            if (!employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId1).isEmpty() &&
                    employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId2).isEmpty() &&
                    roleDao.existsById(roleId2)) {

                employeeRoleDao.updateRoleId1ByRoleId2(roleId2, System.currentTimeMillis(), employeeId, roleId1);
                return returnValueService.succeedState(ROLE, UPDATE_SUCCEED, roleId2, OK_CODE);
            } else if (employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId1).isEmpty()) {
                return returnValueService.failState(ROLE, UPDATE_FAILED, roleId1, NOT_FOUND_CODE);

            } else if (!employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId2).isEmpty()) {
                return returnValueService.failState(ROLE, UPDATE_FAILED, roleId2, REPEAT_ASK_CODE);
            } else {
                return returnValueService.failState(ROLE, UPDATE_FAILED, roleId2, NOT_FOUND_CODE);

            }
        } else {
            return returnValueService.failState(ROLE, UPDATE_FAILED, employeeId, NO_LOGIN_CODE);
        }

    }


}