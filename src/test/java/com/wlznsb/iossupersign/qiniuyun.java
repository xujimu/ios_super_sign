package com.wlznsb.iossupersign;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.GetIpaInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class qiniuyun {



    @Value("${qiniuyun.accessKey}")
    private String port;

    /**
     * 测试证书是否正确
     *
     * @throws IOException
     */

    @Test
    public void test() throws IOException {
        Long time = System.currentTimeMillis();

        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());
//...其他参数参考类注释
        cfg.useHttpsDomains = false;
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "EwSEYiKZtViB3YiqVQR8Y-Go4vijLhhY3WxIJxCz";
        String secretKey = "ToI-iR-Dhq-udEwIrZEhauqJCpbX6vrl-yk4ZXuh";
        String bucket = "abcqweqwedq";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\xujimu\\Desktop\\120011002.ipa";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "1231.ipa";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("本次上传耗费:" + (System.currentTimeMillis() - time)/1000 + "秒");
        } catch (Exception ex) {
                ex.printStackTrace();
        }

    }



    @Test
    public  void  test1(){

        System.out.println(port);
        if(port.equals("")){
            System.out.println("1");
        }else {
            System.out.println("2");
        }

    }


}
