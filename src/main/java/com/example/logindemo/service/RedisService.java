package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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


    public void addRedis(LoginDto loginDtO, String name) {
        String id;
//        if (name.equals(USER)) {
//            loginDtO.setId(userDao.findIdByAccount(loginDtO.getAccount()));
//            id = REDIS_USER + loginDtO.getId();
//        } else {
//            loginDtO.setId(employeeDao.findIdByAccount(loginDtO.getAccount()));
//            id = REDIS_EMPLOYEE + loginDtO.getId();
//        }
//        loginDtO.setGmtCreate(System.currentTimeMillis());
//        String jsonStr = JSON.toJSONString(loginDtO);
//        stringRedisTemplate.opsForValue().set(id, jsonStr);
//        stringRedisTemplate.expire(id, TIME_OUT, TimeUnit.DAYS);
    }

    public boolean hasKey(Integer id) {
        String key = "login_employee:" + id;
        return stringRedisTemplate.hasKey(key);
    }
}
