package com.wlznsb.iossupersign;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AppleApi {


    /**
     * 测试证书是否正确
     *
     * @throws IOException
     */

    @Test
    public void test() throws IOException {

        AppleApiUtil appleApiUtil = new AppleApiUtil("70e2fe2f-cb06-49a2-9696-753ca9ca7a50","9W6Y7V67D3","C:\\Users\\xujimu\\Desktop\\AuthKey_9W6Y7V67D3.p8");
        System.out.println(appleApiUtil.init());

    }


    /**
     * 删除所有证书
     * @throws IOException
     */

    @Test
    public void test1() throws IOException {
        AppleApiUtil appleApiUtil = new AppleApiUtil("70e2fe2f-cb06-49a2-9696-753ca9ca7a50","9W6Y7V67D3","C:\\Users\\xujimu\\Desktop\\AuthKey_9W6Y7V67D3.p8");
        appleApiUtil.init();
        appleApiUtil.deleCertAll();
    }


    /**
     * 申请证书
     * @throws IOException
     */@Test
   public void test2() throws IOException {

        AppleApiUtil appleApiUtil = new AppleApiUtil("70e2fe2f-cb06-49a2-9696-753ca9ca7a50","9W6Y7V67D3","C:\\Users\\xujimu\\Desktop\\1111.p8");
        appleApiUtil.init();
        String abc = appleApiUtil.queryProfiles();
        int count =  new ObjectMapper().readTree(abc).get("meta").get("paging").get("total").asInt();
        for (int i = 0; i < count; i++) {
            String id = new ObjectMapper().readTree(abc).get("data").get(i).get("attributes").get("name").asText();
            if(id.equals("3SFBDJAC38")){
                System.out.println("是的");
            }
        }
       // int intIndex = abc.indexOf("02bc34e0ad3d9769de492164344b450917fc9d2");
        //System.out.println(intIndex);
//        String keyPath = new File("/sign/mode/my.key").getAbsolutePath();
//        String certId = appleApiUtil.createCert("C:\\Users\\xujimu\\Desktop\\kkk",keyPath,"123456").get("certId");
//        System.out.println("证书id" + certId);
//        String a = appleApiUtil.addUuid("02bc34e0ad3d9769de4292154344b450917fc9d2");
//        System.out.println("设备id" + a);
//        String b = appleApiUtil.addIdentifiers("com.qweqe.qweq", "qqweqwe");
//        System.out.println("包名id" + b);
//        System.out.println(appleApiUtil.addProfiles(b,certId,a,"1qwe123qw","C:\\Users\\xujimu\\Desktop"));
    }


}
