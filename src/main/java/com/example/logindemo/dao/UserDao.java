package com.example.logindemo.dao;

import com.example.logindemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author hrh13
 * @date 2021/7/12
 */
@Repository
public interface UserDao extends JpaRepository<UserEntity, Integer> {
    public UserEntity findByAccount(String account);

    @Override
    public void deleteById(Integer id);

    @Transactional
    @Modifying
    @Query("update UserEntity d set d.passWord=?1 , d.gmtModified=?2  where d.id=?3")
    Integer updatePassWordById(String pd, long modfied, Integer userid);
}
