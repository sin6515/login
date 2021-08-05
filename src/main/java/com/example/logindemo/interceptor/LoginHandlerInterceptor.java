package com.example.logindemo.interceptor;

import com.example.logindemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/8/5
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Integer employeeId= Integer.valueOf(request.getHeader(EMPLOYEE_ID));
        String token=request.getHeader(TOKEN);
        if(redisService.hasRedis(employeeId,EMPLOYEE)){
            if (token.equals(redisService.findToken(employeeId,EMPLOYEE))){
                return true;
            }
//            if (employeeId.equals(redisService.verityToken(token,EMPLOYEE,employeeId))){
//                return true;
//            }
        }
        return false;
    }

}
