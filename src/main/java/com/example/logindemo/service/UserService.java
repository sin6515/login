package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.RedisDto;
import com.example.logindemo.dto.UserDto;
import com.example.logindemo.entity.UserEntity;
import com.example.logindemo.view.LoginRequest;
import com.example.logindemo.view.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/15
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;

    public UserDto addUser(RegisterRequest request) {
        UserEntity userEntity = new UserEntity(request.getAccount(), DigestUtils.md5DigestAsHex(request.getPassWord().getBytes()),
                request.getNickname(), request.getEmail(), request.getPhone(), System.currentTimeMillis());
        userDao.save(userEntity);
        return new UserDto(userEntity);
    }

    public RedisDto findByRedis(Integer userId) {
        JSONObject jsonObject = JSON.parseObject(redisService.findRedis(userId, USER));
        return new RedisDto(jsonObject.getString(ID), jsonObject.getString(ACCOUNT),
                jsonObject.getString(PASSWORD), jsonObject.getString(GMT_CREAT));
    }

    public UserEntity findByAccount(String account) {
        return userDao.findByAccount(account);
    }

    public UserDto findById(Integer userId) {
        if (userDao.findById(userId).isPresent()) {
            return new UserDto(userDao.findById(userId).get());
        }
        return null;
    }

    public Boolean existsById(Integer userId) {
        if (redisService.existsRedis(userId, USER)) {
            return true;
        }
        return userDao.existsById(userId);
    }

    public Boolean existsByAccount(String account) {
        return userDao.existsByAccount(account);
    }

    public void deleteUser(Integer userId) {
        if (redisService.addDateBaseLock(userId, USER)) {
            userDao.deleteById(userId);
            if (redisService.existsRedis(userId, USER)) {
                redisService.deleteRedis(userId, USER);
            }
            redisService.deleteDataLock(userId, USER);
        }
    }

    public RedisDto updateUserRedis(LoginRequest request) {
        RedisDto redisDto = new RedisDto(request.getAccount(),
                request.getPassWord(), System.currentTimeMillis());
        redisDto.setId(findByAccount(redisDto.getAccount()).getId());
        String key = redisService.returnKey(redisDto.getId(), USER);
        redisDto.setToken(redisService.creatToken(redisDto.getId(), USER));
        redisService.updateRedis(key, JSON.toJSONString(redisDto));
        return redisDto;
    }

    public UserDto updatePassword(Integer userId, String pd) {
        if (redisService.addDateBaseLock(userId, USER)) {
            if (redisService.existsRedis(userId, USER)) {
                LoginRequest request = new LoginRequest();
                request.setAccount(request.getAccount());
                request.setPassWord(DigestUtils.md5DigestAsHex(request.getPassWord().getBytes()));
                updateUserRedis(request);
            }
            userDao.updatePassWordById(DigestUtils.md5DigestAsHex(pd.getBytes()), System.currentTimeMillis(), userId);
            redisService.deleteDataLock(userId, USER);
        }
        return findById(userId);
    }
}
