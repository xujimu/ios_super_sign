package com.wlznsb.iossupersign.interceptor;

import com.wlznsb.iossupersign.annotation.PxCheckAdmin;
import com.wlznsb.iossupersign.annotation.PxCheckLogin;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.execption.ResRunException;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 用户登录拦截器
 *
 */
public class UserAdminInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private UserServiceImpl userService;

    //过滤未登录用户
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取类上的注解
            PxCheckAdmin pxCheckAdminClass =  handlerMethod.getMethod().getDeclaringClass().getAnnotation(PxCheckAdmin.class);
            // 获取方法上的注解
            PxCheckAdmin pxCheckAdmin = handlerMethod.getMethod().getAnnotation(PxCheckAdmin.class);
            //如果类上加了注解进行拦截
            if(null != pxCheckAdminClass){
                //如果这个方法有注解 且 设置了为不登录则通过 否则检验
                if(null != pxCheckAdmin && !pxCheckAdmin.value()){
                    return true;
                }else {
                    User user = userService.getUser(token);
                    if(user.getType() == 1){
                        return true;
                    }else {
                        throw new ResRunException(3,"只允许管理操作",null);
                    }
                }
            }else {
                //如果类上没注册则检查方法
                if(null != pxCheckAdmin && pxCheckAdmin.value()){
                    User user = userService.getUser(token);
                    if(user.getType() == 1){
                        return true;
                    }else {
                        throw new ResRunException(3,"只允许管理操作",null);
                    }
                }
            }
        }
        return true;

    }


}
