package com.wlznsb.iossupersign.filter;


import com.wlznsb.iossupersign.util.JsonCode;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GlobalFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if(request.getServletPath().indexOf("android") != -1){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        //设置apk5分钟有效
        if(request.getServletPath().indexOf("distribute") != -1){
            String token = request.getParameter("token");
            Long time = new Date().getTime();
            Long time1 = Long.parseLong(token);
            System.out.println(time - time1);
            if(time - time1 >= 0 &&  time - time1 < 3600000){
               filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }


}
