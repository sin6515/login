package com.example.logindemo.dao;

import com.example.logindemo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Repository
public interface EmployeeDao extends JpaRepository<EmployeeEntity, Integer> {
    EmployeeEntity findByAccount(String account);

    Boolean existsByAccount(String account);

    @Override
    void deleteById(Integer id);
}