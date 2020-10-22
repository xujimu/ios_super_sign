package com.wlznsb.iossupersign;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.GetIpaInfoUtil;
import com.wlznsb.iossupersign.util.IoHandler;
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

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
//            //p8路径
            String p8Path = new File("C:\\Users\\xujimu\\Desktop\\a5bfcfba-0592-4103-8bf4-26155249e76f.p8").getAbsolutePath();
//            log.info("p8路径:" + new File(p8Path).getAbsoluteFile());
//            //key路径
//            String keyPath = new File("C:\\Users\\xujimu\\Desktop\\123\\my.key").getAbsolutePath();
//            log.info("key路径:" + keyPath);
//            //创建证书目录
//            String certRoot = new File("C:\\Users\\xujimu\\Desktop\\123").getAbsolutePath();
//            new File(certRoot).mkdirs();
            //写入p8这里的new file必须是绝对路径抽象路径无效
            //创建苹果api工具类
            AppleApiUtil appleApiUtil = new AppleApiUtil("a5bfcfba-0592-4103-8bf4-26155249e76f", "2H85Q3Q5YW", p8Path);
            appleApiUtil.init();
//            //获取证书工作目录
//            String directoryPath = new File("C:\\Users\\xujimu\\Desktop\\123").getAbsolutePath();
//            log.info("证书工作目录:" + directoryPath);
//
//            //删除所有证书
//           // appleApiUtil.deleCertAll();
//            //生成p12
//            Map<String,String> map=  appleApiUtil.createCert(directoryPath,keyPath,"123456");
//            //随机bunild id
//            String buildId = ServerUtil.getUuid();
//           // String identifier = appleApiUtil.addIdentifiers(buildId,buildId);
//            String p12 = map.get("p12");
//            String certId = map.get("certId");

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
    public void  test6() throws IOException {

         String a= Base64.getEncoder().encodeToString(String.valueOf(1).getBytes());
         int c =Integer.valueOf(new String(Base64.getDecoder().decode(a.getBytes())));
        System.out.println(c);

    }

    @Test
    public void  test7(){



    }


}
