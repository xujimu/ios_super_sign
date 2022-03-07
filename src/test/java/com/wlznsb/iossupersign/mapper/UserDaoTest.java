package com.wlznsb.iossupersign.mapper;

import cn.hutool.core.util.ZipUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;


    void queryAccount() {
        System.out.println(userDao.queryAccount("12113"));
    }


    void queryAll() {
        System.out.println(userDao.queryAll().get(0).getAccount());
    }


    void updatePassword() {
        System.out.println(userDao.updatePassword("123", "78911"));
    }


    void updateType() {
        System.out.println(userDao.updateType("123",999));
    }


    void deleteAcount() {
        System.out.println(userDao);
    }


    @Test
    void addAccount() {
        ZipUtil.zip("C:\\Users\\Administrator\\Desktop\\4","C:\\Users\\Administrator\\Desktop\\123.ipa");

//        System.out.println(new File("C:\\sign\\mode\\my.key").getParent());
//        Integer id = 0;
//        System.out.println(-id);
//        User user = new User(null, "1111", "119999", new Date(), 0,0);
//        System.out.println(userDao.addAccount(user));
    }
}
