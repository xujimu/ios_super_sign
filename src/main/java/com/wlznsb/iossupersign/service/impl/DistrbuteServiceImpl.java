package com.wlznsb.iossupersign.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                //域名路径
                String rootUrl = ServerUtil.getRootUrl(request);
                String name = mapIpa.get("displayName").toString();
                String url = "{\"data\": {\"id\": idRep,\"name\": \"nameRep\",\"size\": \"sizeRep\",\"icon\" : \"iconRep\"}};";
                url = url.replace("idRep", id.toString());
                url = url.replace("nameRep", name);
                url = url.replace("sizeRep", mapIpa.get("size").toString());
                url = url.replace("iconRep", rootUrl+ user.getAccount() + "/distribute/" + id  + "/" + id + ".png");
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
            e.printStackTrace();
            throw  new RuntimeException("上传失败" + e.getMessage());
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
            StringBuffer url = request.getRequestURL();
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
            PackStatus packStatus = new PackStatus(null, null, null, uuidk, uuid, null, new Date(), null, null, "正在打包", 1);
            packStatusDao.add(packStatus);
            //查询这个应用对应的账号
            Distribute distribute = distributeDao.query(id);
            //查询账号所有可用的证书
            List<AppleIis> appleIislist = appleIisDao.queryUsIis(distribute.getAccount());
            System.out.println(appleIislist);
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
                        //判断是否有这个设备
                        int isAdd = devices.indexOf(uuid);
                        if(isAdd == -1){
                             addUuid = appleApiUtil.addUuid(uuid);
                        }else {
                           int count = new ObjectMapper().readTree(devices).get("meta").get("paging").get("total").asInt();
                           //找出id
                           for (int i = 0; i < count; i++) {
                                String udid = new ObjectMapper().readTree(devices).get("data").get(i).get("attributes").get("udid").asText();
                                if(udid.equals(uuid)){
                                    new ObjectMapper().readTree(devices).get("data").get(i).get("id").asText();
                                    addUuid = new ObjectMapper().readTree(devices).get("data").get(i).get("id").asText();
                                    break;
                                }
                            }
                        }
                        //判断是否添加成功
                        if(addUuid != null){
                            packStatusDao.updateStatus("正在注册配置文件", uuidk);
                            //获取pros
                            String profiles =appleApiUtil.queryProfiles();
                            String filePro = null;
                            //判断有没有注册过
                            if(profiles.indexOf(addUuid) == -1){
                                 filePro = appleApiUtil.addProfiles(appleIis1.getIdentifier(),appleIis1.getCertId(), addUuid, addUuid,new File("/sign/mode/temp").getAbsolutePath());
                            }else {
                                int count =  new ObjectMapper().readTree(profiles).get("meta").get("paging").get("total").asInt();
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
                                packStatusDao.updateStatus("正在对ipa签名", uuidk);
                                log.info("签名结果" + RuntimeExec.runtimeExec(cmd).get("status").toString());
                                log.info("包名"+ nameIpa);
                                //获取plist
                                String plist = IoHandler.readTxt(new File("/sign/mode/install.plist").getAbsolutePath());
                                plist = plist.replace("urlRep", tempContextUrl + nameIpa);
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
            e.printStackTrace();
            throw  new RuntimeException("失败" + e.toString());
        }
        return null;
    }

}
