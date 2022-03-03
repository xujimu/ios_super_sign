package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.EnterpriseSignCert;
import com.wlznsb.iossupersign.entity.IosSignSoftwareDistribute;
import com.wlznsb.iossupersign.util.MyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class EnterpriseSignCertDaoTest {

    @Autowired
    private EnterpriseSignCertDao enterpriseSignCertDao;

    @Autowired
    private IosSignSoftwareDistributeDao iosSignSoftwareDistributeDao;

    @Test
    void addaa() {
        IosSignSoftwareDistribute iosSignSoftwareDistribute = new IosSignSoftwareDistribute();
        iosSignSoftwareDistribute.setIosId(MyUtil.getUuid());
        iosSignSoftwareDistribute.setAccount("123");
        iosSignSoftwareDistribute.setAppName("蔡旭库");
        iosSignSoftwareDistribute.setPageName("123");
        iosSignSoftwareDistribute.setVersion("123");
        iosSignSoftwareDistribute.setIcon("123");
        iosSignSoftwareDistribute.setIpa("123");
        iosSignSoftwareDistribute.setApk("123");

        iosSignSoftwareDistribute.setUrl("123");
        iosSignSoftwareDistribute.setCreateTime(new Date());
        iosSignSoftwareDistribute.setIntroduce("123");

        iosSignSoftwareDistribute.setAutoPageName(1);
        iosSignSoftwareDistributeDao.add(iosSignSoftwareDistribute);
    }

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
