package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author hrh13
 * @date 2021/7/20
 */
@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmployeeDao employeeDao;
    String USER_NAME = "user";
    String EMPLOYEE_NAME = "employee";

    public String addRedis(LoginDto loginDtO, String name) {
        String id = "";
        if (name.equals(USER_NAME)) {
            loginDtO.setId(userDao.findIdByAccount(loginDtO.getAccount()));
            id = "login_user:" + loginDtO.getId();
        } else {
            loginDtO.setId(employeeDao.findIdByAccount(loginDtO.getAccount()));
            id = "login_employee:" + loginDtO.getId();
        }
        loginDtO.setGmtCreate(System.currentTimeMillis());
        String jsonStr = JSON.toJSONString(loginDtO);
        stringRedisTemplate.opsForValue().set(id, jsonStr);
        stringRedisTemplate.expire(id, 3, TimeUnit.DAYS);
        return "登陆成功！";
    }

    public boolean hasKey(Integer id) {
        String key = "login_employee:" + id;
        return stringRedisTemplate.hasKey(key);
    }
}
