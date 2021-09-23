package com.example.logindemo.shiro;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.logindemo.dto.ReturnValue;
import com.example.logindemo.error.ErrorController;
import com.example.logindemo.error.IllegalInputException;
import com.example.logindemo.service.RedisService;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.example.logindemo.dto.ConstantValue.*;
import static com.example.logindemo.error.ErrorConstantValue.*;

/**
 * @author hrh13
 * @date 2021/8/16
 */
@Component
public class ShiroFilter extends AccessControlFilter {
    @Autowired
    private RedisService redisService;
    @Autowired
    private ErrorController errorController;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        Set<String> passUrl = new HashSet<>();
        //配置不需要认证即可访问的地址，如登录接口
        passUrl.add("/users/login");
        passUrl.add("/users");
        passUrl.add("/employees");
        passUrl.add("/employees/login");
        //允许直接访问
        return passUrl.contains(uri);
        //不允许访问，执行onAccessDenied的拦截内容
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            if (req.getHeader(HEADER_EMPLOYEE_ID) == null || req.getHeader(HEADER_TOKEN) == null) {
                throw new IllegalInputException(NO_HAVE_HEARER);
            }
        } catch (IllegalInputException e) {
            try {
                response.getWriter().write(JSON.toJSONString(errorController.handleError(e)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return false;
        }
        Integer employeeId = Integer.valueOf(req.getHeader(HEADER_EMPLOYEE_ID));
        String token = req.getHeader(HEADER_TOKEN);
        if (redisService.existsRedis(employeeId, EMPLOYEE)) {
            try {
                if (employeeId.equals(redisService.findTokenId(token))) {
                    AuthenticationToken authenticationToken = new AuthenticationToken() {
                        @Override
                        public Object getPrincipal() {
                            return token;
                        }

                        @Override
                        public Object getCredentials() {
                            return token;
                        }
                    };
                    //委托给Realm进行认证
                    getSubject(request, response).login(authenticationToken);
                    return true;
                }
            } catch (JWTVerificationException e) {
                try {
                    response.getWriter().write(JSON.toJSONString(errorController.handleError(e)));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return false;
            }
        }
        try {
            response.getWriter().write(JSON.toJSONString(ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, HEADER_EMPLOYEE_ID + " : " + req.getHeader(HEADER_EMPLOYEE_ID))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
