package com.example.logindemo.service;

import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.entity.UserEntity;
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
    private ReturnValueService returnValueService;
    @Autowired
    private RedisService redisService;

    public UserEntity addUser(AddDto addDto) {
        UserEntity userEntity = new UserEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                addDto.getNickname(), addDto.getEmail(), addDto.getPhone(), System.currentTimeMillis());
        userDao.save(userEntity);
        return userEntity;
    }

    public LoginDto findUser(String account) {

        if (userDao.findByAccount(account) == null) {
            return null;
        } else {
            LoginDto loginDto = new LoginDto(userDao.findByAccount(account).getAccount(),
                    userDao.findByAccount(account).getPassWord());
            return loginDto;
        }

    }

}
