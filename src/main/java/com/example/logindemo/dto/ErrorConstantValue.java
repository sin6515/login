package com.example.logindemo.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hrh13
 * @date 2021/8/13
 */
public class ErrorConstantValue {
    public static final String ADD_EXISTS = "已存在！", LOGIN_ERROR_ACCOUNT = "该帐户不存在！", LOGIN_ERROR_PASSWORD = "密码错误！",
            NO_EXIST = "未找到对象，请确认是否存在！", NO_PERMISSION = "No Permission", HAVE_ROLE = "该角色已拥有！",
            BAD_REQUEST = "Bad Request", FORBIDDEN = "Forbidden", ERROR_INPUT = "Error Input", ERROR_INPUT_STATE = "权限名不合法！",
            NOT_FOUND = "Not Found", NO_LOGIN = "No Login", NO_LOGIN_STATE = "Please login to your account first",
            REPEAT_ASK = "Repeat Ask", REPEAT_ASK_STATE = "对象已存在！",NO_HAVE_HEARER="No Have Header",ERROR_TOKEN="Error Token";
    public static final Integer OK_CODE = 200, BAD_REQUEST_CODE = 400, FORBIDDEN_CODE = 403, NOT_FOUND_CODE = 404,
            NO_LOGIN_CODE = 411, REPEAT_ASK_CODE = 412, ERROR_INPUT_CODE = 413;
    public static final Map<Integer, String> ERROR_MAP;

    static {
        ERROR_MAP = new HashMap<>();
        ERROR_MAP.put(BAD_REQUEST_CODE, BAD_REQUEST);
        ERROR_MAP.put(FORBIDDEN_CODE, FORBIDDEN);
        ERROR_MAP.put(NOT_FOUND_CODE, NOT_FOUND);
        ERROR_MAP.put(NO_LOGIN_CODE, NO_LOGIN);
        ERROR_MAP.put(REPEAT_ASK_CODE, REPEAT_ASK);
        ERROR_MAP.put(ERROR_INPUT_CODE, ERROR_INPUT);
    }
}

