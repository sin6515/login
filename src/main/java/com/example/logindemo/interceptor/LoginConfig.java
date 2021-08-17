package com.example.logindemo.interceptor;

import com.example.logindemo.controller.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hrh13
 * @date 2021/8/5
 */
@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;
    @Autowired
    private LoginException loginException;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(loginHandlerInterceptor);
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/users/login", "/users", "/employees", "/employees/login");
    }
//    @Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//        resolvers.add(loginException);
//    }
}
