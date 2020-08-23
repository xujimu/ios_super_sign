package com.wlznsb.iossupersign.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//json处理工具
public class JsonCode {


    //转换json并输出
    public static void toJson(HttpServletResponse response, Map<String,Object> returnJson){
        ObjectMapper getJsonOBJ = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            String json = getJsonOBJ.writeValueAsString(returnJson);
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
