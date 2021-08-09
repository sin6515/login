package com.example.logindemo.dao;

import com.example.logindemo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Repository
public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByRoleName(String roleName);

    @Override
    Optional<RoleEntity> findById(Integer roleId);

}
