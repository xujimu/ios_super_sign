package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.IosSignSoftwareDistributeStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class IosSignSoftwareDistributeStatusDaoTest {

    @Autowired
    private IosSignSoftwareDistributeStatusDao iosSignSoftwareDistributeStatusDao;

    @Test
    void add() {
        IosSignSoftwareDistributeStatus ios = new IosSignSoftwareDistributeStatus("123","admin","123","123","123","123","123","123","123",new Date(),new Date());
        iosSignSoftwareDistributeStatusDao.add(ios);
    }

    @Test
    void updateStatus() {
        iosSignSoftwareDistributeStatusDao.updateStatus("蔡徐坤","123");
    }

    @Test
    void query() {
        IosSignSoftwareDistributeStatus s =    iosSignSoftwareDistributeStatusDao.query("123");
        System.out.println(s);
    }

    @Test
    void queryAccountAll() {
        List<IosSignSoftwareDistributeStatus> list =  iosSignSoftwareDistributeStatusDao.queryAccountAll("admin");
        System.out.println(list);
    }
}
