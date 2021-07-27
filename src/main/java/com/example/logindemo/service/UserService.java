package com.example.logindemo.service;

import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author hrh13
 * @date 2021/7/15
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public String addUser(AddDto addDto) {
        UserEntity userEntity = new UserEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                addDto.getNickname(), addDto.getEmail(), addDto.getPhone(), System.currentTimeMillis());
        userDao.save(userEntity);
        return "注册成功!";
    }

    public String loginUser(LoginDto loginDTO) {
        loginDTO.setPassWord(DigestUtils.md5DigestAsHex(loginDTO.getPassWord().getBytes()));
        if (userDao.findByAccount(loginDTO.getAccount()).isEmpty()) {
            return "用户不存在！";
        } else if (userDao.findByAccountAndPassWord(loginDTO.getAccount(),
                loginDTO.getPassWord()).isEmpty()) {
            return "密码错误！";
        } else {
            return "succeed";
        }
    }

}
