package com.wlznsb.iossupersign.config;

import com.wlznsb.iossupersign.filter.GlobalFilter;
import com.wlznsb.iossupersign.interceptor.UserAuthInterceptor;
import com.wlznsb.iossupersign.interceptor.UserLoginInterceptor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Configuration
public class LoginConfig implements WebMvcConfigurer {


    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new GlobalFilter());
        registration.addUrlPatterns("*.apk");
        registration.setName("LogCostFilter");
        registration.setOrder(1);
        return registration;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
      //  registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns("/**");
        UserLoginInterceptor userLoginInterceptor = new UserLoginInterceptor();
        //添加用户登录拦截器
//        registry.addInterceptor(userLoginInterceptor).excludePathPatterns("/user/login")
//        .excludePathPatterns("/user/register").excludePathPatterns("/user/quit").
//                excludePathPatterns("/distribute/getUdid").excludePathPatterns("/distribute/getMobile").excludePathPatterns("/**.mobileconfig").
//                excludePathPatterns("/**.ipa").excludePathPatterns("/**.plist").excludePathPatterns("/**.html");

        //过滤所有未登录用户
        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/user/updatePassword").addPathPatterns("/user/queryDomain")
                .addPathPatterns("/user/queryDown").addPathPatterns("/admin/**").excludePathPatterns("/admin/distribute/**").addPathPatterns("/distribute/uploadIpa").addPathPatterns("/distribute/queryAccountAll")
                .addPathPatterns("/distribute/deleIpa").addPathPatterns("/distribute/updateDownCodeStatus").addPathPatterns("/distribute/updateBuyDownCodeUrl").addPathPatterns("/distribute/addDownCode").
                addPathPatterns("/distribute/queryAllDownCode").addPathPatterns("/distribute/downCert").addPathPatterns("/distribute/deleDownCode")
                .addPathPatterns("/distribute/uploadImg").addPathPatterns("/distribute/updateIntroduce").
                addPathPatterns("/iis/**").addPathPatterns("/distribute/uploadApk").addPathPatterns("/pack/**").excludePathPatterns("/pack/distribute/**").
                addPathPatterns("/EnterpriseSign/**").
                addPathPatterns("/softwareDistribute/**").excludePathPatterns("/softwareDistribute/down/**");

        //添加andmin拦截器
        registry.addInterceptor(new UserAuthInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin/distribute/**");


        //添加iis证书拦截器
        //registry.addInterceptor(new AppleIisInterceptor());
    }

}
