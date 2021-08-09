package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.logindemo.dao.*;
import com.example.logindemo.dto.*;
import com.example.logindemo.entity.PermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Autowired
    private RoleService roleService;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private PermissionDao permissionDao;
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
        String token = JWT.create()
                .withClaim(redisName, id)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TIME_OUT_MILLS))
                .sign(Algorithm.HMAC256(SECRET_KEY));
        return token;
    }

    public boolean verityToken(String token, String redisName, Integer verityId) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        Integer id = Integer.parseInt(String.valueOf(jwt.getClaim(redisName)));
        return verityId.equals(id);
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
        redisDto.setToken(creatToken(redisDto.getId(), redisName));
        String jsonStr = JSON.toJSONString(redisDto);
        if (addLock(key)) {
            stringRedisTemplate.opsForValue().set(key, jsonStr);
            stringRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
            deleteLock(key);
        }
    }

    public RolePermissionRedisDto registerRoleRedis(RoleNameDto roleNameDto) {
        Integer roleId = roleService.findByRoleName(roleNameDto.getRoleName()).getRoleId();
        String roleName = roleService.findByRoleName(roleNameDto.getRoleName()).getRoleName();
        RolePermissionRedisDto redisDto = new RolePermissionRedisDto(roleId, roleName);
        key = REDIS_ROLE + roleId;
        String jsonStr = JSON.toJSONString(redisDto);
        if (addLock(key)) {
            stringRedisTemplate.opsForValue().set(key, jsonStr);
            stringRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
            deleteLock(key);
            return redisDto;
        } else {
            return null;
        }
    }

    public RolePermissionRedisDto updatePermissionRedis(Integer roleId) {
        List<Integer> permissionIdList = rolePermissionDao.findPermissionIdByRoleId(roleId);
        List<String> permissionNameList = permissionDao.findByIdIn(permissionIdList)
                .stream().map(PermissionEntity::getPermissionName).collect(Collectors.toList());
        String roleName = roleService.findByRoleId(roleId).getRoleName();
        RolePermissionRedisDto redisDto = new RolePermissionRedisDto(roleId, roleName, permissionNameList);
        key = REDIS_ROLE + roleId;
        String jsonStr = JSON.toJSONString(redisDto);
        if (addLock(key)) {
            stringRedisTemplate.opsForValue().set(key, jsonStr);
            stringRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
            deleteLock(key);
            return redisDto;
        } else {
            return null;
        }
    }

    public Collection findPermissionRedis(Integer roleId, String redisName) {
        String value = stringRedisTemplate.opsForValue().get(returnKey(roleId, redisName));
        JSONObject jsonObject = JSON.parseObject(value);
        String permission = jsonObject.getString("permission");
        Map<Integer, String> map = JSONObject.parseObject(permission, Map.class);
        return map.values();
    }

    public void deleteRedis(Integer id, String redisName) {
        key = returnKey(id, redisName);
        if (addLock(key)) {
            stringRedisTemplate.delete(key);
            deleteLock(key);
        }
    }


    public RedisDto findRedis(Integer id, String redisName) {
        String value = stringRedisTemplate.opsForValue().get(returnKey(id, redisName));
        JSONObject jsonObject = JSON.parseObject(value);
        RedisDto redisDto = new RedisDto(jsonObject.getString("id"), jsonObject.getString("account"),
                jsonObject.getString("passWord"), jsonObject.getString("gmt_creat"));
        return redisDto;
    }

    public boolean hasRedis(Integer id, String redisName) {
        return stringRedisTemplate.hasKey(returnKey(id, redisName));
    }

    public boolean addLock(String key) {
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(LOCK_KEY + key, LOCK_VALUE, LOCK_TIME_OUT, TimeUnit.SECONDS);
        return lock != null && lock;
    }

    public void deleteLock(String key) {
        stringRedisTemplate.delete(LOCK_KEY + key);
    }

}
