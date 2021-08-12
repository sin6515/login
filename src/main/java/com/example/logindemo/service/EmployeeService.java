package com.example.logindemo.service;

import com.example.logindemo.dao.EmployeeDao;
import com.example.logindemo.dto.AddDto;
import com.example.logindemo.dto.EmployeeDto;
import com.example.logindemo.dto.LoginDto;
import com.example.logindemo.entity.EmployeeEntity;
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

    public EmployeeDto addEmployee(AddDto addDto) {
        EmployeeEntity employeeEntity = new EmployeeEntity(addDto.getAccount(), DigestUtils.md5DigestAsHex(addDto.getPassWord().getBytes()),
                addDto.getNickname(), System.currentTimeMillis());
        employeeDao.save(employeeEntity);
        return new EmployeeDto(employeeEntity);

    }

    public LoginDto findLoginDtoByEmployeeAccount(String account) {
        if (findByEmployeeAccount(account) == null) {
            return null;
        } else {
            return new LoginDto(findByEmployeeAccount(account));
        }
    }

    public EmployeeEntity findByEmployeeAccount(String account) {
        if (employeeDao.findByAccount(account) == null) {
            return null;
        }
        return employeeDao.findByAccount(account);
    }

    public EmployeeEntity findByEmployeeId(Integer employeeId) {
        if (employeeDao.findById(employeeId).isEmpty()) {
            return null;
        }
        return employeeDao.findById(employeeId).get();
    }
}
