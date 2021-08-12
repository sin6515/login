package com.example.logindemo.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.service.RedisService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static com.example.logindemo.dto.ConstantValue.*;

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
        Integer employeeId = Integer.valueOf(request.getHeader(EMPLOYEE_ID));
        String token = request.getHeader(TOKEN);
        if (redisService.hasRedis(employeeId, EMPLOYEE)) {
            if (redisService.verityToken(token, EMPLOYEE, employeeId)) {
                return true;
            }
        }
        PrintWriter out = null;
        JSONObject res = new JSONObject();
        res.put(ERROR_INPUT, ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId));
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert out != null;
        out.print(res);
        return false;
    }

}
