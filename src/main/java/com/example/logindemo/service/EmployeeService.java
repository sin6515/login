package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.entity.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ReturnValueService returnValueService;
    @Autowired
    private RedisService redisService;

    public String addEmployee(AddDto addDto) {
        if (employeeDao.findByAccount(addDto.getAccount()).isEmpty()) {
            EmployeeEntity employeeEntity = new EmployeeEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                    addDto.getNickname(), System.currentTimeMillis());
            employeeDao.save(employeeEntity);
            Integer employeeId = employeeDao.findIdByAccount(addDto.getAccount());
            ReturnDetailValue returnDetailValue = new ReturnDetailValue(employeeId);
            return returnValueService.succeedState(REGISTER_SUCCEED, returnDetailValue);
        } else {
            return returnValueService.failState(USER, ADD_FAILED, addDto.getAccount(), BAD_REQUEST_CODE);
        }

    }

    public String loginEmployee(LoginDto loginDTO) {
        loginDTO.setPassWord(DigestUtils.md5DigestAsHex(loginDTO.getPassWord().getBytes()));
        if (employeeDao.findByAccount(loginDTO.getAccount()).isEmpty()) {
            return returnValueService.failState(USER, LOGIN_ERROR_ACCOUNT, loginDTO.getAccount(), BAD_REQUEST_CODE);
        } else if (employeeDao.findByAccountAndPassWord(loginDTO.getAccount(),
                loginDTO.getPassWord()).isEmpty()) {
            return returnValueService.failState(USER, LOGIN_ERROR_PASSWORD, loginDTO.getAccount(), BAD_REQUEST_CODE);
        } else {
            redisService.addRedis(loginDTO, EMPLOYEE);
            ReturnDetailValue returnDetailValue = new ReturnDetailValue(loginDTO.getAccount());
            return returnValueService.succeedState(LOGIN_SUCCEED, returnDetailValue);
        }
    }

    public String findUser(Integer employeeId, Integer userId) {
        String permissionName = Find;
        if (SUCCEED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            if (userDao.existsById(userId)) {
                return returnValueService.succeedFindState(userDao.findById(userId).get());
            } else {
                return returnValueService.failState(EMPLOYEE, FIND_FAILED, userId, NOT_FOUND_CODE);
            }
        } else if (FAILED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            return returnValueService.failState(EMPLOYEE, FIND_FAILED, employeeId, FORBIDDEN_CODE);
        } else {
            return returnValueService.failState(EMPLOYEE, FIND_FAILED, employeeId, NO_LOGIN_CODE);
        }
    }

    public String deleteUser(Integer employeeId, Integer userId) {
        String permissionName = DELETE;
        if (SUCCEED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            if (userDao.existsById(userId)) {
                userDao.deleteById(userId);
                ReturnDetailValue returnDetailValue = new ReturnDetailValue(userId);
                return returnValueService.succeedState(DELETE_SUCCEED, returnDetailValue);
            } else {
                return returnValueService.failState(EMPLOYEE, DELETE_FAILED, userId, NOT_FOUND_CODE);
            }
        } else if (FAILED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            return returnValueService.failState(EMPLOYEE, DELETE_FAILED, employeeId, FORBIDDEN_CODE);
        } else {
            return returnValueService.failState(EMPLOYEE, DELETE_FAILED, employeeId, NO_LOGIN_CODE);
        }
    }

    public String updateUser(Integer employeeId, Integer userId, String pd) {
        String permissionName = UPDATE;
        if (SUCCEED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            if (userDao.existsById(userId)) {
                userDao.updatePassWordById(DigestUtils.md5DigestAsHex(pd.getBytes()), System.currentTimeMillis(), userId);
                ReturnDetailValue returnDetailValue = new ReturnDetailValue(userId);
                return returnValueService.succeedState(UPDATE_SUCCEED, returnDetailValue);
            } else {
                return returnValueService.failState(EMPLOYEE, UPDATE_FAILED, userId, NOT_FOUND_CODE);
            }
        } else if (FAILED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            return returnValueService.failState(EMPLOYEE, UPDATE_FAILED, employeeId, FORBIDDEN_CODE);
        } else {
            return returnValueService.failState(EMPLOYEE, UPDATE_FAILED, employeeId, NO_LOGIN_CODE);
        }
    }
}
