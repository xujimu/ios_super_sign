package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.DownCode;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DownCodeDaoTest {

    @Autowired
    private DownCodeDao downCodeDao;

    @Test
    void addDownCode() {
        DownCode downCode = new DownCode(null, "123", "12321312", new Date(), null, 1);
        downCodeDao.addDownCode(downCode);
        System.out.println();
    }

    @Test
    void updateDownCodeStatus() {
        System.out.println(downCodeDao.updateDownCodeStatus("123","7e2abb7d99b14623aac5e83d44176ef4", new Date(), 0));;
    }

    @Test
    void deleDownCode() {
        downCodeDao.deleDownCode("123",1);
    }

    @Test
    void queryAccountAllDownCode() {
        System.out.println(downCodeDao.queryAccountAllDownCode("123"));;
    }
}
