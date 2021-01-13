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
 * 用户登录拦截器
 *
 */
public class UserAuthInterceptor extends HandlerInterceptorAdapter {

    //过滤未登录用户
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("用户权限拦截器");
        System.out.println(request.getServletPath());
        //获取用户类型
        User user = (User) request.getSession().getAttribute("user");
        int type = user.getType();
        //获取路径
        String path = request.getServletPath();
        Map<String,Object> map = new HashMap<String, Object>();

        //只有管理员允许访问这些接口
        if(type == 1){
            return true;
        }else {
            map.put("code", 1);
            map.put("message", "权限不足");
            JsonCode.toJson(response,map);
            return false;
        }

    }
}
