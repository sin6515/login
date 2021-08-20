package com.example.logindemo.dao;

import com.example.logindemo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Repository
public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByRoleName(String roleName);

    Boolean existsByRoleName(String roleName);

    @Override
    Optional<RoleEntity> findById(Integer roleId);

    @Transactional
    @Modifying
    @Query("update RoleEntity d set d.roleName=?1 , d.gmtModified=?2  where d.id=?3")
    void updateRoleName(String roleName, long gmtModified, Integer roleId);
}
