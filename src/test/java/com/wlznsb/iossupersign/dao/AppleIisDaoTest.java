package com.wlznsb.iossupersign.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.IoHandler;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Cipher;
import javax.naming.ldap.PagedResultsControl;
import java.io.File;
import java.io.IOException;
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
    void dele() throws IOException {
        String p12Path = "C:\\Users\\Administrator\\Desktop\\q.p12";
        String password = "1";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("p12",p12Path,
                        RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"),
                                new File(p12Path)))
                .addFormDataPart("password", password)
                .build();
        Request request = new Request.Builder()
                .url("https://check.signstack.cc:2052/checkcert")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();


        if(response.code() == 200){
            String res = response.body().string();
            JsonNode jsonNode = new ObjectMapper().readTree(res);
            System.out.println(jsonNode.get("data").get("name"));

            System.out.println(res);
        }else {


        }
    }

    @Test
    void updateStatus() throws IOException {

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
