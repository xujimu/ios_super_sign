package com.wlznsb.iossupersign.interceptor;

import com.wlznsb.iossupersign.annotation.PxCheckLogin;
import com.wlznsb.iossupersign.constant.RedisKey;
import com.wlznsb.iossupersign.execption.ResRunException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //过滤未登录用户
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("用户登录拦截器");
        System.out.println(request.getServletPath());
        this.hasPermission(handler,request,response);
        return true;
    }
    //判断是否登录
    private boolean isLogin(HttpServletRequest request,HttpServletResponse response){
        String token =  request.getHeader("token");
        if(null != token){
            String useInfo = stringRedisTemplate.opsForValue().get(String.format(RedisKey.USER_TOKEN,token));
            if(null == useInfo || "".equals(useInfo)){
                throw new ResRunException(4,"账号在其他地方登录",null);
            }
            return true;
        }else {
            throw new ResRunException(4,"请登录",null);
        }
    }


    /**
     * 是否有权限
     */
    private boolean hasPermission(Object handler,HttpServletRequest request,HttpServletResponse response) {
        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取类上的注解
            PxCheckLogin pxCheckLoginClass =  handlerMethod.getMethod().getDeclaringClass().getAnnotation(PxCheckLogin.class);
            // 获取方法上的注解
            PxCheckLogin pxCheckLogin = handlerMethod.getMethod().getAnnotation(PxCheckLogin.class);
            //如果类上加了注解进行拦截
            if(null != pxCheckLoginClass){
                //如果这个方法有注解 且 设置了为不登录则通过 否则检验
                if(null != pxCheckLogin && !pxCheckLogin.value()){
                    return true;
                }else {
                    this.isLogin(request,response);
                }
            }else {
                //如果类上没注册则检查方法
                if(null != pxCheckLogin && pxCheckLogin.value()){
                    this.isLogin(request,response);
                }
            }
        }
        return true;
    }


}
