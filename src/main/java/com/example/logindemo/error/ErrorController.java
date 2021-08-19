package com.example.logindemo.error;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.logindemo.dto.ReturnValue;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.logindemo.dto.ErrorConstantValue.*;

/**
 * @author hrh13
 * @date 2021/8/18
 */
@RestControllerAdvice
@ControllerAdvice
public class ErrorController {
    @ExceptionHandler
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

    @ExceptionHandler
    public ReturnValue<String> handleError(NotFoundException e) {
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(RepeatAskException e) {
        return ReturnValue.fail(REPEAT_ASK_CODE, REPEAT_ASK_STATE, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(IllegalInputException e) {
        return ReturnValue.fail(ILLEGAL_INPUT_CODE, ILLEGAL_INPUT_STATE, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(PasswordErrorException e) {
        return ReturnValue.fail(BAD_REQUEST_CODE, LOGIN_ERROR_PASSWORD, e.getMessage());
    }
}
