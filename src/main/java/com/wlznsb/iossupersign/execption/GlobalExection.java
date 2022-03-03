package com.wlznsb.iossupersign.execption;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExection {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String,Object> Exection(Exception e){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 3);
        map.put("message","操作失败,请联系管理员");
        e.printStackTrace();
        return map;
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Map<String,Object> RuntimeException(RuntimeException e){

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 3);
        System.out.println(e.toString() + "1");
        map.put("message", e.getMessage());
        e.printStackTrace();
        return map;
    }

    //其他返回状态码异常
    @ExceptionHandler(ResRunException.class)
    @ResponseBody
    public Map<String,Object> codeException(ResRunException e) {
        e.printStackTrace();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", e.getCode());
        System.out.println(e.toString() + "1");
        map.put("message", e.getMessage());
        map.put("data", e.getData());
        e.printStackTrace();
        return map;
    }
}
