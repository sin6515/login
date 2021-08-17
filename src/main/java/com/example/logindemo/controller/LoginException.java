package com.example.logindemo.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.logindemo.dto.ReturnValue;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.logindemo.dto.ConstantValue.HEADER_EMPLOYEE_ID;
import static com.example.logindemo.dto.ErrorConstantValue.FORBIDDEN_CODE;
import static com.example.logindemo.dto.ErrorConstantValue.NO_PERMISSION;

/**
 * @author hrh13
 * @date 2021/8/16
 */
@Component
//@ControllerAdvice
@RestControllerAdvice
public class LoginException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        if (e instanceof UnauthorizedException) {
            try {
                httpServletResponse.getWriter().write(JSON.toJSONString(ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, HEADER_EMPLOYEE_ID + " : " + httpServletRequest.getHeader(HEADER_EMPLOYEE_ID))));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else if ( e instanceof JWTVerificationException) {
            try {
                httpServletResponse.getWriter().write(JSON.toJSONString(ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, HEADER_EMPLOYEE_ID + " : " + httpServletRequest.getHeader(HEADER_EMPLOYEE_ID))));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        } else if (e instanceof ServletException) {
            try {
                httpServletResponse.getWriter().write(JSON.toJSONString(ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, HEADER_EMPLOYEE_ID + " : " + httpServletRequest.getHeader(HEADER_EMPLOYEE_ID))));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return new ModelAndView();
    }
//    @ExceptionHandler(JWTDecodeException.class)
//    public ReturnValue<String> handle(JWTDecodeException jwtDecodeException) {
//        return ReturnValue.fail(BAD_REQUEST_CODE, ERROR_TOKEN, HEADER_TOKEN + " : ");
//    }
//
//    @ExceptionHandler(value = Exception.class)
//    public String exceptionHandler(Exception e) {
//        System.out.println("未知异常！原因是:" + e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public String exceptionHandler(RuntimeException e) {
//        System.out.println("未知异常！原因是:" + e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(ServletException.class)
//    public String exceptionHandler(ServletException e) {
//        System.out.println("未知异常！原因是:" + e);
//        return e.getMessage();
//    }
}
