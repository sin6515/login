package com.example.logindemo.service;

import com.example.logindemo.dao.LoginRecordDao;
import com.example.logindemo.entity.LoginRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * @author hrh13
 * @date 2021/9/23
 */
@Service
public class LoginRecordService {
    @Autowired
    private LoginRecordDao loginRecordDao;

    @Bean
    public Consumer<LoginRecordEntity> loginRecord() {
        return entity -> {
            entity.setLoginTime(System.currentTimeMillis());

            loginRecordDao.save(entity);
        };
    }

}
