package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dao.OperationRecordDao;
import com.example.logindemo.entity.OperationRecordEntity;
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
public class OperationRecordService {
    @Autowired
    private OperationRecordDao operationRecordDao;

    public void createRecord(JoinPoint joinPoint, Object returnValue) {
        // 获取签名
        Signature signature = joinPoint.getSignature();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String jsonString = JSON.toJSONString(joinPoint.getArgs());
        //传入参数
        String args = jsonString.substring(1, jsonString.length() - 1);
        // 获取切入的包名
        String declaringTypeName = signature.getDeclaringTypeName();
        // 获取即将执行的方法名
        String funcName = signature.getName();
        // 获取请求 URL
        String url = request.getRequestURL().toString();

        OperationRecordEntity recordEntity = new OperationRecordEntity();
        recordEntity.setOperator(request.getHeader(HEADER_EMPLOYEE_ID));
        recordEntity.setParameter(args);
        recordEntity.setUrl(url);
        recordEntity.setPacketName(declaringTypeName);
        recordEntity.setFuncName(funcName);
        recordEntity.setGmtCreate(System.currentTimeMillis());
        if (returnValue != null) {
            recordEntity.setResult(SUCCEED);
        }
        operationRecordDao.save(recordEntity);
    }

}
