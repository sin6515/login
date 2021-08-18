package com.example.logindemo.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.logindemo.dto.ReturnValue;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

import static com.example.logindemo.dto.ErrorConstantValue.*;
import static com.example.logindemo.dto.ErrorConstantValue.NO_PERMISSION;

/**
 * @author hrh13
 * @date 2021/8/18
 */
@RestControllerAdvice
@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(JWTVerificationException.class)
    public ReturnValue<String> handleError(JWTVerificationException e) {
        return ReturnValue.fail(BAD_REQUEST_CODE, ERROR_TOKEN, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(UnauthorizedException e) {
        return ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(UnknownAccountException e) {
        return ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, e.getMessage());
    }
}
