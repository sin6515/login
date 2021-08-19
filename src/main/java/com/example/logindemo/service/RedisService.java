package com.example.logindemo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/20
 */
@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String key;

    public String returnKey(Integer id, String redisName) {
        if (EMPLOYEE.equals(redisName)) {
            key = REDIS_EMPLOYEE + id;
        } else if (USER.equals(redisName)) {
            key = REDIS_USER + id;
        } else {
            key = REDIS_ROLE + id;
        }
        return key;
    }

    public String creatToken(Integer id, String redisName) {
        return JWT.create()
                .withClaim(redisName, id)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TIME_OUT_MILLS))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public Integer findTokenId(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        return Integer.parseInt(String.valueOf(jwt.getClaim(EMPLOYEE)));
    }

    public void updateRedis(String key, String value) {
        if (addLock(key)) {
            stringRedisTemplate.opsForValue().set(key, value);
            stringRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
            deleteLock(key);
        }
    }

    public String findRedis(Integer id, String redisName) {
        return stringRedisTemplate.opsForValue().get(returnKey(id, redisName));
    }

    public void deleteRedis(Integer id, String redisName) {
        key = returnKey(id, redisName);
        if (addLock(key)) {
            stringRedisTemplate.delete(key);
            deleteLock(key);
        }
    }

    public boolean existsRedis(Integer id, String redisName) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(returnKey(id, redisName)));
    }

    public boolean addLock(String key) {
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(LOCK_KEY + key, LOCK_VALUE, LOCK_TIME_OUT, TimeUnit.SECONDS);
        return lock != null && lock;
    }

    public void deleteLock(String key) {
        stringRedisTemplate.delete(LOCK_KEY + key);
    }

    public boolean addDateBaseLock(Integer id, String redisName) {
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(LOCK_KEY + DATABASE + returnKey(id, redisName), LOCK_VALUE, LOCK_TIME_OUT, TimeUnit.SECONDS);
        return lock != null && lock;
    }

    public void deleteDataLock(Integer id, String redisName) {
        stringRedisTemplate.delete(LOCK_KEY + DATABASE + returnKey(id, redisName));
    }

}
