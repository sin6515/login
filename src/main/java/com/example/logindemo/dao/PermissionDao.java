package com.example.logindemo.dao;

import com.example.logindemo.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author hrh13
 * @date 2021/7/22
 */
@Repository
public interface PermissionDao extends JpaRepository<PermissionEntity, Integer> {
    List<PermissionEntity> findByIdIn(List<Integer> permissionId);

    List<PermissionEntity> findByPermissionNameIn(List<String> permissionName);

    Boolean existsByPermissionNameIn(List<String> permissionName);

    @Override
    Optional<PermissionEntity> findById(Integer permissionId);

}