package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.DownCode;
import com.wlznsb.iossupersign.util.ServerUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class DownCodeDaoTest {

    @Autowired
    public DownCodeDao downCodeDao;

    @Test
    void addDownCode() {
        List<DownCode> downCodeList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            DownCode downCode = new DownCode(null, "123", ServerUtil.getUuid(), new Date(), null, 1);
            System.out.println(downCode);
            downCodeList.add(downCode);
        }
        System.out.println(downCodeList.size());
        downCodeDao.addDownCode(downCodeList);
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
