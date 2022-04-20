package com.wlznsb.iossupersign.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PackStatusEnterpriseSignDaoTest {

    @Autowired
    private PackStatusEnterpriseSignDao packStatusEnterpriseSignDao;

    @Test
    void add() {
       // PackStatusEnterpriseSign packStatusEnterpriseSign = new PackStatusEnterpriseSign(null,12,"superdown.html","superdown.html",new Date(),"superdown.html","superdown.html","superdown.html","superdown.html",null);

        //packStatusEnterpriseSignDao.add(packStatusEnterpriseSign);

    }

    @Test
    void updateStatus() {

        //packStatusEnterpriseSignDao.updateStatus("排队中", 62);

    }

    @Test
    void queryAccount() {

        packStatusEnterpriseSignDao.queryAccount("123");

    }

    @Test
    void queryAll() {
        packStatusEnterpriseSignDao.queryAll();
    }
}
