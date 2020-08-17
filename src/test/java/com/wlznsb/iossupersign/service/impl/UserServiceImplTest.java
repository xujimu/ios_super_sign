package com.wlznsb.iossupersign.service.impl;

import com.wlznsb.iossupersign.dto.UserDto;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void register() {
        System.out.println(userService);
        User user = new User(null, "1231231", "119999", new Date(), 0);
        System.out.println(userService.register(user).getMessage());;
    }

    @Test
    void login() {
        UserDto userDto =  userService.login("1231231", "11999");
        System.out.println(userDto.getMessage());
    }

    @Test
    void dele() {
        UserDto userDto =  userService.dele("1231231");
        System.out.println(userDto.getMessage());
    }

    @Test
    void updatePassword() {
        UserDto userDto =  userService.updatePassword("121","123");
        System.out.println(userDto.getMessage());
    }

    @Test
    void updateType() {
        UserDto userDto =  userService.updateType("1111231231211",2);
        System.out.println(userDto.getMessage());
    }
}
