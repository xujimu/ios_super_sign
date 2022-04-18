package com.wlznsb.iossupersign.service;

import com.alibaba.fastjson.JSON;
import com.qiniu.util.Json;
import com.wlznsb.iossupersign.constant.RedisKey;
import com.wlznsb.iossupersign.mapper.UserDao;
import com.wlznsb.iossupersign.dto.UserDto;
import com.wlznsb.iossupersign.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.File;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl {


    @Autowired
    private UserDao userDao;


    @Transactional
    public UserDto register(User user) {
        try {
            if(userDao.queryAccount(user.getAccount()) != null){
                throw new RuntimeException("用户已存在");
            }else {
                if(userDao.addAccount(user) == 1){
                    //创建个人目录
                    new File("./sign/temp/" + user.getAccount() + "/distribute").mkdirs();
                    return new UserDto(0, "注册成功", user);
                }else {
                    throw new RuntimeException("注册失败");
                }
            }
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("注册失败:" + e.getMessage());
        }
    }


    @Autowired
    private StringRedisTemplate redisTemplate;



    public User getUser(String token){
        String users = redisTemplate.opsForValue().get(String.format(RedisKey.USER_TOKEN, token));
        User user = JSON.parseObject(users,User.class);
        return user;
    }

    public User getUserInfo(String token){
        String users = redisTemplate.opsForValue().get(String.format(RedisKey.USER_TOKEN, token));
        User user = JSON.parseObject(users,User.class);
        User user1 = userDao.queryAccount(user.getAccount());
        user.setToken(token);

        redisTemplate.opsForValue().set(String.format(RedisKey.USER_TOKEN, token), JSON.toJSONString(user1));
        return user;
    }

    @Transactional
    public UserDto login(String account, String password) {
        try {
            User user = userDao.queryAccount(account);
            if(user == null){
                throw new RuntimeException("账号不存在");
            }else {
                if(user.getPassword().equals(password)){
                    String token = UUID.randomUUID().toString();
                    user.setToken(token);
                    redisTemplate.opsForValue().set(String.format(RedisKey.USER_TOKEN, token), JSON.toJSONString(user));

                    return new UserDto(0, "登陆成功", user);

                }else {
                    throw new RuntimeException("密码错误");
                }
            }
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("登陆失败:" + e.getMessage());
        }
    }

    @Transactional
    public UserDto dele(String account) {
        try {
            User user = userDao.queryAccount(account);
            if(user == null){
                throw new RuntimeException("账号不存在");
            }else {
                if(userDao.deleteAcount(account) == 1){
                    return new UserDto(0, "删除成功", user);
                }else {
                    throw new RuntimeException("删除失败");
                }
            }
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("删除失败:" + e.getMessage());
        }
    }


    @Transactional
    public UserDto updatePassword(String account,String password, String newPassword) {
        try {
            User user = userDao.queryAccount(account);
            if(user == null){
                throw new RuntimeException("账号不存在");
            }else {
                if(user.getPassword().equals(password)){
                    if(userDao.updatePassword(account,newPassword) == 1){
                        return new UserDto(0, "修改成功", user);
                    }else {
                        throw new RuntimeException("修改失败");
                    }
                }else {
                    throw new RuntimeException("密码错误");
                }
            }
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("修改失败:" + e.getMessage());
        }
    }

    @Transactional
    public UserDto updateType(String account, int type) {
        try {
            User user = userDao.queryAccount(account);
            if(user == null){
                throw new RuntimeException("账号不存在");
            }else {
                if(userDao.updateType(account, type) == 1){
                    return new UserDto(0, "修改成功", user);
                }else {
                    throw new RuntimeException("修改失败");
                }
            }
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("修改失败:" + e.getMessage());
        }
    }

}
