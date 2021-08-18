package com.example.logindemo.dto;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Data
public class ConstantValue {
    public static final String EMPLOYEE = "employee", USER = "user", ROLE = "role", LOCK_KEY = "key", LOCK_VALUE = "value",
            USER_ID = "userId", ROLE_ID = "roleId", EMPLOYEE_ID = "employeeId",PERMISSION_NAME = "permissionName",
            ID = "id", ACCOUNT = "account", PASSWORD = "passWord", GMT_CREAT = "gmt_creat",
            HEADER_EMPLOYEE_ID = "EmployeeId", HEADER_TOKEN = "Token",
            REDIS_USER = "user:", REDIS_EMPLOYEE = "employee:", REDIS_ROLE = "role:",
            SECRET_KEY = "secret", ADMIN = "admin", NORMAL = "normal";

    public static final Integer FILTER_NUM = 1, TIME_OUT = 3, LOCK_TIME_OUT = 10, TIME_OUT_MILLS = 1000 * 60 * 60 * 24 * TIME_OUT;

}
