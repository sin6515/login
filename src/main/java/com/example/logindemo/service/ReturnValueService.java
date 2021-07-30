package com.example.logindemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.entity.UserEntity;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/28
 */
@Service
public class ReturnValueService {
    public String succeedState(String message, ReturnDetailValue returnDetailValue ) {
        ReturnValue returnValue;
        returnValue = new ReturnValue(SUCCEED, OK_CODE, message, returnDetailValue);
        String jsonStr = JSON.toJSONString(returnValue, SerializerFeature.PrettyFormat);
        return jsonStr;
    }



    public String succeedFindState(UserEntity userEntity) {
        ReturnDetailValue returnDetailValue = new ReturnDetailValue(userEntity);
        ReturnValue returnValue = new ReturnValue(SUCCEED, OK_CODE, FIND_SUCCEED, returnDetailValue);
        String jsonStr = JSON.toJSONString(returnValue, SerializerFeature.PrettyFormat);
        return jsonStr;
    }

    public String failState(String object, String message, Object detail, Integer code) {
        ReturnDetailValue returnDetailValue;
        ReturnValue returnValue;
        String error;
        if (NO_LOGIN_CODE.equals(code)) {
            returnDetailValue = new ReturnDetailValue((Integer) detail, NO_LOGIN_STATE);
            returnValue = new ReturnValue(FAILED, NO_LOGIN, code, message, returnDetailValue);
        } else if (FORBIDDEN_CODE.equals(code)) {
            returnDetailValue = new ReturnDetailValue((Integer) detail, NO_PERMISSION);
            returnValue = new ReturnValue(FAILED, FORBIDDEN, code, message, returnDetailValue);
        } else if (NOT_FOUND_CODE.equals(code)) {
            if (PERMISSION.equals(object)) {
                returnDetailValue = new ReturnDetailValue(String.valueOf(detail), NO_ROLE);
            } else {
                returnDetailValue = new ReturnDetailValue(String.valueOf(detail), NO_EXIST);
            }
            returnValue = new ReturnValue(FAILED, NOT_FOUND, code, message, returnDetailValue);
        } else if (ERROR_INPUT_CODE.equals(code)) {
            returnDetailValue = new ReturnDetailValue((String) detail, ERROR_INPUT_STATE);
            returnValue = new ReturnValue(FAILED, ERROR_INPUT, code, message, returnDetailValue);
        } else if (message.equals(ADD_FAILED)) {
            if (PERMISSION.equals(object) && code.equals(REPEAT_ASK_CODE)) {
                returnDetailValue = new ReturnDetailValue(String.valueOf(detail), ADD_PERMISSION_ERROR);
                error = REPEAT_ASK;
            } else {
                returnDetailValue = new ReturnDetailValue(String.valueOf(detail), ADD_EXISTS);
                error = BAD_REQUEST;
            }
            returnValue = new ReturnValue(FAILED, error, code, message, returnDetailValue);
        } else if (object.equals(USER)) {
            returnDetailValue = new ReturnDetailValue(String.valueOf(detail), message);
            returnValue = new ReturnValue(FAILED, BAD_REQUEST, code, LOGIN_FAILED, returnDetailValue);
        } else if (message.equals(DELETE_FAILED)) {
            returnDetailValue = new ReturnDetailValue((Integer) detail, NO_ROLE);
            error = BAD_REQUEST;
            returnValue = new ReturnValue(FAILED, error, code, message, returnDetailValue);
        } else {
            if (PERMISSION.equals(object) && BAD_REQUEST_CODE.equals(code)) {
                returnDetailValue = new ReturnDetailValue((Integer) detail, NO_ROLE);
                error = BAD_REQUEST;
            } else {
                returnDetailValue = new ReturnDetailValue((Integer) detail, HAVE_ROLE);
                error = REPEAT_ASK;
            }
            returnValue = new ReturnValue(FAILED, error, code, message, returnDetailValue);
        }

        String jsonStr = JSON.toJSONString(returnValue, SerializerFeature.PrettyFormat);
        return jsonStr;

    }

}