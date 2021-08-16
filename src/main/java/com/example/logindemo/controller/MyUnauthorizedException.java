package com.example.logindemo.controller;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dto.ReturnValue;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

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
@Controller
public class MyUnauthorizedException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        if (e instanceof UnauthorizedException) {
            try {
                httpServletResponse.getWriter().write(JSON.toJSONString(ReturnValue.fail(FORBIDDEN_CODE, NO_PERMISSION, HEADER_EMPLOYEE_ID + " " + httpServletRequest.getHeader(HEADER_EMPLOYEE_ID))));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return new ModelAndView();
    }
}
