package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.RedisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.ConstantValue.TIME_OUT;

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
        RedisDto redisDto = new RedisDto(loginDtO.getAccount(),
                loginDtO.getPassWord(), System.currentTimeMillis());
        if (name.equals(USER)) {
            redisDto.setId(userDao.findIdByAccount(redisDto.getAccount()));
            id = REDIS_USER + redisDto.getId();
        } else {
            redisDto.setId(employeeDao.findIdByAccount(redisDto.getAccount()));
            id = REDIS_EMPLOYEE + redisDto.getId();
        }
        String jsonStr = JSON.toJSONString(redisDto);
        stringRedisTemplate.opsForValue().set(id, jsonStr);
        stringRedisTemplate.expire(id, TIME_OUT, TimeUnit.DAYS);
    }

    public boolean hasKey(Integer id) {
        String key = REDIS_EMPLOYEE + id;
        return stringRedisTemplate.hasKey(key);
    }
}
