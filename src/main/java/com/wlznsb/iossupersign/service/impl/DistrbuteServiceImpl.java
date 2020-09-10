package com.wlznsb.iossupersign.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.wlznsb.iossupersign.entity.*;
import com.wlznsb.iossupersign.service.DistrbuteService;
import com.wlznsb.iossupersign.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
@Slf4j
public class DistrbuteServiceImpl implements DistrbuteService {


    @Value("${qiniuyun.accessKey}")
    private String accessKey;
    @Value("${qiniuyun.secretKey}")
    private String secretKey;
    @Value("${qiniuyun.bucket}")
    private String bucket;
    @Value("${qiniuyun.url}")
    private String url;



    @Autowired
    private DistributeDao distributeDao;
    @Autowired
    private AppleIisDao appleIisDao;
    @Autowired
    private PackStatusDao packStatusDao;

    @Override
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
                //写出
                System.out.println(ipaPath);
                ipa.transferTo(new File(ipaPath));
                //ipa.transferTo(new File(iconPath));
                //读取信息
                Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaPath,iconPath);
                if(mapIpa.get("code") != null){
                    throw new RuntimeException("无法读取包信息");
                }
                String name = mapIpa.get("displayName").toString();
                url = rootUrl + "distribute/down/" + Base64.getEncoder().encodeToString(String.valueOf(id).getBytes());
                Distribute distribute = new Distribute(id,user.getAccount(),name,mapIpa.get("package").
                        toString(),mapIpa.get("versionName").toString(),iconPath,ipaPath,null,url,new Date(),"极速下载",null);
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
    @Override
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
    @Override
    public String getUuid(int id,String uuid,String url, String udid){
        try {
            log.info("udid:" + udid);
            //查询这个应用对应的账号
            Distribute distribute = distributeDao.query(id);
            //查询账号所有可用的证书
            List<AppleIis> appleIislist = appleIisDao.queryUsIis(distribute.getAccount());
            //判断循环结束是否成功添加了设备,如果为null说明没有可用的证书了
            int isSuccess = 1;
            //如果有证书
            if(appleIislist.size() != 0){
                log.info("开始遍历证书");
                packStatusDao.updateStatus("正在匹配证书", uuid);
                for (AppleIis appleIis1:appleIislist){
                    AppleApiUtil appleApiUtil = new AppleApiUtil(appleIis1.getIis(),
                            appleIis1.getKid(),appleIis1.getP8());
                    //如果初始化失败就把状态设置成失效
                    log.info("开始初始化");
                    if(appleApiUtil.init()){
                        packStatusDao.updateStatus("正在添加设备", uuid);
                        log.info("初始化完毕");
                        //添加id后返回的唯一id
                        String addUuid = null;
                        //查询所有设备
                        String devices = appleApiUtil.queryDevices();
                        //获取剩余设备数
                        int deviceCount = 100 - new ObjectMapper().readTree(devices).get("meta").get("paging").get("total").asInt();
                        //判断是否有这个设备
                        int isAdd = devices.indexOf(udid);
                        log.info("是否有这个设备" + isAdd);
                        if(isAdd == -1){
                            log.info("没有这个设备");
                            addUuid = appleApiUtil.addUuid(udid);
                            appleIisDao.updateCount(deviceCount -1,appleIis1.getIis());
                        }else {
                            log.info("有这个设备");
                            Integer count = new ObjectMapper().readTree(devices).get("meta").get("paging").get("total").asInt();
                            //找出id
                            for (int i = 0; i < count; i++) {
                                String udid1 = new ObjectMapper().readTree(devices).get("data").get(i).get("attributes").get("udid").asText();
                                if(udid.equals(udid1)){
                                    addUuid = new ObjectMapper().readTree(devices).get("data").get(i).get("id").asText();
                                    System.out.println(deviceCount);
                                    break;
                                }
                            }
                        }
                        //判断是否添加成功
                        if(addUuid != null){
                            log.info("adduuid" + addUuid);
                            packStatusDao.updateStatus("注册配置文件", uuid);
                            //获取pros
                            String profiles = appleApiUtil.queryProfiles();
                            String filePro = null;
                            //判断有没有注册过
                            if(profiles.indexOf(addUuid) == -1){
                                log.info("配置文件没注册过");
                                filePro = appleApiUtil.addProfiles(appleIis1.getIdentifier(),appleIis1.getCertId(), addUuid, addUuid,new File("/sign/mode/temp").getAbsolutePath());
                            }else {
                                log.info("配置文件有注册过");
                                Integer count =  new ObjectMapper().readTree(profiles).get("meta").get("paging").get("total").asInt();
                                System.out.println(count);
                                for (int i = 0; i < count; i++) {
                                    String proId = new ObjectMapper().readTree(profiles).get("data").get(i).get("attributes").get("name").asText();
                                    if(proId.equals(addUuid)){
                                        String certPro = new ObjectMapper().readTree(profiles).get("data").get(i).get("attributes").get("profileContent").asText();
                                        //写出路径
                                        byte[] data = Base64.getDecoder().decode(certPro);
                                        filePro = new File("/sign/mode/temp").getAbsolutePath() + "/" + new Date().getTime() + ".mobileprovision";
                                        IoHandler.fileWriteTxt(filePro, data);
                                        break;
                                    }
                                }
                            }
                            //如果pro文件创建成功
                            if(filePro != null){
                                //包名
                                String nameIpa = new Date().getTime() + ".ipa";
                                //临时目录
                                String temp = new File("/sign/mode/temp").getAbsolutePath() + "/" + nameIpa;
                                String cmd = "zsign -k " + appleIis1.getP12() + " -p 123456 -m " + filePro + " -o " + temp + " -z 9 " + distribute.getIpa();
                                packStatusDao.updateStatus("正在签名", uuid);
                                log.info("签名结果" + RuntimeExec.runtimeExec(cmd).get("status").toString());
                                log.info("签名命令" + cmd);
                                log.info("包名"+ nameIpa);
                                //获取plist
                                String plist = IoHandler.readTxt(new File("/sign/mode/install.plist").getAbsolutePath());
                                packStatusDao.updateStatus("准备下载", uuid);

                                //是否使用七牛云
                                if(this.accessKey.equals("")){
                                    plist = plist.replace("urlRep", url  + nameIpa);
                                }else {
                                    plist = plist.replace("urlRep", this.url + uploadQly(temp));
                                }

                                plist = plist.replace("bundleRep", distribute.getPageName());
                                plist = plist.replace("versionRep", distribute.getVersion());
                                String iconPath = url + distribute.getAccount() + "/distribute/" + id + "/" + id + ".png";
                                plist = plist.replace("iconRep", iconPath);
                                plist = plist.replace("appnameRep", distribute.getAppName());
                                String plistName = new Date().getTime() + ".plist";
                                IoHandler.writeTxt(new File("/sign/mode/temp").getAbsolutePath() + "/" + plistName, plist);
                                String plistUrl = "itms-services://?action=download-manifest&url=" +  url + plistName;
                                packStatusDao.update(new PackStatus(null, distribute.getAccount(), distribute.getPageName(), null, null, appleIis1.getIis(), null, nameIpa,plistUrl , "点击下载", null,null,null), uuid);
                                log.info("打包完成");
                                log.info("plist名" + plistName);
                                isSuccess = 0;
                                return plistName;
                            }else {
                                log.info("创建配置文件失败");
                                //  appleIisDao.updateStatus(0, appleApiUtil.getIis());
                            }
                        }
                    }else {
                        log.info("初始化失败");
                        appleIisDao.updateStatus(0, appleApiUtil.getIis());
                    }
                }
                if(isSuccess == 1){
                    packStatusDao.updateStatus("没有可用的证书", uuid);
                }
            }else {
                packStatusDao.updateStatus("没有可用的证书", uuid);
                throw  new RuntimeException("没有可用的证书");
            }
        }catch (Exception e){
            log.info(e.toString());
            throw  new RuntimeException("失败" + e.getMessage());
        }
        return null;
    }


    @Override
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

    @Override
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
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
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

}
