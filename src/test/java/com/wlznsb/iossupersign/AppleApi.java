package com.wlznsb.iossupersign;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.GetIpaInfoUtil;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import net.odyssi.asc4j.util.TokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AppleApi {


    @Value("${thread}")
    private   Integer thread;

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

//
        System.out.println(ServerUtil.getUuid());
        AppleApiUtil appleApiUtil = new AppleApiUtil("da1a8314-e03a-4a39-afb3-d788e24dcc24","8R4XT9AFKL","C:\\Users\\xujimu\\Desktop\\123.p8");
        appleApiUtil.initTocken();

        System.out.println(appleApiUtil.queryDevice("c"));

//        String abc = appleApiUtil.queryProfiles();
//        int count =  new ObjectMapper().readTree(abc).get("meta").get("paging").get("total").asInt();
//        for (int i = 0; i < count; i++) {
//            String id = new ObjectMapper().readTree(abc).get("data").get(i).get("attributes").get("name").asText();
//            if(id.equals("3SFBDJAC38")){
//                System.out.println("是的");
//            }
//        }
       // int intIndex = abc.indexOf("02bc34e0ad3d9769de492164344b450917fc9d2");
        //System.out.println(intIndex);
//        String keyPath = new File("/sign/mode/my.key").getAbsolutePath();
//        String a = appleApiUtil.addUuid("02bc34e0ad3d9769de4292154344b450917fc9d2");
//        System.out.println("设备id" + a);
//        String b = appleApiUtil.addIdentifiers("com.qweqe.qweq", "qqweqwe");
//        System.out.println("包名id" + b);
//        System.out.println(appleApiUtil.addProfiles(b,certId,a,"1qwe123qw","C:\\Users\\xujimu\\Desktop"));
         // Map<String,Object>  a = GetIpaInfoUtil.readIPA("C:\\Users\\xujimu\\Desktop\\111.ipa", "C:\\Users\\xujimu\\Desktop\\123.png");
        //System.out.println(appleApiUtil.queryDevice("c019313fdb835a842f98941ce0c9bd8801ddeef7"));
       // System.out.println(appleApiUtil.deleProfiles("XKF4326P2L"));
         // System.out.println(a);
    }


    @Test
    public void  test5(){
         String a = "eyJkYXRhIjogeyJpZCI6IDMsIm5hbWUiOiAi5LiH6aG65aix5LmQIiwic2l6ZSI6ICIxME0iLCJpY29uIiA6ICJodHRwczovL3NpZ24ud2x6bnNiLmNuL2lvc2lnbi8xMTIzMS9kaXN0cmlidXRlLzMvMy5wbmcifX0=";
        System.out.println(new String(Base64.getDecoder().decode(a)));;
    }

    @Test
    public void  test6(){

    }

    @Test
    public void  test7(){

        System.out.println(thread);


    }


}
