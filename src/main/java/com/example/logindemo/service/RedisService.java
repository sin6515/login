package com.example.logindemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author hrh13
 * @date 2021/7/20
 */
@Component
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public void addRedis(String key,String value){

        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key,3,TimeUnit.DAYS);
    }
}
