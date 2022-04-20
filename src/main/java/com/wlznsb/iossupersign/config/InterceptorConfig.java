package com.wlznsb.iossupersign.config;

import com.wlznsb.iossupersign.filter.GlobalFilter;
import com.wlznsb.iossupersign.interceptor.UserAdminInterceptor;
import com.wlznsb.iossupersign.interceptor.UserLoginInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new GlobalFilter());
        registration.addUrlPatterns("*.apk");
        registration.setName("LogCostFilter");
        registration.setOrder(1);
        return registration;
    }



    /**
     * 这种方式可以在拦截器中使用Autowired
     * @return
     */
    @Bean
    public UserLoginInterceptor getUserLoginInterceptor(){
        return new UserLoginInterceptor();
    }


    @Bean
    public UserAdminInterceptor getUserAdminInterceptor(){
        return new UserAdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> pathList = new ArrayList<>();

        pathList.add("/**");


        //过滤所有未登录用户
        registry.addInterceptor(getUserLoginInterceptor()).addPathPatterns(pathList).excludePathPatterns("/admin/distribute/**");

        registry.addInterceptor(getUserAdminInterceptor()).addPathPatterns(pathList).excludePathPatterns("/admin/distribute/**");;

//        //已登录用户进行角色判断
//        registry.addInterceptor(UserRoleInterceptor()).addPathPatterns(pathList);
//        //已登录用户进行权限判断
//        registry.addInterceptor(getUserPermissionInterceptor()).addPathPatterns(pathList);

    }

}
