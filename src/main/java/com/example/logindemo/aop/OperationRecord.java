package com.example.logindemo.aop;

import com.example.logindemo.service.OperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hrh13
 * @date 2021/9/8
 */
@Aspect
@Component
@Slf4j
public class OperationRecord {
    @Autowired
    private OperationRecordService recordService;

    @Pointcut("execution(* com.example.logindemo.controller.EmployeeController.*(..))" +
            "||execution(* com.example.logindemo.controller.RoleController.*(..))")
    public void pointCut() {

    }

    @AfterReturning(value = "pointCut()", returning = "returnValue")
    public void doRecord(JoinPoint joinPoint, Object returnValue) {
        recordService.createRecord(joinPoint, returnValue);
    }

    @AfterThrowing(value = "pointCut()")
    public void doErrorRecord(JoinPoint joinPoint) {
        recordService.createRecord(joinPoint, null);
    }

}