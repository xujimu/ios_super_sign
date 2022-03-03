package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.PackStatus;
import com.wlznsb.iossupersign.util.ServerUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

        for (int i = 0; i < 1000; i++) {
            PackStatus packStatus = packStatusDao.query("18");
            packStatus.setId(null);
            packStatus.setUuid(ServerUtil.getUuid());
            packStatus.setStatus("排队中");
            packStatusDao.add(packStatus);
        }
       // packStatus.setStatus("分好了");
        //packStatusDao.update(packStatus,"123");

    }

    @Test
    void update() {
        packStatusDao.updateStatus("打包了","123");
    }
}
