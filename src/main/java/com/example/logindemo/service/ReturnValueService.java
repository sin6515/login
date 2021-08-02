package com.example.logindemo.service;

import com.example.logindemo.dto.ReturnDetailValue;
import com.example.logindemo.dto.ReturnValue;
import org.springframework.stereotype.Service;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/28
 */
@Service
public class ReturnValueService {
//    public ReturnValue succeedState(String message, ReturnDetailValue returnDetailValue) {
//        ReturnValue returnValue;
//        returnValue = new ReturnValue(SUCCEED,"d", OK_CODE, message, returnDetailValue);
//        return returnValue;
//    }
//
//    public ReturnValue failState(String object, String message, Object detail, Integer code) {
//        ReturnDetailValue returnDetailValue;
//        ReturnValue returnValue;
//        if (NO_LOGIN_CODE.equals(code)) {
//            returnDetailValue = new ReturnDetailValue((Integer) detail, NO_LOGIN_STATE);
//            returnValue = new ReturnValue(FAILED, NO_LOGIN, code, message, returnDetailValue);
//        } else if (FORBIDDEN_CODE.equals(code)) {
//            returnDetailValue = new ReturnDetailValue((Integer) detail, NO_PERMISSION);
//            returnValue = new ReturnValue(FAILED, FORBIDDEN, code, message, returnDetailValue);
//        } else if (NOT_FOUND_CODE.equals(code)) {
//            if (detail instanceof Integer) {
//                returnDetailValue = new ReturnDetailValue((Integer) detail, NO_EXIST);
//            } else {
//                returnDetailValue = new ReturnDetailValue(String.valueOf(detail), NO_EXIST);
//            }
//            returnValue = new ReturnValue(FAILED, NOT_FOUND, code, message, returnDetailValue);
//        } else if (ERROR_INPUT_CODE.equals(code)) {
//            returnDetailValue = new ReturnDetailValue((String) detail, ERROR_INPUT_STATE);
//            returnValue = new ReturnValue(FAILED, ERROR_INPUT, code, message, returnDetailValue);
//        } else if (REPEAT_ASK_CODE.equals(code)) {
//            if (EMPLOYEE_ROLE.equals(object)) {
//                returnDetailValue = new ReturnDetailValue((Integer) detail, HAVE_ROLE);
//            } else {
//                returnDetailValue = new ReturnDetailValue(String.valueOf(detail), REPEAT_ASK_STATE);
//            }
//            returnValue = new ReturnValue(FAILED, REPEAT_ASK, code, message, returnDetailValue);
//        } else {
//            returnDetailValue = new ReturnDetailValue(String.valueOf(detail), message);
//            returnValue = new ReturnValue(FAILED, BAD_REQUEST, code, LOGIN_FAILED, returnDetailValue);
//        }
//        return returnValue;
//
//    }

}