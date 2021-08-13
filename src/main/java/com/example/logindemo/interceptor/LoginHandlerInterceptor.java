package com.example.logindemo.interceptor;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.service.RedisService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.dto.ErrorConstantValue.*;

/**
 * @author hrh13
 * @date 2021/8/5
 */
@Data
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Integer employeeId = Integer.valueOf(request.getHeader(HEADER_EMPLOYEE_ID));
        String token = request.getHeader(HEADER_TOKEN);
        if (redisService.hasRedis(employeeId, EMPLOYEE)) {
            if (redisService.verityToken(token, employeeId)) {
                return true;
            }
        }
        try {
            response.getWriter().write(JSON.toJSONString(ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
