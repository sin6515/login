package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dao.RolePermissionDao;
import com.example.logindemo.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private EmployeeRoleDao employeeRoleDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    public String addRole(String roleName) {
        RoleEntity roleEntity = new RoleEntity(roleName, System.currentTimeMillis());
        roleDao.save(roleEntity);
        return "添加角色成功！角色"+roleName+"的id为"+roleDao.findIdByRoleName(roleName);
    }

    public List<Integer> findRole(Integer employeeId) {
        return employeeRoleDao.findRoleIdByEmployeeId(employeeId);
    }

    public String updateRole(Integer employeeId, Integer roleId1, Integer roleId2) {
        if (redisService.hasKey(employeeId)) {
            if (!employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId1).isEmpty() &&
                    employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId2).isEmpty() &&
                    roleDao.existsById(roleId2)) {

                employeeRoleDao.updateRoleId1ByRoleId2(roleId1, System.currentTimeMillis(), roleId2);
                return "更改角色成功！";
            } else if (employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId1).isEmpty()) {
                return "该员工未拥有编号" + roleId1 + "的角色！";
            } else if (!employeeRoleDao.findByEmployeeIdAndRoleId(employeeId, roleId2).isEmpty()) {
                return "该员工已拥有编号" + roleId2 + "的角色！";
            } else  {
                return "角色"+roleId2 + "不存在！";
            }
        } else {
            return "请先登录帐号！";
        }

    }

    public String deleteRole(Integer roleId) {
        if (roleDao.existsById(roleId)) {
            roleDao.deleteById(roleId);
            employeeRoleDao.deleteByRoleId(roleId);
            rolePermissionDao.deleteByRoleId(roleId);
            return "删除角色成功！";
        } else {
            return "角色不存在！";
        }


    }
}
