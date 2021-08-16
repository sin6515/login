package com.example.logindemo.shiro;

import com.alibaba.fastjson.JSON;
import com.example.logindemo.dto.ReturnValue;
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
import static com.example.logindemo.dto.ErrorConstantValue.*;

/**
 * @author hrh13
 * @date 2021/8/16
 */
@Component
public class ShiroFilter extends AccessControlFilter {
    @Autowired
    private RedisService redisService;

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
        if (req.getHeader(HEADER_EMPLOYEE_ID) == null || req.getHeader(HEADER_TOKEN) == null) {
            try {
                response.getWriter().write(JSON.toJSONString(ReturnValue.fail(BAD_REQUEST_CODE, NO_HAVE_HEARER, HEADER_EMPLOYEE_ID + " OR " + HEADER_TOKEN)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        Integer employeeId = Integer.valueOf(req.getHeader(HEADER_EMPLOYEE_ID));
        String token = req.getHeader(HEADER_TOKEN);
        if (redisService.hasRedis(employeeId, EMPLOYEE)) {
            Integer foundId = redisService.findTokenId(token);
            if (employeeId.equals(foundId)) {
                //将token传入AuthenticationToken
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
            } else {
                try {
                    response.getWriter().write(JSON.toJSONString(ReturnValue.fail(BAD_REQUEST_CODE, ERROR_TOKEN, HEADER_TOKEN + " : " + token)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        try {
            response.getWriter().write(JSON.toJSONString(ReturnValue.fail(NO_LOGIN_CODE, NO_LOGIN_STATE, HEADER_EMPLOYEE_ID + " : " + employeeId)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
