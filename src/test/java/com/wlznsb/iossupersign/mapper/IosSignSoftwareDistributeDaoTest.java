package com.wlznsb.iossupersign.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class IosSignSoftwareDistributeDaoTest {

    @Autowired
    private IosSignSoftwareDistributeDao iosSignSoftwareDistributeDao;

    @Test
    void add() {
//        IosSignSoftwareDistribute iosSignSoftwareDistribute = new IosSignSoftwareDistribute();
//        iosSignSoftwareDistribute.setIosId(MyUtil.getUuid());
//        iosSignSoftwareDistribute.setCertId(MyUtil.getUuid());
//
//        iosSignSoftwareDistribute.setAccount("superdown.html");
//        iosSignSoftwareDistribute.setAppName("蔡旭库");
//        iosSignSoftwareDistribute.setPageName("superdown.html");
//        iosSignSoftwareDistribute.setVersion("superdown.html");
//        iosSignSoftwareDistribute.setIcon("superdown.html");
//        iosSignSoftwareDistribute.setIpa("superdown.html");
//        iosSignSoftwareDistribute.setApk("superdown.html");
//        iosSignSoftwareDistribute.setUrl("superdown.html");
//        iosSignSoftwareDistribute.setCreateTime(new Date());
//        iosSignSoftwareDistribute.setIntroduce("superdown.html");
//
//        iosSignSoftwareDistribute.setAutoPageName(1);
//        iosSignSoftwareDistributeDao.add(iosSignSoftwareDistribute);
        iosSignSoftwareDistributeDao.updateIntroduce("菜不开心","7a0a9507d44c49a9a67ec6a6179447b1","admin");
        System.out.println(iosSignSoftwareDistributeDao.queryAccountAll("admin"));;
        iosSignSoftwareDistributeDao.query("7a0a9507d44c49a9a67ec6a6179447b1");
        iosSignSoftwareDistributeDao.delete("7a0a9507d44c49a9a67ec6a6179447b1","admin");
    }
}
