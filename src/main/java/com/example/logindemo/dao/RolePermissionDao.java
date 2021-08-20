package com.example.logindemo.dao;

import com.example.logindemo.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Repository
public interface RolePermissionDao extends JpaRepository<RolePermissionEntity, Integer> {
    List<RolePermissionEntity> findByRoleId(Integer roleId);

    List<RolePermissionEntity> findByRoleIdIn(List<Integer> roleId);

    Boolean existsByRoleIdAndPermissionIdIn(Integer roleId, List<Integer> permissionId);

    @Transactional
    void deleteByRoleId(Integer roleId);

    @Transactional
    void deleteByRoleIdAndPermissionIdIn(Integer roleId, List<Integer> permissionId);
}
