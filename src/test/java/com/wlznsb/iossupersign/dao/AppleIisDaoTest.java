package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.util.IoHandler;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Cipher;
import javax.naming.ldap.PagedResultsControl;
import java.io.File;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
class AppleIisDaoTest {

    @Autowired
    private AppleIisDao appleIisDao;
    @Autowired
    private UserDao userDao;


    @Test
    void add() {

        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(day));

//        System.out.println( appleIisDao.query("70e2fe2f-cb06-49a2-9696-753ca9ca7a50").toString());;
       // System.out.println(userDao.queryAccount("123").toString());
      //  AppleIis appleIis = new AppleIis(null,  "123", "123", "123","123","123","123",1, 1, 0, 100,new Date());
      //  appleIisDao.add(appleIis);
    }


    @Test
    void dele() throws MalformedURLException {
        String test = "https://www.baidu.com";
        java.net.URL  url = new  java.net.URL(test);
        System.out.println(url.getHost());
    }


    void updateStatus() {
        appleIisDao.updateStatus(0, "123");
    }


    void updateStart() {
        appleIisDao.updateStart(0, "123");
    }


    void queryAll() {
        System.out.println(appleIisDao.queryAll().get(0).getAccount());
    }


    void query() {

    }


    void updateIspublic(){
        System.out.println(appleIisDao.updateIspublic(1, "123"));
    }


    void queryAccount(){
        System.out.println(appleIisDao.queryAccount("123").get(0).getAccount());
    }


    @Test
    void quertUsIIs(){

    }
}
