package com.wlznsb.iossupersign.aop;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @Description
 * @data 2022/2/25
 * @Author: LiuBin
 * @Modified By:
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    private final ObjectMapper mapper;


    @Autowired
    public WebLogAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Pointcut("execution(* com.wlznsb.iossupersign.controller..*.*(..))")
    public void requestLog() {
        System.out.println("打印");
    }


    //通知方法会在目标方法调用之前执行
    @Before("requestLog()")
    public void Before(JoinPoint joinPoint) throws Throwable {


    }

    //通知方法会在目标方法返回或异常后调用
    @After("requestLog()")
    public void after() {
       // System.out.println("After ...");
    }

    //通知方法会在目标方法返回后调用
    @AfterReturning("requestLog()")
    public void afterReturning() {
       // System.out.println("AfterReturning ...");

    }


    //通知方法会在目标方法抛出异常后调用
    @AfterThrowing("requestLog()")
    public void AfterThrowing() {
       // System.out.println("AfterThrowing ...");
    }

//    将函数标记为在切入点覆盖的方法之前执行的通知。
    @Around("requestLog()")
    public Object  around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取请求头
        Enumeration<String> enumeration = request.getHeaderNames();
        StringBuffer headers = new StringBuffer();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            headers.append(name).append("=").append(value).append(" || ");
        }

        // 打印请求相关参数
        log.info("========================================== 请求参数 ==========================================");
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args   : {}",  Arrays.toString(joinPoint.getArgs()));
        //打印请求头
        log.info("Request header   : {}", headers);

        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        obj = joinPoint.proceed(args);
        log.info("========================================== 返回参数 ==========================================");
        log.info(JSON.toJSONString(obj));

        // 获取执行的方法名
        long endTime = System.currentTimeMillis();


        long diffTime = endTime - startTime;
        log.info("========================================== 结束 " + "接口耗时：" + diffTime + " ms" + "==========================================");
        return obj;
    }




}
