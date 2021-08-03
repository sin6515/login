package com.example.logindemo.dao;

import com.example.logindemo.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Repository
public interface PermissionDao extends JpaRepository<PermissionEntity, Integer> {
    List<PermissionEntity> findByPermissionNameAndId(String permissionName, Integer permissionId);

    List<PermissionEntity> findByPermissionName(String permissionName);

    @Query("select id from PermissionEntity where permissionName=?1")
    Integer findIdByPermissionName(String account);
    @Override
    Optional<PermissionEntity> findById(Integer permissionId);

}