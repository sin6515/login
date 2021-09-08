package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dao.OperationRecordDao;
import com.example.logindemo.entity.OperationRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.example.logindemo.dto.ConstantValue.HEADER_EMPLOYEE_ID;
import static com.example.logindemo.dto.ConstantValue.SUCCEED;

/**
 * @author hrh13
 * @date 2021/9/8
 */
@Service
@Slf4j
public class OperationRecordService {
    @Autowired
    private OperationRecordDao operationRecordDao;

    public void createRecord(JoinPoint joinPoint, Object returnValue) {
        Signature signature=joinPoint.getSignature();
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request= attributes.getRequest();
        String jsonString = JSON.toJSONString(joinPoint.getArgs());
        String args = jsonString.substring(1, jsonString.length() - 1);

        OperationRecordEntity recordEntity = new OperationRecordEntity();
        recordEntity.setOperator(request.getHeader(HEADER_EMPLOYEE_ID));
        recordEntity.setParameter(args);
        recordEntity.setUrl(request.getRequestURI());
        recordEntity.setPacketName(signature.getDeclaringTypeName());
        recordEntity.setFuncName(signature.getName());
        recordEntity.setGmtCreate(System.currentTimeMillis());
        if (returnValue != null) {
            recordEntity.setResult(SUCCEED);
        }
        operationRecordDao.save(recordEntity);
    }

}
