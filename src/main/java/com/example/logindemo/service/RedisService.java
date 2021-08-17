package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.RedisDto;
import com.example.logindemo.dto.RolePermissionRedisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private EmployeeService employeeService;
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

    public Integer findTokenId(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        return Integer.parseInt(String.valueOf(jwt.getClaim(EMPLOYEE)));
    }

    public RedisDto updateUserRedis(LoginDto loginDtO) {
        RedisDto redisDto = new RedisDto(loginDtO.getAccount(),
                loginDtO.getPassWord(), System.currentTimeMillis());
        redisDto.setId(userDao.findByAccount(redisDto.getAccount()).getId());
        key = returnKey(redisDto.getId(), USER);
        redisDto.setToken(creatToken(redisDto.getId(), USER));
        String jsonStr = JSON.toJSONString(redisDto);
        if (addLock(key)) {
            stringRedisTemplate.opsForValue().set(key, jsonStr);
            stringRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
            deleteLock(key);
        }
        return redisDto;
    }

    public RedisDto findUserRedis(Integer id) {
        String value = stringRedisTemplate.opsForValue().get(returnKey(id, USER));
        JSONObject jsonObject = JSON.parseObject(value);
        RedisDto redisDto = new RedisDto(jsonObject.getString(ID), jsonObject.getString(ACCOUNT),
                jsonObject.getString(PASSWORD), jsonObject.getString(GMT_CREAT));
        return redisDto;
    }

    public RedisDto updateEmployeeRedis(Integer employeeId) {
        RedisDto redisDto = new RedisDto(employeeService.findByEmployeeId(employeeId), System.currentTimeMillis());
        redisDto.setToken(creatToken(redisDto.getId(), EMPLOYEE));
        key = returnKey(employeeId, EMPLOYEE);
        redisDto.setCategory(employeeService.findByEmployeeId(employeeId).getCategory());
        redisDto.setRoleId(employeeRoleService.findRoleIdByEmployeeId(redisDto.getId()));
        redisDto.setPermissionCode(rolePermissionService.findPermissionNameByRoleId(redisDto.getRoleId()));
        String jsonStr = JSON.toJSONString(redisDto);
        if (addLock(key)) {
            stringRedisTemplate.opsForValue().set(key, jsonStr);
            stringRedisTemplate.expire(key, TIME_OUT, TimeUnit.DAYS);
            deleteLock(key);
        }
        return redisDto;
    }

    public void updateEmployeeRedis(List<Integer> employeeId) {
        for (Integer integer : employeeId) {
            updateEmployeeRedis(integer);
        }
    }

    public RolePermissionRedisDto updateRoleRedis(Integer roleId) {
        List<Integer> permissionIdList = rolePermissionService.findPermissionIdByRoleId(roleId);
        List<String> permissionNameList = permissionService.findPermissionNameById(permissionIdList);
        String roleName = roleService.findByRoleId(roleId).getRoleName();
        RolePermissionRedisDto redisDto = new RolePermissionRedisDto(roleId, roleName, permissionNameList);
        key = returnKey(roleId, ROLE);
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

    public String findRoleRedis(Integer roleIdBefore, Integer roleIdAfter) {
        String valueBefore = stringRedisTemplate.opsForValue().get(returnKey(roleIdBefore, ROLE));
        String valueAfter = stringRedisTemplate.opsForValue().get(returnKey(roleIdAfter, ROLE));
        return valueBefore + " " + valueAfter;
    }

    public List<String> findPermissionByEmployeeRedis(Integer employeeId) {
        String value = stringRedisTemplate.opsForValue().get(returnKey(employeeId, EMPLOYEE));
        JSONObject jsonObject = JSON.parseObject(value);
        return Collections.singletonList(jsonObject.getString("permissionName"));
    }

    public String findCategoryByEmployeeRedis(Integer employeeId) {
        String value = stringRedisTemplate.opsForValue().get(returnKey(employeeId, EMPLOYEE));
        JSONObject jsonObject = JSON.parseObject(value);
        return jsonObject.getString("category");
    }


    public void deleteRedis(Integer id, String redisName) {
        key = returnKey(id, redisName);
        if (addLock(key)) {
            stringRedisTemplate.delete(key);
            deleteLock(key);
        }
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
