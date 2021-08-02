package com.example.logindemo.dao;

import com.example.logindemo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Repository
public interface EmployeeDao extends JpaRepository<EmployeeEntity, Integer> {
    public EmployeeEntity findByAccount(String account);

    public List<EmployeeEntity> findByAccountAndPassWord(String account, String pd);

    @Override
    public void deleteById(Integer id);

    @Transactional
    @Modifying
    @Query("update EmployeeEntity d set d.passWord=?1 , d.gmt_modified=?2  where d.id=?3")
    Integer updatePassWordById(String pd, long modfied, Integer id);

    @Query("select id from EmployeeEntity where account=?1")
    Integer findIdByAccount(String account);
}