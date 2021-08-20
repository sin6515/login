package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dto.EmployeeDto;
import com.example.logindemo.dto.RedisDto;
import com.example.logindemo.entity.EmployeeEntity;
import com.example.logindemo.view.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Collections;
import java.util.List;

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
    private RedisService redisService;
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;

    public EmployeeDto addEmployee(RegisterRequest request) {
        EmployeeEntity employeeEntity = new EmployeeEntity(request.getAccount(), DigestUtils.md5DigestAsHex(request.getPassWord().getBytes()),
                request.getNickname(), System.currentTimeMillis());
        employeeDao.save(employeeEntity);
        return new EmployeeDto(employeeEntity);

    }

    public EmployeeEntity findByEmployeeAccount(String account) {
        return employeeDao.findByAccount(account);
    }

    public EmployeeEntity findByEmployeeId(Integer employeeId) {
        if (employeeDao.findById(employeeId).isPresent()) {
            return employeeDao.findById(employeeId).get();
        }
        return null;
    }

    /**
     * description:通过id获取redis对应value，再获取permissionCode，对["permissionCode"]进行处理，去除首尾字符(为空则为“【】”)，去除字符的双引号
     * @author hrh
     * @date 2021/8/20
     */
    public List<String> findPermissionByEmployeeRedis(Integer employeeId) {
        JSONObject jsonObject = JSON.parseObject(redisService.findRedis(employeeId, EMPLOYEE));
        String json = jsonObject.getString(PERMISSION_CODE);
        if (json.length() == PERMISSION_CODE_FIRST_SIZE) {
            return null;
        }
        return Collections.singletonList(json.substring(1, json.length() - 1).replace("\"", ""));
    }

    public String findCategoryByEmployeeRedis(Integer employeeId) {
        JSONObject jsonObject = JSON.parseObject(redisService.findRedis(employeeId, EMPLOYEE));
        return jsonObject.getString("category");
    }

    public RedisDto updateEmployeeRedis(Integer employeeId) {
        RedisDto redisDto = new RedisDto(findByEmployeeId(employeeId), System.currentTimeMillis());
        redisDto.setToken(redisService.creatToken(redisDto.getId(), EMPLOYEE));
        String key = redisService.returnKey(employeeId, EMPLOYEE);
        redisDto.setCategory(findByEmployeeId(employeeId).getCategory());
        redisDto.setRoleId(employeeRoleService.findRoleIdByEmployeeId(redisDto.getId()));
        redisDto.setPermissionCode(rolePermissionService.findPermissionNameByRoleId(redisDto.getRoleId()));
        redisService.updateRedis(key, JSON.toJSONString(redisDto));
        return redisDto;
    }

    public void updateEmployeeRedis(List<Integer> employeeId) {
        for (Integer integer : employeeId) {
            updateEmployeeRedis(integer);
        }
    }

    public Boolean existsByAccount(String account) {
        return employeeDao.existsByAccount(account);
    }

    public Boolean existsByEmployeeId(Integer employeeId) {
        return employeeDao.existsById(employeeId);
    }
}
