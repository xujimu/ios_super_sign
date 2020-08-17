package com.wlznsb.iossupersign.dao;

import com.sun.glass.ui.Application;
import com.wlznsb.iossupersign.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.sql.DataSourceDefinitions;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void queryAccount() {
        System.out.println(userDao.queryAccount("12113"));
    }

    @Test
    void queryAll() {
        System.out.println(userDao.queryAll().get(0).getAccount());
    }

    @Test
    void updatePassword() {
        System.out.println(userDao.updatePassword("123", "78911"));
    }

    @Test
    void updateType() {
        System.out.println(userDao.updateType("123",999));
    }

    @Test
    void deleteAcount() {
        System.out.println(userDao.deleteAcount("1111"));
    }

    @Test
    void addAccount() {
        User user = new User(null, "1111", "119999", new Date(), 0);
        System.out.println(userDao.addAccount(user));
    }
}
