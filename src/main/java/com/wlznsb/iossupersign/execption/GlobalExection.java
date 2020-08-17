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
        map.put("message", e.getMessage());
        return map;
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Map<String,Object> RuntimeException(RuntimeException e){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code", 3);
        map.put("message", e.getMessage());
        return map;
    }

}
