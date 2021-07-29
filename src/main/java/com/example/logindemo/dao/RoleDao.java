package com.example.logindemo.dao;

import com.example.logindemo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Repository
public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    @Query("select id from RoleEntity where roleName=?1")
    Integer findIdByRoleName(String roleName);
    @Query("select roleName from RoleEntity where id=?1")
    Integer findRoleNameById(Integer roleId);
    boolean existsByRoleName(String roleName);
    @Transactional
    @Modifying
    @Query("update RoleEntity d set d.roleName=?1 , d.gmt_modified=?2 where d.id=?3")
    void updateRoleNameById(String name2, long modfied, Integer roleId);
}
