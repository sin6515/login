package com.example.logindemo.dao;

import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Repository
public interface EmployeeRoleDao extends JpaRepository<EmployeeRoleEntity, Integer> {
    @Query(value = "select roleId from EmployeeRoleEntity where employeeId=?1")
    List<Integer> findRoleIdByEmployeeId(Integer employeeId);

    List<EmployeeRoleEntity> findByEmployeeId(Integer employId);

   EmployeeRoleEntity findByEmployeeIdAndRoleId(Integer employeeId, Integer roleId);

    @Transactional
    public void deleteByRoleId(Integer roleId);

    @Transactional
    public void deleteByEmployeeIdAndRoleId(Integer employeeId, Integer roleId);

    @Transactional
    @Modifying
    @Query("update EmployeeRoleEntity d set d.roleId=?1 , d.gmt_modified=?2 where d.employeeId=?3")
    Integer updateRoleIdByEmployeeId(Integer roleId, long modfied, Integer id);

    @Transactional
    @Modifying
    @Query("update EmployeeRoleEntity d set d.roleId=?1 , d.gmt_modified=?2 where d.employeeId=?3 and d.roleId=?4")
    Integer updateRoleId1ByRoleId2(Integer roleId2, long modfied, Integer employee, Integer roleId1);


}
