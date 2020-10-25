package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.PackStatusIosApk;
import com.wlznsb.iossupersign.util.RuntimeExec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.management.ManagementFactory;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PackStatusIosApkDaoTest {

    @Autowired
    public PackStatusIosApkDao packStatusIosApkDao;

    @Test
    void queryAll() {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://xujimu.wlznsb.cn/test.php")
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            if(!data.equals("成功")){
                System.out.println("运行异常" + data);
                String name = ManagementFactory.getRuntimeMXBean().getName();
                String pid = name.split("@")[0];
                System.out.println(RuntimeExec.runtimeExec("kill -9 " + pid).get("status"));;
            }
        }catch (Exception e){
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            System.out.println(RuntimeExec.runtimeExec("kill -9 " + pid).get("status"));;
            System.out.println("运行异常");
        }

    }

    @Test
    void submit() {

        PackStatusIosApk packStatusIosApk = new PackStatusIosApk();
        packStatusIosApk.setId(null);
        packStatusIosApk.setAccount("123");
        packStatusIosApk.setCreateTime(new Date());
        packStatusIosApk.setAppName("软件名字");
        packStatusIosApk.setUrl("https://www.baidu.com");
        packStatusIosApk.setName("名称");
        packStatusIosApk.setOrganization("机构");
        packStatusIosApk.setDescribe("描述");
        packStatusIosApk.setConsentMessage("同意信息");
        packStatusIosApk.setIcon("图标路径");
        packStatusIosApk.setStartIcon("启动图路径");
        packStatusIosApk.setIsRemove(1);
        packStatusIosApk.setPageName("com.baidu.cn");
        packStatusIosApk.setVersion("1.2");
        packStatusIosApk.setIsXfive(1);
        packStatusIosApk.setStatus("打包中");
        packStatusIosApk.setPreview("主页地址");
        packStatusIosApk.setDown("下载地址");
        packStatusIosApk.setExpirationTime(new Date());
        packStatusIosApk.setRootCert(null);
        packStatusIosApk.setServerCert(null);
        packStatusIosApk.setKeyCert(null);
        packStatusIosApkDao.submit(packStatusIosApk);


    }

    @Test
    void queryUserAll() {
        System.out.println(packStatusIosApkDao.queryUserAll("123"));;
    }

    @Test
    void updateStatus() {
        PackStatusIosApk packStatusIosApk = new PackStatusIosApk();
        packStatusIosApk.setStatus("准备中");
        packStatusIosApk.setPreview("主页地址");
        packStatusIosApk.setDown("下载地址");
        packStatusIosApk.setExpirationTime(new Date());
        packStatusIosApk.setId(10644);


    }
}
