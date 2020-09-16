package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.PackStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class PackStatusDaoTest {

    @Autowired
    private PackStatusDao packStatusDao;

    @Test
    void query() {

    }

    @Test
    void add() {

    }

    @Test
    void updateStatus() {

        PackStatus packStatus = packStatusDao.query("953");
        packStatus.setId(null);


        packStatusDao.add(packStatus);
       // packStatus.setStatus("分好了");
        //packStatusDao.update(packStatus,"123");

    }

    @Test
    void update() {
        packStatusDao.updateStatus("打包了","123");
    }
}
