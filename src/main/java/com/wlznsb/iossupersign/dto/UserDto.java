package com.wlznsb.iossupersign.dto;

import com.wlznsb.iossupersign.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户service返回值
 *
 */
public class UserDto {
    private int code;
    private String message;
    private User user;
    private List<User> userList;

    public UserDto(int code, String message, List<User> userList) {
        this.code = code;
        this.message = message;
        this.userList = userList;
    }

    public UserDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public UserDto(int code, String message, User user) {
        this.code = code;
        this.message = message;
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
