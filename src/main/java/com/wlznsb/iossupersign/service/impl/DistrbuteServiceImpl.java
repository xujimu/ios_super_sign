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
import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.entity.Distribute;
import com.wlznsb.iossupersign.entity.PackStatus;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.DistrbuteService;
import com.wlznsb.iossupersign.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Distribute uploadIpa(MultipartFile ipa, HttpServletRequest request) {
        Integer id = null;
        try {
            if(ipa.getSize() != 0){
                User user = (User)request.getSession().getAttribute("user");
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
                //域名路径
                String rootUrl = ServerUtil.getRootUrl(request);
                String name = mapIpa.get("displayName").toString();
                String url = "{\"data\": {\"id\": idRep,\"account\": \"accountRep\",\"name\": \"nameRep\",\"size\": \"sizeRep\",\"icon\" : \"iconRep\"}};";
                url = url.replace("idRep", id.toString());
                url = url.replace("nameRep", name);
                url = url.replace("sizeRep", mapIpa.get("size").toString());
                url = url.replace("iconRep", rootUrl+ user.getAccount() + "/distribute/" + id  + "/" + id + ".png");
                url = url.replace("accountRep", user.getAccount());
                url = rootUrl + "distribute/down/" +Base64.getEncoder().encodeToString(url.getBytes());
                Distribute distribute = new Distribute(id,user.getAccount(),name,mapIpa.get("package").
                        toString(),mapIpa.get("versionName").toString(),iconPath,ipaPath,null,url,new Date());
                //写入数据库
                distributeDao.add(distribute);
                return distribute;
            }else {
                throw new RuntimeException("请不要上传空包");
            }
        }catch (Exception e){
            log.info(e.toString());
            throw  new RuntimeException("上传失败:" + e.getMessage());
        }
    }








    /**
     * 这里不能加事务否则状态无法读取
     * @param id
     * @param uuidk
     * @param request
     * @param response
     * @return
     */
    @Override
    public String getUuid(int id,String uuidk,HttpServletRequest request, HttpServletResponse response){
        try {
            //获取当前项目域名
            log.info("1");
            StringBuffer url = request.getRequestURL();
            log.info("2");
            String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getSession().getServletContext().getContextPath()).append("/").toString();
            log.info(tempContextUrl);
            //获取HTTP请求的输入流
            InputStream is = request.getInputStream();
            //已HTTP请求输入流建立一个BufferedReader对象
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            //读取HTTP请求内容
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }

            //去除xml多余信息
            String content = sb.toString().substring(sb.toString().indexOf("<?xml"), sb.toString().indexOf("</plist>")+8);
            //获取到uuid
            String json =  org.json.XML.toJSONObject(content).toString();
            String uuid = new ObjectMapper().readTree(json).get("plist").get("dict").get("string").get(3).asText();
            //创建状态
            PackStatus packStatus = new PackStatus(null, null, null, uuidk, uuid, null, new Date(), null, null, "请求下载", 1);
            packStatusDao.add(packStatus);
            //查询这个应用对应的账号
            Distribute distribute = distributeDao.query(id);
            //查询账号所有可用的证书
            List<AppleIis> appleIislist = appleIisDao.queryUsIis(distribute.getAccount());

            //如果有证书
            if(appleIislist.size() != 0){
                log.info("开始遍历证书");
                packStatusDao.updateStatus("正在匹配证书", uuidk);
                for (AppleIis appleIis1:appleIislist){
                    AppleApiUtil appleApiUtil = new AppleApiUtil(appleIis1.getIis(),
                            appleIis1.getKid(),appleIis1.getP8());
                    //如果初始化失败就把状态设置成失效
                    log.info("开始初始化");
                    if(appleApiUtil.init()){
                        packStatusDao.updateStatus("正在添加设备", uuidk);
                        log.info("初始化完毕");
                        //添加id后返回的唯一id
                        String addUuid = null;
                        //查询所有设备
                        String devices = appleApiUtil.queryDevices();
                        //获取剩余设备数
                        int deviceCount = 100 - new ObjectMapper().readTree(devices).get("meta").get("paging").get("total").asInt();
                        //判断是否有这个设备
                        int isAdd = devices.indexOf(uuid);
                        if(isAdd == -1){
                             addUuid = appleApiUtil.addUuid(uuid);
                            appleIisDao.updateCount(deviceCount -1,appleIis1.getIis());
                        }else {
                           Integer count = new ObjectMapper().readTree(devices).get("meta").get("paging").get("total").asInt();
                           //找出id
                           for (int i = 0; i < count; i++) {
                                String udid = new ObjectMapper().readTree(devices).get("data").get(i).get("attributes").get("udid").asText();
                                if(udid.equals(uuid)){
                                    new ObjectMapper().readTree(devices).get("data").get(i).get("id").asText();
                                    addUuid = new ObjectMapper().readTree(devices).get("data").get(i).get("id").asText();
                                    System.out.println(deviceCount);
                                    break;
                                }
                            }
                        }
                        //判断是否添加成功
                        if(addUuid != null){
                            packStatusDao.updateStatus("注册配置文件", uuidk);
                            //获取pros
                            String profiles = appleApiUtil.queryProfiles();
                            String filePro = null;
                            //判断有没有注册过
                            if(profiles.indexOf(addUuid) == -1){
                                 filePro = appleApiUtil.addProfiles(appleIis1.getIdentifier(),appleIis1.getCertId(), addUuid, addUuid,new File("/sign/mode/temp").getAbsolutePath());
                            }else {
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
                                log.info("签名命令" + cmd);
                                packStatusDao.updateStatus("正在签名", uuidk);
                                log.info("签名结果" + RuntimeExec.runtimeExec(cmd).get("status").toString());
                                log.info("包名"+ nameIpa);
                                //获取plist
                                String plist = IoHandler.readTxt(new File("/sign/mode/install.plist").getAbsolutePath());
                                packStatusDao.updateStatus("准备下载", uuidk);

                                //是否使用七牛云
                                if(this.accessKey.equals("")){
                                    plist = plist.replace("urlRep", tempContextUrl + nameIpa);
                                }else {
                                    plist = plist.replace("urlRep", this.url + uploadQly(temp));
                                }

                                plist = plist.replace("bundleRep", distribute.getPageName());
                                plist = plist.replace("versionRep", distribute.getVersion());
                                String iconPath = tempContextUrl + distribute.getAccount() + "/distribute/" + id + "/" + id + ".png";
                                plist = plist.replace("iconRep", iconPath);
                                plist = plist.replace("appnameRep", distribute.getAppName());
                                String plistName = new Date().getTime() + ".plist";
                                IoHandler.writeTxt(new File("/sign/mode/temp").getAbsolutePath() + "/" + plistName, plist);
                                String plistUrl = "itms-services://?action=download-manifest&url=" +  tempContextUrl + plistName;
                                packStatusDao.update(new PackStatus(null, distribute.getAccount(), distribute.getPageName(), null, null, appleIis1.getIis(), null, nameIpa,plistUrl , "点击下载", null), uuidk);
                                log.info("打包完成");
                                log.info("plist名" + plistName);
                                return plistName;
                            }else {
                                log.info("创建配置文件失败");
                                appleIisDao.updateStatus(0, appleApiUtil.getIis());
                            }
                        }
                    }else {
                        log.info("添加设备失败");
                        appleIisDao.updateStatus(0, appleApiUtil.getIis());
                    }
                }
            }else {
                packStatusDao.updateStatus("没有可用的证书", uuidk);
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
        String accessKey = "EwSEYiKZtViB3YiqVQR8Y-Go4vijLhhY3WxIJxCz";
        String secretKey = "ToI-iR-Dhq-udEwIrZEhauqJCpbX6vrl-yk4ZXuh";
        //控件名
        String bucket = "abcqweqwedq";
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
            return null;
        }
    }

}
