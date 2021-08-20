package com.example.logindemo.dao;

import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Repository
public interface EmployeeRoleDao extends JpaRepository<EmployeeRoleEntity, Integer> {
    List<EmployeeRoleEntity> findByEmployeeId(Integer employeeId);

    List<EmployeeRoleEntity> findByRoleId(Integer roleId);

    Boolean existsByEmployeeIdAndRoleId(Integer employeeId, Integer roleId);

    @Transactional
    void deleteByRoleId(Integer roleId);

    @Transactional
    void deleteByEmployeeId(Integer employeeId);

    @Transactional
    void deleteByEmployeeIdAndRoleId(Integer employeeId, Integer roleId);

}
