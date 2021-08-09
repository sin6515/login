package com.example.logindemo.service;

import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.UserDto;
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

    public UserDto addUser(AddDto addDto) {
        UserEntity userEntity = new UserEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                addDto.getNickname(), addDto.getEmail(), addDto.getPhone(), System.currentTimeMillis());
        userDao.save(userEntity);
        return new UserDto(userEntity);
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

    public UserDto findUser(Integer userId) {
        if (userDao.findById(userId).isPresent()) {
            return new UserDto(userDao.findById(userId).get());
        } else {
            return null;
        }
    }

    public UserDto deleteUser(Integer userId) {
        UserDto userDto = findUser(userId);
        userDao.deleteById(userId);
        return userDto;
    }

    public UserDto updateUser(Integer userId, String pd) {
        userDao.updatePassWordById(DigestUtils.md5DigestAsHex(pd.getBytes()), System.currentTimeMillis(), userId);
        return findUser(userId);
    }
}
