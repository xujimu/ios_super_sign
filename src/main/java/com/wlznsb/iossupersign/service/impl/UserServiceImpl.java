package com.wlznsb.iossupersign.service.impl;

import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.dto.UserDto;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public UserDto register(User user) {
        try {
            if(userDao.queryAccount(user.getAccount()) != null){
                throw new RuntimeException("用户已存在");
            }else {
                if(userDao.addAccount(user) == 1){
                    //创建个人目录
                    new File("/sign/temp/" + user.getAccount() + "/distribute").mkdirs();
                    return new UserDto(0, "注册成功", user);
                }else {
                    throw new RuntimeException("注册失败");
                }
            }
        }catch (Exception e){
            throw new RuntimeException("注册失败:" + e.getMessage().toString());
        }
    }

    @Transactional
    @Override
    public UserDto login(String account, String password) {
        try {
            User user = userDao.queryAccount(account);
            if(user == null){
                throw new RuntimeException("账号不存在");
            }else {
                if(user.getPassword().equals(password)){
                    return new UserDto(0, "登陆成功", user);
                }else {
                    throw new RuntimeException("密码错误");
                }
            }
        }catch (Exception e){
            throw new RuntimeException("登陆失败:" + e.getMessage().toString());
        }
    }

    @Transactional
    @Override
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
            throw new RuntimeException("删除失败:" + e.getMessage().toString());
        }
    }


    @Transactional
    @Override
    public UserDto updatePassword(String account,String password,String newPassword) {
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
            throw new RuntimeException("修改失败:" + e.getMessage().toString());
        }
    }

    @Override
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
            throw new RuntimeException("修改失败:" + e.getMessage().toString());
        }
    }


}
