package com.example.logindemo.dao;

import com.example.logindemo.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Repository
public interface RolePermissionDao extends JpaRepository<RolePermissionEntity, Integer> {
    @Query("select permissionId from RolePermissionEntity where roleId=?1")
    List<Integer> findPermissionIdByRoleId(Integer roleId);
    List<RolePermissionEntity> findByRoleIdAndPermissionId(Integer roleId,Integer permissionId);
    @Transactional
    void deleteByRoleIdAndPermissionId(Integer roleId, Integer permissionId);

    @Transactional
    void deleteByRoleId(Integer roleId);
    @Transactional
    @Modifying
    @Query("update RolePermissionEntity d set d.permissionId=?1 , d.gmt_modified=?2 where d.roleId=?3 and d.permissionId=?4")
    Integer updatePermissionId2ByRoleIdAndPermissionId1(Integer permissionId2, long modfied, Integer roleId,Integer permission1);
}
