package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dao.UserDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.entity.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserDao userDao;
    public void addEmployee(AddDto addDto) {
        EmployeeEntity employeeEntity = new EmployeeEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                addDto.getNickname(), System.currentTimeMillis());
        employeeDao.save(employeeEntity);
    }

    public String loginEmployee(LoginDto loginDTO) {
        loginDTO.setPassWord(DigestUtils.md5DigestAsHex(loginDTO.getPassWord().getBytes()));
        if (employeeDao.findByAccount(loginDTO.getAccount()).isEmpty()) {
            return "用户不存在！";
        } else if (employeeDao.findByAccountAndPassWord(loginDTO.getAccount(),
                loginDTO.getPassWord()).isEmpty()) {
            return "密码错误！";
        } else {
            return "succeed";
        }
    }

    public Object findUser(Integer employeeId,Integer userId) {
        String permissionName = "find";
        if (SUCCEED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            if (userDao.existsById(userId)){
                return userDao.findById(userId);
            }
            else{
                return "不存在用户"+userId;
            }
        } else if(FAILED.equals(permissionService.findIsPermission(permissionName, employeeId))){
            return "抱歉，您没有查询的权限！";
        }
        else {
            return "请先登录帐号！";
        }
    }

    public String deleteUser(Integer employeeId,Integer userId) {
        String permissionName = "delete";
        if (SUCCEED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            if (userDao.existsById(userId)) {
                userDao.deleteById(userId);
                return "succeed";
            } else {
                return "该用户不存在";
            }
        } else if(FAILED.equals(permissionService.findIsPermission(permissionName, employeeId))){
            return "抱歉，您没有注销的权限！";
        }
        else {
            return "请先登录帐号！";
        }
    }

    public String updateEmployee(Integer employeeId,Integer userId, String pd) {
        String permissionName = "update";
        if (SUCCEED.equals(permissionService.findIsPermission(permissionName, employeeId))) {
            if (userDao.existsById(userId)) {
                userDao.updatePassWordById(DigestUtils.md5DigestAsHex(pd.getBytes()), System.currentTimeMillis(), userId);
                return "更新成功";
            } else {
                return "该用户不存在";
            }
        } else if(FAILED.equals(permissionService.findIsPermission(permissionName, employeeId))){
            return "抱歉，您没有更改密码的权限！";
        }
        else {
            return "请先登录帐号！";
        }
    }
}
