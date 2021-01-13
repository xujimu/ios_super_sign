package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.EnterpriseSignCert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EnterpriseSignCertDaoTest {

    @Autowired
    private EnterpriseSignCertDao enterpriseSignCertDao;

    @Test
    void addCert() {

        EnterpriseSignCert enterpriseSignCert = new EnterpriseSignCert(12,"123","123","123","123","123","123",0,"123",new Date(),new Date(),"123");

        enterpriseSignCertDao.addCert(enterpriseSignCert);
    }

    @Test
    void deleteCert() {
        System.out.println(enterpriseSignCertDao.queryId(32));;
       // enterpriseSignCertDao.deleteCert("123", 12);
    }

    @Test
    void updateDownCodeStatus() {
     //   enterpriseSignCertDao.updateCertStatus("111", "123", 12);
    }

    @Test
    void queryAllCert() {
       //enterpriseSignCertDao.queryAllCert("123");
    }
}
