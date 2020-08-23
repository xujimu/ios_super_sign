package com.wlznsb.iossupersign.config;

import com.wlznsb.iossupersign.interceptor.AppleIisInterceptor;
import com.wlznsb.iossupersign.interceptor.UserAuthInterceptor;
import com.wlznsb.iossupersign.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
      //  registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns("/**");
        UserLoginInterceptor userLoginInterceptor = new UserLoginInterceptor();
        //添加用户登录拦截器
        registry.addInterceptor(userLoginInterceptor).excludePathPatterns("/user/login")
        .excludePathPatterns("/user/register").excludePathPatterns("/user/quit").
                excludePathPatterns("/distribute/**").excludePathPatterns("/**.mobileconfig").
                excludePathPatterns("/**.ipa").excludePathPatterns("/**.plist");
        //添加权限拦截器
        registry.addInterceptor(new UserAuthInterceptor()).addPathPatterns("/user/dele")
                .addPathPatterns("/user/updateType");
        //添加iis证书拦截器
        registry.addInterceptor(new AppleIisInterceptor()).addPathPatterns("/iis/queryAll").
                addPathPatterns("/iis/query");
    }

}
