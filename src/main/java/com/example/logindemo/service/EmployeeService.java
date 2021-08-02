package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.entity.EmployeeEntity;
import com.example.logindemo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private UserDao userDao;

    public EmployeeEntity addEmployee(AddDto addDto) {
        EmployeeEntity employeeEntity = new EmployeeEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                addDto.getNickname(), System.currentTimeMillis());
        employeeDao.save(employeeEntity);
        return employeeEntity;

    }

    public LoginDto findEmployee(String account) {
        if (employeeDao.findByAccount(account) == null) {
            return null;
        } else {
            LoginDto loginDto = new LoginDto(employeeDao.findByAccount(account).getAccount(),
                    employeeDao.findByAccount(account).getPassWord());
            return loginDto;
        }
    }

    public LoginDto findEmployeeById(Integer id) {
        if (employeeDao.findById(id) == null) {
            return null;
        } else {
            LoginDto loginDto = new LoginDto();
            return loginDto;
        }
    }

    public UserEntity findUser(Integer userId) {

        if (userDao.findById(userId).isPresent()) {
            return userDao.findById(userId).get();
        } else {
            return null;
        }
    }

    public UserEntity deleteUser(Integer userId) {
        UserEntity userEntity = findUser(userId);
        userDao.deleteById(userId);
        return userEntity;
    }

    public UserEntity updateUser(Integer userId, String pd) {

        userDao.updatePassWordById(DigestUtils.md5DigestAsHex(pd.getBytes()), System.currentTimeMillis(), userId);
        UserEntity userEntity = findUser(userId);
        return userEntity;

    }
}
