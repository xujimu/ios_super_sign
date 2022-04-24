package com.wlznsb.iossupersign.constant;


/**
 * redis键值存储方式
 */
public class RedisKey {

    //用户token %d用户uid 用户登录后键为uid 值为用户对象
    public static final String USER_TOKEN = "sign:user:token:%s";


    //推送任务 %s是序列化后的实体
    public static final String TASK_PUSH = "mdm:task:%s";


}
