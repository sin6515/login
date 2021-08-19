package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dto.EmployeeDto;
import com.example.logindemo.entity.EmployeeEntity;
import com.example.logindemo.view.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author hrh13
 * @date 2021/7/23
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

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

    public Boolean existsByAccount(String account) {
        return employeeDao.existsByAccount(account);
    }
    public Boolean existsByEmployeeId(Integer employeeId){
        return employeeDao.existsById(employeeId);
    }
}
