package com.wlznsb.iossupersign.task;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.wlznsb.iossupersign.common.TimeLockInfo;
import com.wlznsb.iossupersign.mapper.*;
import com.wlznsb.iossupersign.entity.*;
import com.wlznsb.iossupersign.service.DistrbuteServiceImpl;
import com.wlznsb.iossupersign.service.MdmDistrbuteServiceImpl;
import com.wlznsb.iossupersign.util.*;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
    private MdmDistrbuteServiceImpl mdmDistrbuteService;

    @Autowired
    private EnterpriseSignCertDao enterpriseSignCertDao;

    @Value("${signCount}")
    private Integer signCount;

    @Autowired
    private PackStatusDao packStatusDao;

    @Autowired
    private MdmPackStatusMapper mdmPackStatusMapper;



    @Value("${server.servlet.context-path}")
    private String index;

    @Autowired
    private PackStatusIosApkDao packStatusIosApkDao;

    private ThreadPoolExecutor poolExecutor;

    private ThreadPoolExecutor mdmSuperPoolExecutor;

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

        mdmSuperPoolExecutor =  new ThreadPoolExecutor(thread, thread, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void run(){
        List<MdmPackStatusEntity> distributeList = mdmPackStatusMapper.queryPage("排队中");
       for (MdmPackStatusEntity packStatus:distributeList){
           packStatusDao.updateStatus("准备中", packStatus.getUuid());
           poolExecutor.execute(new Runnable() {
               @Override
               public void run() {
                   log.info(Thread.currentThread().getName());
                   mdmDistrbuteService.getUuid(packStatus);
               }
           });
       }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void runmdmsuper(){
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


    @Autowired
    private MdmSuperUpdateIpaTaskMapper updateIpaTaskMapper;

    @Value("${qiniuyun.accessKey}")
    private String qiniuyunAccessKey;
    @Value("${qiniuyun.secretKey}")
    private String qiniuyunSecretKey;
    @Value("${qiniuyun.bucket}")
    private String qiniuyunBucket;
    @Value("${qiniuyun.url}")
    private String qiniuyunUrl;
    @Value("${qiniuyun.reg}")
    private String qiniuyunReg;

    @Value("${aliyun.accessKey}")
    private String aliyunAccessKey;
    @Value("${aliyun.secretKey}")
    private String aliyunSecretKey;
    @Value("${aliyun.bucket}")
    private String aliyunBucket;
    @Value("${aliyun.url}")
    private String aliyunUrl;
    @Value("${aliyun.downUrl}")
    private String aliyunDownUrl;


    @Autowired
    private MdmDistributeMapper mdmDistributeMapper;


    @Autowired
    private DeviceCommandTaskMapper deviceCommandTaskMapper;


    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    @Autowired
    private DeviceStatusMapper deviceStatusMapper;

    /**
     * mdm ios超级签更新任务
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void ios超级签更新任务(){
        log.info("ios超级签更新任务");
        List<MdmSuperUpdateIpaTaskEntity> 待处理 = updateIpaTaskMapper.selectByStatus("待处理");

        for (MdmSuperUpdateIpaTaskEntity s: 待处理) {

            try {
                MdmPackStatusEntity packStatus = mdmPackStatusMapper.selectById(s.getPackStatusId());
                //查询这个应用对应的账号
                MdmDistributeEntity distribute = mdmDistributeMapper.query(packStatus.getAppId());

                //包名
                String nameIpa = new Date().getTime() + ".ipa";
                //临时目录
                String temp = new File("./sign/mode/temp").getAbsolutePath() + "/" + nameIpa;
                String cmd = "./sign/mode/zsign -k " + packStatus.getP12Path() + " -p 123456 -m " + packStatus.getMobilePath() + " -o " + temp + " -z 1 " + distribute.getIpa();
                log.info("开始签名" + cmd);
                packStatusDao.updateStatus("正在签名", packStatus.getUuid());
                Map<String,Object>  map1 =  RuntimeExec.runtimeExec(cmd);
                log.info("签名结果" + map1.get("status").toString());
                log.info("签名反馈" + map1.get("info").toString());
                log.info("签名命令" + cmd);
                log.info("包名"+ nameIpa);
                if(!map1.get("status").toString().equals("0")){
                    throw  new RuntimeException("签名失败");
                }
                //获取plist
                String plist = IoHandler.readTxt(new File("./sign/mode/install.plist").getAbsolutePath());

                //是否使用七牛云
                if(!this.qiniuyunAccessKey.equals("")){
                    log.info("使用七牛云");
                    plist = plist.replace("urlRep", this.qiniuyunUrl + uploadQly(temp,"ipa"));
                    //删除ipa
                    new File("./sign/mode/temp/" + nameIpa).delete();
                }else if(!this.aliyunAccessKey.equals("")){
                    log.info("使用阿里云");
                    plist = plist.replace("urlRep", this.aliyunDownUrl + uploadAly(temp,"ipa"));
                    //删除ipa
                    new File("./sign/mode/temp/" + nameIpa).delete();
                }else {
                    log.info("不使用七牛云");
                    if(SettingUtil.ipaDownUrl != null && !SettingUtil.ipaDownUrl.equals("")){
                        plist = plist.replace("urlRep", SettingUtil.ipaDownUrl  + nameIpa);
                    }else {
                        plist = plist.replace("urlRep", packStatus.getUrl()  + nameIpa);
                    }
                    log.info("ipa路径:" + packStatus.getUrl()  + nameIpa);
                }
                //bundle要随机不然有时候没进度条
                DeviceStatusEntity deviceStatusEntity = deviceStatusMapper.selectById(packStatus.getDeviceId());
                log.info("设备状态" + deviceStatusEntity.getStatus());
//                if(deviceStatusEntity.getStatus().equals(DeviceStatusEntity.STATUS_ON)){
//                    log.info("设备在线 直接更新");
//                    plist = plist.replace("bundleRep", packStatus.getPageName());
//                }else {
//                    log.info("设备不在线 修改包名");
//                    plist = plist.replace("bundleRep", packStatus.getPageName() + "update");
//                }
                plist = plist.replace("bundleRep", packStatus.getPageName());
                log.info(plist);
                plist = plist.replace("versionRep", distribute.getVersion());
                String iconPath = packStatus.getUrl() + distribute.getAccount() + "/mdmdistribute/" + packStatus.getAppId() + "/" + packStatus.getAppId() + ".png";
                plist = plist.replace("iconRep", iconPath);
                plist = plist.replace("appnameRep", distribute.getAppName() + "更新防掉签文件");
                String plistName = new Date().getTime() + ".plist";
                IoHandler.writeTxt(new File("./sign/mode/temp").getAbsolutePath() + "/" + plistName, plist);
                String plistUrl;
                //如果没有指定下载地址就是默认的
                plistUrl = packStatus.getUrl() + plistName;

                //删除配置文件
                log.info("打包完成");
                log.info("plist名" + plistName);
                DeviceInfoEntity deviceInfoEntity = deviceInfoMapper.selectById(packStatus.getDeviceId());



                Date date = new Date();
                DeviceCommandTaskEntity taskEntity = new DeviceCommandTaskEntity();
                taskEntity.setTaskId(MyUtil.getUuid());
                taskEntity.setDeviceId(packStatus.getDeviceId());
                taskEntity.setCmd("InstallApplication");
                taskEntity.setExecResult("");
                taskEntity.setCreateTime(date);
                taskEntity.setExecTime(date);
                taskEntity.setResultTime(date);
                taskEntity.setTaskStatus(0);
                taskEntity.setPushCount(0);
                taskEntity.setExecResultStatus("");
                taskEntity.setUdid(deviceInfoEntity.getUdid());
                taskEntity.setCertId(deviceInfoEntity.getCertId());
                String cmda = "{\"type\":\"ManifestURL\",\"value\":\"#plist#\"}";
                cmda = cmda.replace("#plist#",plistUrl);
                taskEntity.setCmdAppend(cmda);


                deviceCommandTaskMapper.insert(taskEntity);
                s.setStatus("已处理");
                s.setUpdateTime(new Date());
                s.setPlistUrl(plistUrl);
                s.setTaskId(taskEntity.getTaskId());
                updateIpaTaskMapper.updateById(s);

            }catch (Exception e){

                log.info("更新ipa失败");
                e.printStackTrace();
                s.setStatus("失败");
                s.setUpdateTime(new Date());
                updateIpaTaskMapper.updateById(s);

            }

        }

    }



    /**
     * 上传七牛云
     * @return
     */
    public String uploadQly(String localFilePath,String suffix){
        Long time = System.currentTimeMillis();
        Configuration cfg;
        //内网
        if(this.qiniuyunReg.equals("huadong")){
            cfg = new Configuration(Region.qvmRegion0());
        }else if(this.qiniuyunReg.equals("huabei")){
            cfg = new Configuration(Region.qvmRegion1());
        }else {
            cfg = new Configuration(Region.qvmRegion1());
        }

        cfg.useHttpsDomains = false;

        UploadManager uploadManager = new UploadManager(cfg);
        String key = new Date().getTime() + "." + suffix;
        Auth auth = Auth.create(qiniuyunAccessKey, qiniuyunSecretKey);
        String upToken = auth.uploadToken(qiniuyunBucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("本次上传耗费:" + (System.currentTimeMillis() - time)/1000 + "秒");
            return putRet.key;
        } catch (Exception ex) {
            log.info("上传失败" + ex.toString());
            return null;
        }
    }

    /**
     * 阿里云上传
     * @param localFilePath
     * @return
     */
    public String uploadAly(String localFilePath,String  suffix){
        Long time = System.currentTimeMillis();
        try {
            String name = System.currentTimeMillis() + "." + suffix;
            String endpoint = aliyunUrl;
            String accessKeyId = aliyunAccessKey;
            String accessKeySecret = aliyunSecretKey;
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliyunBucket,name, new File(localFilePath));
            ossClient.putObject(putObjectRequest);
            ossClient.shutdown();
            log.info("阿里云上传时间:" + (System.currentTimeMillis() - time)/1000 + "秒");
            return name;
        }catch (Exception e){
            log.info("阿里云上传失败:" + e.toString());
            return null;
        }
    }

    @Resource
    private AppleIisMapper appleIisMapper;

    /**
     * 检测失效超级签
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void checkChaojiqianP12() throws IOException {
        log.info("检测失效超级签");
        List<AppleIisEntity> appleIisEntities = appleIisMapper.selectByStatus(0);
        for (AppleIisEntity a:
                appleIisEntities) {
            AppleApiUtil appleApiUtil = new AppleApiUtil(a.getIis(),
                    a.getKid(),a.getP8());
            //手动设置token
            if(appleApiUtil.init()){
                log.info("证书未失效");
                a.setStatus(1);
                appleIisMapper.updateById(a);
            }else {
                log.info("证书失效");
            }
            System.out.println(a);

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
                    String signPath = "./sign/mode/temp/sign" + uuid + ".ipa";

                    //是否是时间锁
                    if(packStatusEnterpriseSign.getIsTimeLock().equals(1)){
                        log.info("需要注入时间锁");

                        TimeLockInfo timeLockInfo = new TimeLockInfo();
                        timeLockInfo.setRequest_url(packStatusEnterpriseSign.getLockRequestUrl());

                        File file = new File(packStatusEnterpriseSign.getIpaPath() + "/lock.info");
                        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
                        fileWriter.write(JSON.toJSONString(timeLockInfo));


//                        String dirPath = "./sign/mode/temp/unsigned_sign" + packStatusEnterpriseSign.getId();
//                        String ipaPath = "./sign/mode/temp/unsigned_sign"  + packStatusEnterpriseSign.getId() + ".ipa";

//                        ZipUtil.zip(ipaPath,dirPath);

                        String dlibPath = new File("./sign/mode/lock.dylib").getAbsolutePath();
                        String cmd = "./sign/mode/zsign -k " + enterpriseSignCert.getCertPath() + " -p " + enterpriseSignCert.getPassword() + " -m " + enterpriseSignCert.getMoblicPath() + " -o " + signPath + " -z 1 " + packStatusEnterpriseSign.getIpaPath() + " -l " + dlibPath;
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
                    }else {
                        log.info("不需要注入时间锁");
                        String cmd = "./sign/mode/zsign -k " + enterpriseSignCert.getCertPath() + " -p " + enterpriseSignCert.getPassword() + " -m " + enterpriseSignCert.getMoblicPath() + " -o " + signPath + " -z 1 " + packStatusEnterpriseSign.getIpaPath();
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
            }
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }
    }

    @Autowired
    private IosSignSoftwareDistributeStatusDao distributeStatusDao;

    @Autowired
    private IosSignUdidCertDao iosSignUdidCertDao;

    @Autowired
    private IosSignSoftwareDistributeDao iosSignSoftwareDistributeDao;




    /**
     * 单点分发打包
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void iosSignPack(){
        List<IosSignSoftwareDistributeStatus> list = distributeStatusDao.queryStatusAll("排队中");
        for (IosSignSoftwareDistributeStatus ios:list){
            if("排队中".equals(ios.getStatus())){
                try {
                    String uuid = MyUtil.getUuid();
                    IosSignSoftwareDistribute iosSignSoftwareDistribute = iosSignSoftwareDistributeDao.query(ios.getIosId());
                    IosSignUdidCert iosSignUdidCert = iosSignUdidCertDao.query(ios.getCertId());

                    SystemctlSettingsEntity systemctlSettingsEntity = settingsMapper.selectOne(null);

                    if(systemctlSettingsEntity.getMqDomain().equals("www.xxx.com")){
                        distributeStatusDao.updateStatus("请先前往系统设置-常规设置-填写主域名", ios.getUuid());
                        throw  new RuntimeException("请先前往系统设置-常规设置-填写主域名");
                    }


                    try {
                        userDao.reduceCountC(iosSignSoftwareDistribute.getAccount(),systemctlSettingsEntity.getOneSuperTotal());
                    }catch (Exception e){
                        distributeStatusDao.updateStatus("公有池不足", ios.getUuid());
                        continue;
                    }

                    String plist = IoHandler.readTxt(new File("./sign/mode/install.plist").getAbsolutePath());
                    //bundle要随机不然有时候没进度条
                    plist = plist.replace("bundleRep", uuid);
                    plist = plist.replace("versionRep", iosSignSoftwareDistribute.getVersion());
                    plist = plist.replace("iconRep", iosSignSoftwareDistribute.getIcon());
                    plist = plist.replace("appnameRep",iosSignSoftwareDistribute.getAppName());
                    //对ipa签名
                    String uuidTemp = MyUtil.getUuid();
                    String signPath = "./sign/mode/temp/" + uuidTemp +".ipa";
                    String cmd = "./sign/mode/zsign -k " + iosSignUdidCert.getP12Path() + " -p " + iosSignUdidCert.getP12Password() + " -m " + iosSignUdidCert.getMobileprovisionPath() + " -o " + signPath + " -z 1 " + iosSignSoftwareDistribute.getIpa();

                    if(iosSignSoftwareDistribute.getAutoPageName() == 1){
                        log.info("随机包名");
                        cmd = "./sign/mode/zsign -k " + iosSignUdidCert.getP12Path() + " -p " + iosSignUdidCert.getP12Password() + " -m " + iosSignUdidCert.getMobileprovisionPath() + " -o " + signPath + " -z 1 " + iosSignSoftwareDistribute.getIpa() + " -b " + new Date().getTime();
                    }
                    log.info("开始签名" + cmd);
                    distributeStatusDao.updateStatus("签名中", ios.getUuid());
                    Map<String,Object>  map1 =  RuntimeExec.runtimeExec(cmd);
                    log.info("签名结果" + map1.get("status").toString());
                    log.info("签名反馈" + map1.get("info").toString());
                    log.info("签名命令" + cmd);
                    if(!map1.get("status").toString().equals("0")){
                        distributeStatusDao.updateStatus("打包错误", ios.getUuid());
                        throw  new RuntimeException("打包错误");
                    }
                    distributeStatusDao.updateStatus("准备下载", ios.getUuid());
                    //上传云端
                    String ipaUrl = distrbuteService.uploadSoftwareIpa(signPath);


                    if(null == ipaUrl){
                        ipaUrl ="https://" +  systemctlSettingsEntity.getMqDomain() + "/" + uuidTemp + ".ipa";
                    }
                    plist = plist.replace("urlRep", ipaUrl);
                    String plistName = uuidTemp + ".plist";
                    IoHandler.writeTxt(new File("./sign/mode/temp/" +  plistName).getAbsolutePath(), plist);
                    String plistUrl = "itms-services://?action=download-manifest&url=" + "https://" +  systemctlSettingsEntity.getMqDomain() + "/" + plistName;
                    distributeStatusDao.updateDownUrl("点击安装",plistUrl, ios.getUuid());
                }catch (Exception e){
                    distributeStatusDao.updateStatus("打包错误", ios.getUuid());
                }

            }
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
                JsonNode jsonNode = new ObjectMapper().readTree(data);
                if(jsonNode.get("data").get("status").asText().equals("revoked")){
                    log.info(enterpriseSignCert + "掉签");
                    enterpriseSignCertDao.updateCertStatus("掉签", enterpriseSignCert.getMd5());
                }
            }
        }
    }




    @Autowired
    private SystemctlSettingsMapper settingsMapper;

    @Scheduled(cron = "0/5 * * * * *")
    public void iosApkPack(){
        //获取mode路径
        String modePath = new File("./sign/mode/static/mode").getAbsolutePath();
        //获取temp路径
        String tempPath = new File("./sign/mode/static/down/mq").getAbsolutePath();

        //处理打包路径
        String execPath = new File("./").getAbsolutePath();

        //备份当前目录
//        String initPath = RuntimeExec.runtimeExec("pwd").get("info").toString();

        try {
            List<PackStatusIosApk> list = packStatusIosApkDao.queryAll();
            for (PackStatusIosApk packStatusIosApk:list){
                //每次执行都切回初始目录
                //POSIXFactory.getPOSIX().chdir(initPath);
                if("排队中".equals(packStatusIosApk.getStatus())){


                    log.info("更新id" + packStatusIosApk.getId());


                    User user = userDao.queryAccount(packStatusIosApk.getAccount());
                    SystemctlSettingsEntity systemctlSettingsEntity = settingsMapper.selectOne(null);
                    if(systemctlSettingsEntity.getMqDomain().equals("www.xxx.com")){
                        packStatusIosApkDao.updateStatus("请先前往系统设置-常规设置-填写主域名","","", new Date(),packStatusIosApk.getId());
                        throw  new RuntimeException("请先前往系统设置-常规设置-填写主域名");
                    }

                    //如果是普通会员就扣公有池
                    try {
                        userDao.reduceCountC(packStatusIosApk.getAccount(),systemctlSettingsEntity.getWebPackTotal());
                    }catch (Exception e){
                        packStatusIosApkDao.updateStatus("公有池不足!","","", new Date(),packStatusIosApk.getId());
                        continue;
                    }
                    //System.out.println( status.getStatusStatus());;
                    // System.out.println("cp -rf " + modePath + " " + tempPath + "/" + status.getStatusId());
                    //获取数字根目录
                    String idPath = new File("./sign/mode/static/down/mq/" + packStatusIosApk.getId()).getAbsolutePath();
                    //拷贝mode目录到数字目录
                    log.info("拷贝命令:" + "cp -rf " + modePath + " " + tempPath + "/" + packStatusIosApk.getId());
                    RuntimeExec.runtimeExec("cp -rf " + modePath + " " + tempPath + "/" + packStatusIosApk.getId());

                    log.info("更换shell.sh");
                    String s = FileUtil.readUtf8String(new File(modePath + "/android/temp/shell.sh"));
                    s = s.replace("#项目路径#",idPath + "/android/temp/app");
                    FileUtil.writeUtf8String(s,new File(idPath + "/android/temp/shell.sh"));
                    log.info("完成shell内容" + s);

                    // log.info("给予权限命令:" + "chmod -R 777 " + tempPath +  "/" + packStatusIosApk.getId());
                   // RuntimeExec.runtimeExec("chmod -R 777 " + tempPath + "/" + packStatusIosApk.getId());

                    //获取为签名描述文件路径,描述文件名和iconpath名是一样的所以提取出来
                    String mobileConfig = packStatusIosApk.getRemark();
                    //签名后的描述文件输出路径
                    String mobileConfigSign =  idPath +  "/" + "ios" + "/" + "sign.mobileconfig";
                    //三个证书的位置,如果没传就用默认的
                    String serverCrt = new File("./sign/mode/cert/cert.pem").getAbsolutePath();
                    String rootCrt =  new File("./sign/mode/cert/cert.pem").getAbsolutePath();
                    String keyCrt =  new File("./sign/mode/cert/cert.key").getAbsolutePath();
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

                            urlText = urlText.replace("http://www.wlznsb.cn/html", "https://" +  systemctlSettingsEntity.getMqDomain() + "pack/distribute/" + packStatusIosApk.getId());
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
                        log.info("安卓目录" + androidPath);

                        //切换到执行安卓打包的目录
                       // POSIXFactory.getPOSIX().chdir(tempPath + "/" + packStatusIosApk.getId() + "/" + "android" + "/" + "temp" + "/");

                        log.info("执行shell命令" + "sh " + idPath + "/android/temp/shell.sh");
                        Map<String,Object> ccc =  RuntimeExec.runtimeExec("sh " + idPath + "/android/temp/shell.sh");
                        log.info(ccc.get("info").toString());
                        boolean isSu =  "0".equals(ccc.get("status").toString());
                        //执行打包命令
                        if(isSu){
                            log.info("安卓打包成功");
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
                            log.info(indexName);

                            String indexText = IoHandler.readTxt(indexName);
                            IoHandler.writeTxt(indexName, indexText.replace("应用名称", packStatusIosApk.getAppName()));
                            log.info("替换idnex结束");
                            //删除temp目录
                            RuntimeExec.runtimeExec("rm -rf " + androidPath + "/" + "temp");
                            //清理ios多余目录
                            RuntimeExec.runtimeExec("rm -rf " + idPath + "/" + "ios" + "/" + "cert " +  idPath + "/" + "ios" + "/" + "demo.txt");
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); //设置时间
                            c.add(Calendar.DATE, 9000); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
                            Date date = c.getTime(); //结果
                            //打包
                            //POSIXFactory.getPOSIX().chdir(idPath);
                            log.info("开始压缩");
                            RuntimeExec.runtimeExec("tar -zcvf " + idPath  +  "/down.zip " + idPath ).get("info").toString();
                            log.info("更新id" + packStatusIosApk.getId());

                            packStatusIosApkDao.updateStatus("打包成功", "https://" +  systemctlSettingsEntity.getMqDomain()  + "/mq/" + packStatusIosApk.getId()
                                        + "/" + "html/index.html", "https://" + systemctlSettingsEntity.getMqDomain() + "/mq/" + packStatusIosApk.getId() + "/" + "down.zip", date,packStatusIosApk.getId());


                        }else {
                            log.info("安卓打包失败");
                           // RuntimeExec.runtimeExec("rm -rf " + idPath);
                            packStatusIosApkDao.updateStatus("安卓打包错误","","", new Date(),packStatusIosApk.getId());
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.info("异常退出");
            log.info(e.toString());
            e.printStackTrace();
        }
    }




}
