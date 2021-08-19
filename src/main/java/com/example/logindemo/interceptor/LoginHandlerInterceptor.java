package com.example.logindemo.interceptor;

import com.example.logindemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hrh13
 * @date 2021/8/5
 */
@Order(-999)
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        if (request.getHeader(HEADER_EMPLOYEE_ID) == null || request.getHeader(HEADER_TOKEN) == null) {
//            try {
//                response.getWriter().write(JSON.toJSONString(ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, "NO HEARER")));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//        Integer employeeId = Integer.valueOf(request.getHeader(HEADER_EMPLOYEE_ID));
//        String token = request.getHeader(HEADER_TOKEN);
//        if (redisService.hasRedis(employeeId, EMPLOYEE)) {
//            if (employeeId.equals(redisService.findTokenId(token))) {
//                return true;
//            }
//        }
//        try {
//            response.getWriter().write(JSON.toJSONString(ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, employeeId)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
        return true;
    }
}
