package com.wlznsb.iossupersign.interceptor;

import com.wlznsb.iossupersign.util.JsonCode;
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

    //过滤未登录用户
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("用户登录拦截器");
        System.out.println(request.getServletPath());
        if(null == request.getSession().getAttribute("user")){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("code", 4);
            map.put("message", "未登录");
            JsonCode.toJson(response,map);
            return false;
        }else {
            return true;
        }
    }
}
