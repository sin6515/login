package com.example.logindemo.dao;

import com.example.logindemo.entity.LoginRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hrh13
 * @date 2021/9/23
 */
@Repository
public interface LoginRecordDao extends JpaRepository<LoginRecordEntity, Integer> {
}
