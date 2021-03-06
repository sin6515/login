package com.example.logindemo.error;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.logindemo.dto.ReturnValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.logindemo.error.ErrorConstantValue.*;

/**
 * @author hrh13
 * @date 2021/8/18
 */
@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class ErrorController {
    //    //todo 使用switch
//    @ExceptionHandler
//    public ReturnValue<String> handleError(Exception e) {
//        switch (getClass().toString()){
//            case "JWTVerificationException.class":
//                return ReturnValue.fail(BAD_REQUEST_CODE, ERROR_TOKEN, e.getMessage());
//        }
//        return ReturnValue.fail(BAD_REQUEST_CODE, "UnKnow Error", getClass().toString());
//    }

    @ExceptionHandler
    public ReturnValue<String> handleError(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(BAD_REQUEST_CODE, NOT_VALID, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(JWTVerificationException e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(BAD_REQUEST_CODE, ERROR_TOKEN, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(UnauthorizedException e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(NotFoundException e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(NOT_FOUND_CODE, NO_EXIST, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(RepeatAskException e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(REPEAT_ASK_CODE, REPEAT_ASK_STATE, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(IllegalInputException e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(ILLEGAL_INPUT_CODE, ILLEGAL_INPUT_STATE, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(PasswordErrorException e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(BAD_REQUEST_CODE, LOGIN_ERROR_PASSWORD, e.getMessage());
    }

    @ExceptionHandler
    public ReturnValue<String> handleError(Exception e) {
        log.warn(e.getMessage());
        return ReturnValue.fail(INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR_STATE, e.getMessage());
    }
}
