package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dao.EmployeeRoleDao;
import com.example.logindemo.dao.RoleDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.entity.EmployeeRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.NULL_ROLE;

/**
 * @author hrh13
 * @date 2021/7/21
 */
@Service
public class EmployeeRoleService {
    @Autowired
    private EmployeeRoleDao employeeRoleDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleDao roleDao;
    public String registerEmployeeRole(AddDto addDto) {
        Integer employeeId = employeeDao.findIdByAccount(addDto.getAccount());
        EmployeeRoleEntity employeeRoleEntity = new EmployeeRoleEntity(employeeId,
                NULL_ROLE, System.currentTimeMillis());
        employeeRoleDao.save(employeeRoleEntity);
        return "注册成功！员工id为：" + employeeId;
    }

    public String addEmployeeRole(Integer employeeId, Integer roleId) {
        if(redisService.hasKey(employeeId)){
            if (roleDao.existsById(roleId)){
                if(employeeRoleDao.findByEmployeeIdAndRoleId(employeeId,roleId).isEmpty()){
                    EmployeeRoleEntity employeeRoleEntity = new EmployeeRoleEntity(employeeId,
                            roleId, System.currentTimeMillis());
                    employeeRoleDao.save(employeeRoleEntity);
                    return "为员工添加角色成功！";
                }
                else {
                    return "员工已拥有该角色，请勿重复赋予！";
                }
            }
            else {
                return "该角色不存在，请先创建！";
            }
        }
        else {
            return "请先登录帐号！";
        }
    }

    public String deleteEmployeeRole(Integer id) {
        employeeRoleDao.deleteByEmployeeId(id);
        return "删除成功！";
    }

}
