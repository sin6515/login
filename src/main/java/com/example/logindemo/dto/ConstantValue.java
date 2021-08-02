package com.example.logindemo.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hrh13
 * @date 2021/7/26
 */
@Data
public class ConstantValue {
    public static final String ADD = "add", UPDATE = "update", Find = "find", DELETE = "delete",
            SUCCEED = "succeed", FAILED = "failed",
            PERMISSION = "permission", ROLE = "role", EMPLOYEE = "employee", USER = "user", EMPLOYEE_ROLE = "employRole",
            REDIS_USER = "login_user:", REDIS_EMPLOYEE = "login_employee:",
            ADD_SUCCEED = "添加成功！", UPDATE_SUCCEED = "更新成功！", FIND_SUCCEED = "查询成功！", DELETE_SUCCEED = "删除成功！",
            REGISTER_SUCCEED = "注册成功！", LOGIN_SUCCEED = "登录成功！",
            ADD_FAILED = "添加失败！", UPDATE_FAILED = "更新失败！", FIND_FAILED = "查询失败！", DELETE_FAILED = "删除失败！",
            REGISTER_FAILED = "注册失败！", LOGIN_FAILED = "登录失败！",
            ADD_EXISTS = "已存在！", LOGIN_ERROR_ACCOUNT = "该帐户不存在！", LOGIN_ERROR_PASSWORD = "密码错误！",
            NO_ROLE = "该角色不存在！", NO_EXIST = "未找到对象，请确认是否存在！", NO_PERMISSION = "未拥有该权限！", HAVE_ROLE = "该角色已拥有！",
            BAD_REQUEST = "Bad Request", FORBIDDEN = "Forbidden", ERROR_INPUT = "Error Input", ERROR_INPUT_STATE = "权限名不合法！",
            NOT_FOUND = "Not Found", NO_LOGIN = "No Login", NO_LOGIN_STATE = "帐号未登录！",
            REPEAT_ASK = "Repeat Ask", REPEAT_ASK_STATE = "对象已存在！";
    public static final Integer ZERO = 0, OK_CODE = 200, BAD_REQUEST_CODE = 400, FORBIDDEN_CODE = 403, NOT_FOUND_CODE = 404,
            NO_LOGIN_CODE = 411, REPEAT_ASK_CODE = 412, ERROR_INPUT_CODE = 413, TIME_OUT = 3;
    public static final Map<Integer, String> errorMap;

    static {
        errorMap = new HashMap<>();
        errorMap.put(BAD_REQUEST_CODE, BAD_REQUEST);
        errorMap.put(FORBIDDEN_CODE, FORBIDDEN);
        errorMap.put(NOT_FOUND_CODE, NOT_FOUND);
        errorMap.put(NO_LOGIN_CODE, NO_LOGIN);
        errorMap.put(REPEAT_ASK_CODE, REPEAT_ASK);
        errorMap.put(ERROR_INPUT_CODE, ERROR_INPUT);
    }

}
