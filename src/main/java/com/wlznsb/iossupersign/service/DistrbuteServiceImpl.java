package com.wlznsb.iossupersign.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.wlznsb.iossupersign.dao.AppleIisDao;
import com.wlznsb.iossupersign.dao.DistributeDao;
import com.wlznsb.iossupersign.dao.PackStatusDao;
import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.entity.Distribute;
import com.wlznsb.iossupersign.entity.PackStatus;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DistrbuteServiceImpl{


    @Value("${qiniuyun.accessKey}")
    private String qiniuyunAccessKey;
    @Value("${qiniuyun.secretKey}")
    private String qiniuyunSecretKey;
    @Value("${qiniuyun.bucket}")
    private String qiniuyunBucket;
    @Value("${qiniuyun.url}")
    private String qiniuyunUrl;

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
    private DistributeDao distributeDao;
    @Autowired
    private AppleIisDao appleIisDao;
    @Autowired
    private PackStatusDao packStatusDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public Distribute uploadIpa(MultipartFile ipa, User user,String rootUrl) {
        Integer id = null;
        try {
            if(ipa.getSize() != 0){

                //获取下一次的主键id
                id = distributeDao.getId();
                if(id == null){
                    id = 1;
                }else {
                    id = id + 1;
                }
                new File("/sign/temp/" + user.getAccount() + "/distribute/" + id + "/").mkdirs();
                //icon路径
                String iconPath = new File("/sign/temp/" + user.getAccount() + "/distribute/" + id + "/" +  id + ".png").getAbsolutePath();
                //ipa路径
                String ipaPath = new File("/sign/temp/" + user.getAccount() + "/distribute/" + id + "/" +  id + ".ipa").getAbsolutePath();
                //ipa解压路基
                String ipaUnzipPath = new File("/sign/temp/" + user.getAccount() + "/distribute/" + id + "/Payload").getAbsolutePath();
                //写出
                System.out.println(ipaPath);
                ipa.transferTo(new File(ipaPath));
                //ipa.transferTo(new File(iconPath));
                //读取信息
                Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaPath,iconPath);
                if(mapIpa.get("code") != null){
                    throw new RuntimeException("无法读取包信息");
                }
                String cmd = "unzip -oq " + ipaPath + " -d " + "/sign/temp/" + user.getAccount() + "/distribute/" + id + "/";
                log.info("解压命令" + cmd);
                log.info("解压结果" + RuntimeExec.runtimeExec(cmd).get("info"));
                String name = mapIpa.get("displayName").toString();
                String url = rootUrl + "distribute/down/" + id;

                Distribute distribute = new Distribute(id,user.getAccount(),name,mapIpa.get("package").
                        toString(),mapIpa.get("versionName").toString(),iconPath,ipaUnzipPath,null,url,new Date(),"极速下载",null);
                distributeDao.add(distribute);
                return distribute;
            }else {
                throw new RuntimeException("请不要上传空包");
            }
        }catch (Exception e){
            log.info(e.toString());
            e.printStackTrace();
            throw  new RuntimeException("上传失败:" + e.getMessage());
        }
    }

    /**
     * 合并apk
     * @param apk
     * @param user
     * @param id
     * @return
     */
    @Transactional
    public int uploadApk(MultipartFile apk,User user, Integer id) {
        try {
            if(apk.getSize() != 0){
                String aokPath = new File("/sign/temp/" + user.getAccount() + "/distribute/" + id + "/" + id + ".apk").getAbsolutePath();
                apk.transferTo(new File(aokPath));

                distributeDao.uploadApk(aokPath, id);
            }else {
                throw new RuntimeException("请不要上传空包");
            }
        }catch (Exception e){
            log.info(e.toString());
            throw  new RuntimeException("上传失败:" + e.getMessage());
        }
        return 0;
    }



    /**这里不能加事务否则状态无法读取
     *
     * @param id
     * @param uuid
     * @param url
     * @param udid
     * @return
     */
    public String getUuid(int id,String uuid,String url, String udid){
        try {
            log.info("udid:" + udid);
            //查询这个应用对应的账号
            Distribute distribute = distributeDao.query(id);
            User user = userDao.queryAccount(distribute.getAccount());
            List<AppleIis> appleIislist;
            if(user.getCount() > 0){
                 //如果共有池有就查询共有的
                log.info("使用共有证书");
                 appleIislist = appleIisDao.queryPublicIis(distribute.getAccount());
                 userDao.reduceCount(user.getAccount());
            }else {
                log.info("使用私有证书");
                //共有池没有就查询自己的证书
                 appleIislist = appleIisDao.queryPrivateIis(distribute.getAccount());
            }
            //判断循环结束是否成功添加了设备,如果为null说明没有可用的证书了
            int isSuccess = 1;
            //如果有证书
            if(appleIislist.size() != 0){
                log.info("开始遍历证书");
                packStatusDao.updateStatus("正在匹配证书", uuid);
                for (AppleIis appleIis1:appleIislist){
                    AppleApiUtil appleApiUtil = new AppleApiUtil(appleIis1.getIis(),
                            appleIis1.getKid(),appleIis1.getP8());
                    //手动设置token
                    appleApiUtil.initTocken();
                    packStatusDao.updateStatus("正在添加设备", uuid);
                    //直接添加设备,如果添加设备为null再去寻找设备
                    String addUuid = appleApiUtil.addUuid(udid);
                    log.info("添加addUuid结果" + addUuid);
                    if(null == addUuid){
                        addUuid = appleApiUtil.queryDevice(udid);
                    }else {
                        if(!addUuid.equals("no")){
                            appleIisDao.reduceCount(appleIis1.getIis());
                        }else {
                            packStatusDao.update(new PackStatus(null, distribute.getAccount(), distribute.getPageName(), null, null, appleIis1.getIis(), null, null,null , "失败udid不合法", null,null,null,null), uuid);
                            throw  new RuntimeException("udid不合法");
                        }
                    }
                    log.info("添加addUuid结果2" + addUuid);
                    //查询id,查不到就添加
                    if(addUuid != null){
                        packStatusDao.updateStatus("注册配置文件", uuid);
                        Map<String,String> map = appleApiUtil.addProfiles(appleIis1.getIdentifier(),appleIis1.getCertId(), addUuid, ServerUtil.getUuid(),new File("/sign/mode/temp").getAbsolutePath());
                        String filePro = map.get("filePath");
                        //如果pro文件创建成功
                        if(filePro != null){
                            //包名
                            String nameIpa = new Date().getTime() + ".ipa";
                            //临时目录
                            String temp = new File("/sign/mode/temp").getAbsolutePath() + "/" + nameIpa;
                            String cmd = "/sign/mode/zsign -k " + appleIis1.getP12() + " -p 123456 -m " + filePro + " -o " + temp + " -z 1 " + distribute.getIpa();
                            packStatusDao.updateStatus("正在签名", uuid);
                            log.info("签名结果" + RuntimeExec.runtimeExec(cmd).get("status").toString());
                            log.info("签名命令" + cmd);
                            log.info("包名"+ nameIpa);
                            //获取plist
                            String plist = IoHandler.readTxt(new File("/sign/mode/install.plist").getAbsolutePath());
                            packStatusDao.updateStatus("准备下载", uuid);
                            //是否使用七牛云
                            if(!this.qiniuyunAccessKey.equals("")){
                                log.info("使用七牛云");
                                plist = plist.replace("urlRep", this.qiniuyunUrl + uploadQly(temp));
                                //删除ipa
                                new File("/sign/mode/temp/" + nameIpa).delete();
                            }else if(!this.aliyunAccessKey.equals("")){
                                log.info("使用阿里云");
                                plist = plist.replace("urlRep", this.aliyunDownUrl + uploadAly(temp));
                                //删除ipa
                                new File("/sign/mode/temp/" + nameIpa).delete();
                            }else {
                                log.info("不使用七牛云");
                                plist = plist.replace("urlRep", url  + nameIpa);
                                log.info("ipa路径:" + url  + nameIpa);
                            }
                            //bundle要随机不然有时候没进度条
                            plist = plist.replace("bundleRep", ServerUtil.getUuid());
                            plist = plist.replace("versionRep", distribute.getVersion());
                            String iconPath = url + distribute.getAccount() + "/distribute/" + id + "/" + id + ".png";
                            plist = plist.replace("iconRep", iconPath);
                            plist = plist.replace("appnameRep", distribute.getAppName());
                            String plistName = new Date().getTime() + ".plist";
                            IoHandler.writeTxt(new File("/sign/mode/temp").getAbsolutePath() + "/" + plistName, plist);
                            String plistUrl = "itms-services://?action=download-manifest&url=" +  url + plistName;
                            packStatusDao.update(new PackStatus(null, distribute.getAccount(), distribute.getPageName(), null, null, appleIis1.getIis(), null, nameIpa,plistUrl , "点击下载", null,null,null,null), uuid);
                            //删除配置文件
                            log.info("删除配置文件");
                            appleApiUtil.deleProfiles(map.get("id"));
                            log.info("打包完成");
                            log.info("plist名" + plistName);
                            isSuccess = 0;
                            return plistName;
                        }else {
                            log.info("创建配置文件失败");
                            //appleIisDao.updateStatus(0, appleApiUtil.getIis());
                        }
                    }else {
                        log.info("添加指定设备失败,证书失效");
                        appleIisDao.updateStatus(0, appleApiUtil.getIis());
                    }
                }
                if(isSuccess == 1){
                    packStatusDao.update(new PackStatus(null, distribute.getAccount(), distribute.getPageName(), null, null, null, null, null,null , "没有可用的证书", null,null,null,null), uuid);
                }
            }else {
                packStatusDao.update(new PackStatus(null, distribute.getAccount(), distribute.getPageName(), null, null, null, null, null,null , "没有可用的证书", null,null,null,null), uuid);
                throw  new RuntimeException("没有可用的证书");
            }
        }catch (Exception e){
            log.info(e.toString());
            throw  new RuntimeException("失败" + e.getMessage());
        }
        return null;
    }


    public int dele(String account, int id) {
        try {
            Distribute distribute = distributeDao.query(id);
            if(distribute != null){
                if(distributeDao.dele(account, id) == 0){
                    File file = new File("/sign/temp/" + account + "/distribute/" + id).getAbsoluteFile();
                    System.out.println(file.getAbsolutePath());
                    FileSystemUtils.deleteRecursively(file);
                    throw  new RuntimeException("删除失败");
                }
            }else {
                throw  new RuntimeException("该应用不存在");
            }

        }catch (Exception e){
            throw  new RuntimeException("删除失败" + e.getMessage());
        }
        return 0;
    }


    public List<Distribute> queryAccountAll(String account) {
        try {
            List<Distribute> distributeList = distributeDao.queryAccountAll(account);
            return distributeList;
        }catch (Exception e){
            throw  new RuntimeException("查询失败" + e.getMessage());
        }
    }

    /**
     * 上传七牛云
     * @return
     */
    public String uploadQly(String localFilePath){
        Long time = System.currentTimeMillis();
        Configuration cfg = new Configuration(Region.region2());
        cfg.useHttpsDomains = false;
        UploadManager uploadManager = new UploadManager(cfg);
        String key = new Date().getTime() + ".ipa";
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
    public String uploadAly(String localFilePath){
        Long time = System.currentTimeMillis();
        try {
            String name = System.currentTimeMillis() + ".ipa";
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

}
