package com.example.logindemo.dao;

import com.example.logindemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/12
 */
@Repository
public interface UserDao extends JpaRepository<UserEntity, Integer> {
    public List<UserEntity> findByAccount(String account);
    public List<UserEntity> findByAccountAndPassWord(String account,String pd);

    @Override
    public void deleteById(Integer id);

    @Query("select id from UserEntity where account=?1")
    Integer findIdByAccount(String account);

    @Transactional
    @Modifying
    @Query("update UserEntity d set d.passWord=?1 , d.gmt_modified=?2  where d.id=?3")
    Integer updatePassWordById(String pd, long modfied, Integer userid);
}
