package com.example.logindemo.dao;

import com.example.logindemo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Repository
public interface EmployeeDao extends JpaRepository<EmployeeEntity, Integer> {
    public EmployeeEntity findByAccount(String account);

    @Override
    public void deleteById(Integer id);

    @Query("select id from EmployeeEntity where account=?1")
    Integer findIdByAccount(String account);
}