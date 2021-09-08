package com.example.logindemo.dao;

import com.example.logindemo.entity.OperationRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hrh13
 * @date 2021/9/8
 */
@Repository
public interface OperationRecordDao extends JpaRepository<OperationRecordEntity, Integer> {
}
