package com.example.logindemo.dto;

import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Data
public class ConstantValue {
    public static final String ADD = "add", UPDATE = "update", Find = "find", DELETE = "delete", SUCCEED = "succeed", FAILED = "failed", NOLOGIN = "noLogin";
    public static final Integer NULL_ROLE = 1, USER_ROLE = 2, ADMIN_ROLE = 3;
}
