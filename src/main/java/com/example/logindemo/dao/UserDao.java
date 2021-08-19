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
    /**
     * description:
     * @param account
     * @return com.example.logindemo.entity.UserEntity
     * @author hrh
     * @date 2021/8/19
     */
    UserEntity findByAccount(String account);

    /**
     * description:
     * @param id
     * @return void
     * @author hrh
     * @date 2021/8/19
     */
    @Override
    void deleteById(Integer id);

    Boolean existsByAccount(String account);
    @Transactional
    @Modifying
    @Query("update UserEntity d set d.passWord=?1 , d.gmtModified=?2  where d.id=?3")
    void updatePassWordById(String pd, long gmtModified, Integer userid);
}
