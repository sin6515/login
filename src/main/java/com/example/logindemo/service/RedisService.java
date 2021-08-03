package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    private String key, value;

    public String returnKey(Integer id, String redisName) {
        if (EMPLOYEE.equals(redisName)) {
            key = REDIS_EMPLOYEE + id;
        } else {
            key = REDIS_USER + id;
        }
        return key;
    }

    public void addRedis(LoginDto loginDtO, String redisName) {
        RedisDto redisDto = new RedisDto(loginDtO.getAccount(),
                loginDtO.getPassWord(), System.currentTimeMillis());
        if (USER.equals(redisName)) {
            redisDto.setId(userDao.findIdByAccount(redisDto.getAccount()));
        } else {
            redisDto.setId(employeeDao.findIdByAccount(redisDto.getAccount()));
        }
        key = returnKey(redisDto.getId(), redisName);
        String jsonStr = JSON.toJSONString(redisDto);
        stringRedisTemplate.opsForValue().set(key, jsonStr);
        stringRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
    }

    public void deleteRedis(Integer id, String redisName) {
        stringRedisTemplate.delete(returnKey(id, redisName));
    }


    public RedisDto findRedis(Integer id, String redisName) {
        value = stringRedisTemplate.opsForValue().get(returnKey(id, redisName));
        JSONObject jsonObject = JSON.parseObject(value);
        RedisDto redisDto = new RedisDto(jsonObject.getString("id"), jsonObject.getString("account"),
                jsonObject.getString("passWord"), jsonObject.getString("gmt_creat"));
        return redisDto;
    }

    public boolean hasRedis(Integer id, String redisName) {
        return stringRedisTemplate.hasKey(returnKey(id, redisName));
    }


}
