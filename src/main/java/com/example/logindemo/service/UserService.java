package com.example.logindemo.service;

import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.dto.ReturnDetailValue;
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

    public String addUser(AddDto addDto) {
        if (userDao.findByAccount(addDto.getAccount()).isEmpty()) {
            UserEntity userEntity = new UserEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                    addDto.getNickname(), addDto.getEmail(), addDto.getPhone(), System.currentTimeMillis());
            userDao.save(userEntity);
            Integer userId = userDao.findIdByAccount(addDto.getAccount());
            ReturnDetailValue returnDetailValue = new ReturnDetailValue(userId);
            return returnValueService.succeedState(REGISTER_SUCCEED, returnDetailValue);
        } else {
            return returnValueService.failState(USER, REGISTER_FAILED, addDto.getAccount(), REPEAT_ASK_CODE);
        }
    }

    public String loginUser(LoginDto loginDTO) {
        loginDTO.setPassWord(DigestUtils.md5DigestAsHex(loginDTO.getPassWord().getBytes()));
        if (userDao.findByAccount(loginDTO.getAccount()).isEmpty()) {
            return returnValueService.failState(USER, LOGIN_ERROR_ACCOUNT, loginDTO.getAccount(), BAD_REQUEST_CODE);
        } else if (userDao.findByAccountAndPassWord(loginDTO.getAccount(),
                loginDTO.getPassWord()).isEmpty()) {
            return returnValueService.failState(USER, LOGIN_ERROR_PASSWORD, loginDTO.getAccount(), BAD_REQUEST_CODE);
        } else {
            redisService.addRedis(loginDTO, USER);
            ReturnDetailValue returnDetailValue = new ReturnDetailValue(loginDTO.getAccount());
            return returnValueService.succeedState(LOGIN_SUCCEED, returnDetailValue);
        }
    }

}
