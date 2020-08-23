package com.wlznsb.iossupersign.interceptor;

import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.util.JsonCode;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * AppleIisInterceptor
 *
 * 权限过滤
 */
public class AppleIisInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("apple api过滤器");
        System.out.println(request.getServletPath());
        //获取用户类型
        User user = (User) request.getSession().getAttribute("user");
        int type = user.getType();
        //获取路径
        String path = request.getServletPath();
        Map<String,Object> map = new HashMap<String, Object>();
        //过滤
        switch (path){
            case "/iis/queryAll":
            case "/iis/query":
                if(type == 3){
                    return true;
                }else {
                    map.put("code", 1);
                    map.put("message", "权限不足");
                    JsonCode.toJson(response,map);
                    return false;
                }
        }
        return false;
    }
}
