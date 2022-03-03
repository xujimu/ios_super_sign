package com.wlznsb.iossupersign.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: xjm
 * @Date: 2021/12/06/15:18
 * @Description: 用户角色校验
 */

//注解信息会被添加到Java文档中
@Documented
//注解的生命周期，表示注解会被保留到什么阶段，可以选择编译阶段、类加载阶段，或运行阶段
@Retention(RetentionPolicy.RUNTIME)
//注解作用的位置，ElementType.METHOD表示该注解仅能作用于方法上
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface PxCheckAdmin {

    /**
     * 是否需要登录 默认为true 设置未false只在方法上有效 比如在类上设置为true
     * 类下方法都需要登录 此时在某个方法下设置为false 则这个方法任然不需要登录
     * @return
     */
    boolean value() default true;

}
