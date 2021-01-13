package com.wlznsb.iossupersign.task;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.dao.*;
import com.wlznsb.iossupersign.entity.*;
import com.wlznsb.iossupersign.service.DistrbuteServiceImpl;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.IoHandler;
import com.wlznsb.iossupersign.util.MyUtil;
import com.wlznsb.iossupersign.util.RuntimeExec;
import jnr.posix.POSIXFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@Slf4j
public class PageStatusHandlerTask {

    @Autowired
    private DistributeDao distributeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DistrbuteServiceImpl distrbuteService;

    @Autowired
    private EnterpriseSignCertDao enterpriseSignCertDao;

    @Value("${signCount}")
    private Integer signCount;

    @Autowired
    private PackStatusDao packStatusDao;

    @Value("${domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String index;

    @Autowired
    private PackStatusIosApkDao packStatusIosApkDao;

    private ThreadPoolExecutor poolExecutor;

    @Autowired
    private PackStatusEnterpriseSignDao packStatusEnterpriseSignDao;

    //线程不安全非阻塞队列
    //private Queue<PackStatus> queue = new LinkedList<>();
    // 线程安全阻塞队列
   // private LinkedBlockingDeque<PackStatus> queue= new LinkedBlockingDeque<PackStatus>(100);
    //开启线程处理
    public PageStatusHandlerTask(@Value("${thread}") Integer thread) {
                                                            //临时线程数只有在有界队列才有用
        poolExecutor =  new ThreadPoolExecutor(thread, thread, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void run(){
        List<PackStatus> distributeList = packStatusDao.queryPage("排队中");
       for (PackStatus packStatus:distributeList){
           packStatusDao.updateStatus("准备中", packStatus.getUuid());
           poolExecutor.execute(new Runnable() {
               @Override
               public void run() {
                   log.info(Thread.currentThread().getName());
                   distrbuteService.getUuid(packStatus);
               }
           });
       }
    }

    /**
     * 企业签名
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void enterpriseSign(){
        try {
            List<PackStatusEnterpriseSign> list = packStatusEnterpriseSignDao.queryAll();
            for (PackStatusEnterpriseSign packStatusEnterpriseSign:list){
                if("排队中".equals(packStatusEnterpriseSign.getStatus())){
                    EnterpriseSignCert enterpriseSignCert = enterpriseSignCertDao.queryId(packStatusEnterpriseSign.getCertId());
                    if(enterpriseSignCert == null){
                        packStatusEnterpriseSignDao.updateStatus("签名失败,证书不存在", null, packStatusEnterpriseSign.getId());
                        throw  new RuntimeException("签名失败,证书不存在");
                    }
                    String uuid = MyUtil.getUuid();
                    String signPath = "/sign/mode/temp/sign" + uuid + ".ipa";
                    String cmd = "/sign/mode/zsign -k " + enterpriseSignCert.getCertPath() + " -p " + enterpriseSignCert.getPassword() + " -m " + enterpriseSignCert.getMoblicPath() + " -o " + signPath + " -z 1 " + packStatusEnterpriseSign.getIpaPath();
                    log.info("开始签名" + cmd);
                    packStatusEnterpriseSignDao.updateStatus("签名中", null, packStatusEnterpriseSign.getId());
                    Map<String,Object>  map =  RuntimeExec.runtimeExec(cmd);
                    log.info("签名结果" + map.get("status").toString());
                    log.info("签名反馈" + map.get("info").toString());
                    log.info("签名命令" + cmd);
                    if(!map.get("status").toString().equals("0")){
                        packStatusEnterpriseSignDao.updateStatus("签名失败", null, packStatusEnterpriseSign.getId());
                        throw  new RuntimeException("签名失败");
                    }else {
                        packStatusEnterpriseSignDao.updateStatus("签名成功", packStatusEnterpriseSign.getUrl() + "sign" + uuid + ".ipa", packStatusEnterpriseSign.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 每天企业签名检测是否掉签
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkCert() throws IOException {
        List<EnterpriseSignCert> enterpriseSignCertList = enterpriseSignCertDao.queryAllCert();
        for (EnterpriseSignCert enterpriseSignCert : enterpriseSignCertList){
            if("正常".equals(enterpriseSignCert.getStatus())){

                //检查证书
                String data = AppleApiUtil.certVerify(enterpriseSignCert.getCertPath(), enterpriseSignCert.getPassword());
                if(data.contains("revoke")){
                    log.info(enterpriseSignCert + "掉签");
                    enterpriseSignCertDao.updateCertStatus("掉签", enterpriseSignCert.getMd5());
                }
            }
        }
    }



    @Scheduled(cron = "0/5 * * * * *")
    public void iosApkPack(){
        //获取mode路径
        String modePath = new File("/sign/mode/static/mode").getAbsolutePath();
        //获取temp路径
        String tempPath = new File("/sign/mode/static/temp").getAbsolutePath();
        //备份当前目录
        String initPath = RuntimeExec.runtimeExec("pwd").get("info").toString();

        try {
            List<PackStatusIosApk> list = packStatusIosApkDao.queryAll();
            for (PackStatusIosApk packStatusIosApk:list){
                //每次执行都切回初始目录
                POSIXFactory.getPOSIX().chdir(initPath);
                if("排队中".equals(packStatusIosApk.getStatus())){
                    User user = userDao.queryAccount(packStatusIosApk.getAccount());
                    //如果是普通会员就扣公有池
                    if(user.getType() == 0){
                        try {
                            userDao.addCount(packStatusIosApk.getAccount(),-this.signCount);
                        }catch (Exception e){
                            packStatusIosApkDao.updateStatus("公有池不足!","","", new Date(),packStatusIosApk.getId());
                            continue;
                        }
                    }
                    //System.out.println( status.getStatusStatus());;
                    // System.out.println("cp -rf " + modePath + " " + tempPath + "/" + status.getStatusId());
                    //获取数字根目录
                    String idPath = new File("/sign/mode/static/temp/" + packStatusIosApk.getId()).getAbsolutePath();
                    //拷贝mode目录到数字目录
                    log.info("拷贝命令:" + "cp -rf " + modePath + " " + tempPath + packStatusIosApk.getId());
                    RuntimeExec.runtimeExec("cp -rf " + modePath + " " + tempPath + "/" + packStatusIosApk.getId());
                   // log.info("给予权限命令:" + "chmod -R 777 " + tempPath +  "/" + packStatusIosApk.getId());
                   // RuntimeExec.runtimeExec("chmod -R 777 " + tempPath + "/" + packStatusIosApk.getId());

                    //获取为签名描述文件路径,描述文件名和iconpath名是一样的所以提取出来
                    String mobileConfig = packStatusIosApk.getRemark();
                    //签名后的描述文件输出路径
                    String mobileConfigSign =  idPath +  "/" + "ios" + "/" + "sign.mobileconfig";
                    //三个证书的位置,如果没传就用默认的
                    String serverCrt = new File("/sign/mode/cert/server.crt").getAbsolutePath();
                    String rootCrt =  new File("/sign/mode/cert/root.crt").getAbsolutePath();
                    String keyCrt =  new File("/sign/mode/cert/key.key").getAbsolutePath();
                    if(packStatusIosApk.getKeyCert() != null){
                        serverCrt =  packStatusIosApk.getServerCert();
                        rootCrt =  packStatusIosApk.getRootCert();
                        keyCrt =  packStatusIosApk.getKeyCert();
                    }
                    //签名cmd
                    String cmd =" openssl smime -sign -in " + mobileConfig + " -out " + mobileConfigSign + " -signer " + serverCrt + " -inkey " + keyCrt + " -certfile " + rootCrt + " -outform der -nodetach ";
                    //执行签名,如果出错就结束本次循环
                    log.info("执行命令" + cmd);
                    if(!("0".equals(RuntimeExec.runtimeExec(cmd).get("status").toString()))){
                        packStatusIosApkDao.updateStatus("签名失败证书出错","","", new Date(),packStatusIosApk.getId());
                        RuntimeExec.runtimeExec("rm -rf " + idPath);
                        continue;
                    }else {
                        //获取安卓文件路径
                        String appName = tempPath + "/" + packStatusIosApk.getId()
                                + "/" + "android" + "/" + "temp" + "/"
                                + "app" + "/" + "src" + "/" + "main" + "/"
                                + "AndroidManifest.xml";
                        String uuid = tempPath + "/" + packStatusIosApk.getId()
                                + "/" + "android" + "/" + "temp" + "/" + "app"
                                + "/" + "build.gradle";
                        String url = tempPath + "/" + packStatusIosApk.getId()
                                + "/" + "android" + "/" + "temp" + "/"
                                + "app" + "/" + "src" + "/" + "main" + "/"
                                + "java" + "/" + "com" + "/" + "example" + "/"
                                + "myapplication" + "/" + "Home.java";
                        //获取文件文本
                        String appNameText = IoHandler.readTxt(appName);
                        String uuidText = IoHandler.readTxt(uuid);
                        String urlText = IoHandler.readTxt(url);
                        //替换文件文本
                        appNameText = appNameText.replace("应用名称", packStatusIosApk.getAppName());
                        uuidText = uuidText.replace("com.example.uuid", packStatusIosApk.getPageName()).replace("9.9", packStatusIosApk.getVersion());

                        //判断是不是动态网页
                        if (packStatusIosApk.getIsVariable() == 0){
                            log.info("非动态网页");
                            urlText = urlText.replace("http://www.wlznsb.cn/html", packStatusIosApk.getUrl());
                        }else {
                            log.info("动态网页");
                            urlText = urlText.replace("http://www.wlznsb.cn/html", domain + "pack/distribute/" + packStatusIosApk.getId());
                        }


                        //写入文件文本
                        IoHandler.writeTxt(appName,appNameText);
                        IoHandler.writeTxt(uuid,uuidText);
                        IoHandler.writeTxt(url,urlText);
                        //设置androidlogo
                        log.info("移动logo");
                        RuntimeExec.runtimeExec("mv " + packStatusIosApk.getIcon() + " "
                                + tempPath + "/" + packStatusIosApk.getId() + "/" + "android" + "/" + "temp" + "/"
                                + "app" + "/" + "src" + "/" + "main" + "/" + "res" + "/"
                                + "mipmap-xxxhdpi" + "/" + "icon.png");
                        if(packStatusIosApk.getStartIcon() != null){
                            RuntimeExec.runtimeExec("mv " + packStatusIosApk.getStartIcon() + " "
                                    + tempPath + "/" + packStatusIosApk.getId() + "/" + "android" + "/" + "temp" + "/"
                                    + "app" + "/" + "src" + "/" + "main" + "/" + "res" + "/"
                                    + "mipmap-xxxhdpi" + "/" + "start.png");
                        }
                        //安卓打包处理
                        //获取安卓目录
                        String androidPath = tempPath + "/" + packStatusIosApk.getId() + "/" + "android";

                        //切换到执行安卓打包的目录
                        POSIXFactory.getPOSIX().chdir(tempPath + "/" + packStatusIosApk.getId() + "/" + "android" + "/" + "temp" + "/");
                        //执行打包命令
                        if("0".equals(RuntimeExec.runtimeExec("sh shell.sh").get("status").toString())){
                            //移动打包后的apk到andorid目录
                            RuntimeExec.runtimeExec("mv " + androidPath + "/" + "temp" + "/"
                                    + "app" + "/" + "build" + "/" + "outputs" + "/" + "apk" + "/" +
                                    "debug" + "/" + "app-debug.apk " + androidPath + "/" +  "app.apk");
                            //把logn移动到html里面的logo路径
                            RuntimeExec.runtimeExec("mv " + androidPath + "/" + "temp" + "/"
                                    + "app" + "/" + "src" + "/" + "main" + "/" + "res" + "/"
                                    + "mipmap-xxxhdpi" + "/" + "icon.png " + idPath + "/" +  "html"
                                    + "/" + "static" + "/" + "picture" + "/" + "logo.png");

                            //获取html并修改成对应的应用名
                            String indexName = idPath + "/" + "html" + "/" + "index.html";
                            String indexText = IoHandler.readTxt(indexName);
                            IoHandler.writeTxt(indexName, indexText.replace("应用名称", packStatusIosApk.getAppName()));
                            //删除temp目录
                            RuntimeExec.runtimeExec("rm -rf " + androidPath + "/" + "temp");
                            //清理ios多余目录
                            RuntimeExec.runtimeExec("rm -rf " + idPath + "/" + "ios" + "/" + "cert " +  idPath + "/" + "ios" + "/" + "demo.txt");
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); //设置时间
                            c.add(Calendar.DATE, 9000); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
                            Date date = c.getTime(); //结果
                            //打包
                            POSIXFactory.getPOSIX().chdir(idPath);
                            RuntimeExec.runtimeExec("zip -r down.zip " + idPath).get("info").toString();
                            packStatusIosApkDao.updateStatus("打包成功", domain  + packStatusIosApk.getId()
                                        + "/" + "html/index.html", domain  + packStatusIosApk.getId() + "/" + "down.zip", date,packStatusIosApk.getId());
                        }else {
                            RuntimeExec.runtimeExec("rm -rf " + idPath);
                            packStatusIosApkDao.updateStatus("安卓打包错误","","", new Date(),packStatusIosApk.getId());
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }
    }





}
