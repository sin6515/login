package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Service
public class EmployeeRoleService {
    @Autowired
    private EmployeeRoleDao employeeRoleDao;

    public Boolean existByEmployeeIdAndRoleId(Integer employeeId, Integer roleId){
        return employeeRoleDao.existsByEmployeeIdAndRoleId(employeeId,roleId);
    }
    public Boolean existByEmployeeIdAndRoleId(Integer employeeId, List<Integer> roleId){
        for (Integer integer:roleId){
            if (!existByEmployeeIdAndRoleId(employeeId,integer)){
                return false;
            }
        }
        return true;
    }
    public List<Integer> findRoleIdByEmployeeId(Integer employeeId) {
        return employeeRoleDao.findByEmployeeId(employeeId).stream().map(EmployeeRoleEntity::getRoleId).collect(Collectors.toList());
    }

    public List<Integer> findEmployeeIdByRoleId(Integer roleId) {
        return employeeRoleDao.findByRoleId(roleId).stream().map(EmployeeRoleEntity::getEmployeeId).collect(Collectors.toList());
    }

    public void addEmployeeRole(Integer employeeId, Integer roleId) {
        EmployeeRoleEntity employeeRoleEntity = new EmployeeRoleEntity(employeeId,
                roleId, System.currentTimeMillis());
        employeeRoleDao.save(employeeRoleEntity);
    }
    public void addEmployeeRole(Integer employeeId, List<Integer> roleId) {
       for (Integer integer:roleId){
           addEmployeeRole(employeeId,integer);
       }
    }
    public void deleteEmployeeRole(Integer employeeId, Integer roleId) {
        employeeRoleDao.deleteByEmployeeIdAndRoleId(employeeId, roleId);
    }

    public void deleteByRoleId(Integer roleId) {
        employeeRoleDao.deleteByRoleId(roleId);
    }
    public void deleteByEmployeeId(Integer employeeId) {
        employeeRoleDao.deleteByEmployeeId(employeeId);
    }
}
