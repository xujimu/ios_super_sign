package com.wlznsb.iossupersign.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IosSignUdidCertDaoTest {

    @Autowired
    private IosSignUdidCertDao iosSignUdidCertDao;

    @Test
    void add() {
//        IosSignUdidCert iosSignUdidCert = new IosSignUdidCert();
//        iosSignUdidCert.setUdid("12312");
//        iosSignUdidCert.setAccount("12312");
//        iosSignUdidCert.setP12Password("12312");
//        iosSignUdidCert.setMobileprovisionPath("12312");
//        iosSignUdidCert.setP12Path("12312");
//        iosSignUdidCert.setIntroduce("12312");
//        iosSignUdidCert.setCreateTime(new Date());
//        iosSignUdidCert.setCertId(MyUtil.getUuid());
//        iosSignUdidCertDao.add(iosSignUdidCert)
//        ;
        iosSignUdidCertDao.updateIntroduce("蔡旭库","8fd2fba82745440f8e33cbf65ce45e29","admin");
        System.out.println( iosSignUdidCertDao.queryAccountAll("admin"));;
        iosSignUdidCertDao.delete("123213","admin");
    }
}
