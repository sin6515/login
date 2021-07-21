package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.LoginDTO;
import com.example.logindemo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Optional;

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
    public String addUser(UserEntity userEntity) {
        userEntity.setPassWord(DigestUtils.md5DigestAsHex(userEntity.getPassWord().getBytes()));
        userEntity.setGmt_Create();
        userDao.save(userEntity);
        return "注册成功！";
    }
    public String loginUser(LoginDTO loginDTO){
        loginDTO.setPassWord(DigestUtils.md5DigestAsHex(loginDTO.getPassWord().getBytes()));
        if (userDao.findByAccount(loginDTO.getAccount()).isEmpty()){
            return "用户不存在！";
        }
        else if(userDao.findByAccountAndPassWord(loginDTO.getAccount(),
                loginDTO.getPassWord()).isEmpty()) {
            return  "密码错误！";
        }
        else {
            String id= String.valueOf(userDao.findIdByAccount(loginDTO.getAccount()));
            id="login_user:"+id;
            loginDTO.setGmtCreate();
            String jsonStr = JSON.toJSONString(loginDTO);
            redisService.addRedis(id,jsonStr);
            return "登陆成功！";
        }
    }

    public Optional<UserEntity> findUser(Integer id ){
        return  userDao.findById(id);
    }

    public String deleteUser(Integer id){
        if(!userDao.findById(id).isEmpty()){
            userDao.deleteById(id);
            return "删除成功";
        }
        else {
            return "该用户不存在";
        }
    }
    public String updateUser(Integer id,String pd){
        if(!userDao.findById(id).isEmpty()){
            userDao.updatePassWordById(DigestUtils.md5DigestAsHex(pd.getBytes()),System.currentTimeMillis(),id);
            return "更新成功";
        }
        else {
            return "该用户不存在";
        }
    }
}
