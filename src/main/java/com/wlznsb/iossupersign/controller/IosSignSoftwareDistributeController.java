package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.annotation.PxCheckLogin;
import com.wlznsb.iossupersign.mapper.*;
import com.wlznsb.iossupersign.entity.*;
import com.wlznsb.iossupersign.service.DistrbuteServiceImpl;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import com.wlznsb.iossupersign.util.*;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/IosSignSoftwareDistribute")
@Validated
@Slf4j
@CrossOrigin(allowCredentials="true")
@PxCheckLogin
public class IosSignSoftwareDistributeController {

    @Autowired
    private IosSignSoftwareDistributeDao iosSignSoftwareDistributeDao;



    @Value("${iosSignSofware}")
    private Integer iosSignSofware;

    @Autowired
    private SoftwareDistributeDao softwareDistributeDao;

    @Autowired
    private IosSignUdidCertDao iosSignUdidCertDao;


    @Autowired
    private UserDao userDao;

    @Autowired
    private DistrbuteServiceImpl distrbuteService;

    @Autowired
    private UserServiceImpl userService;

    //上传ipa
    @RequestMapping(value = "/uploadIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestHeader String token,@RequestParam(value = "ipa") MultipartFile ipa,
                                        @RequestParam(value = "certId") String certId,
                                        @RequestParam(value = "introduce") String introduce,
                                        HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        String uuid = MyUtil.getUuid();
        String mkdir = "./sign/temp/" + user.getAccount() + "/ios_sign_software/app/" + uuid + "/";
        new File(mkdir).mkdirs();
        //icon路径
        String iconPath = new File(mkdir + uuid + ".png").getAbsolutePath();
        //iconUrl
        String iconUrl = rootUrl + user.getAccount() + "/ios_sign_software/app/" + uuid + "/" + uuid + ".png";
        //ipa路径
        String ipaPath = new File(mkdir + uuid + ".ipa").getAbsolutePath();
        //python
        String pyPath = new File(mkdir).getAbsolutePath();
        //写出
        ipa.transferTo(new File(ipaPath));
        //读取信息
        Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaPath,iconPath);
        if(mapIpa.get("code") != null){
            new File(ipaPath).delete();
            new File(mkdir).delete();
            throw new RuntimeException("无法读取包信息");
        }
        try {
            System.out.println(userDao.addCount(user.getAccount(), -this.iosSignSofware));;
        }catch (Exception e){
            new File(ipaPath).delete();
            new File(mkdir + uuid).delete();
            throw  new RuntimeException("共有池不足,IOS分发自助需要扣除共有池" + this.iosSignSofware + "台");
        }

        String name = mapIpa.get("displayName").toString();

        String url = rootUrl + "IosSignSoftwareDistribute/down/"  + uuid;
        IosSignSoftwareDistribute iosSignSoftwareDistribute = new IosSignSoftwareDistribute(uuid,user.getAccount(),name,mapIpa.get("package").
                toString(),mapIpa.get("versionName").toString(),
                iconUrl,ipaPath,null,url,certId,new Date(),introduce,1);
        iosSignSoftwareDistributeDao.add(iosSignSoftwareDistribute);
        //备份当前目录
        //备份当前目录
        MyUtil.getIpaImg(iconPath,iconPath);

        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }

    //上传证书
    @RequestMapping(value = "/uploadCert",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestHeader String token,@RequestParam(value = "mobileprovision")
                                                MultipartFile mobileprovision,
                                        @RequestParam(value = "p12") MultipartFile p12,
                                        @RequestParam(value = "password") String password,
                                        @RequestParam(value = "introduce") String introduce,
                                        @RequestParam(value = "udid") String udid,
                                        HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        String uuid = MyUtil.getUuid();
        String mkdir = "./sign/temp/" + user.getAccount() + "/ios_sign_software/cert/" + uuid + "/";
        new File(mkdir).mkdirs();
        //写入mobileprovision
        String mobileprovisionPath = new File(mkdir + uuid + ".mobileprovision").getAbsolutePath();
        mobileprovision.transferTo(new File(mobileprovisionPath));

        //写入p12
        String p12Path = new File(mkdir + uuid + ".p12").getAbsolutePath();
        p12.transferTo(new File(p12Path));

        IosSignUdidCert iosSignUdidCert = new IosSignUdidCert(uuid,user.getAccount(),p12Path,mobileprovisionPath,password,udid,introduce,new Date());
        iosSignUdidCertDao.add(iosSignUdidCert);

        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }

    @RequestMapping(value = "/queryAllCert",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAllCert(@RequestHeader String token,@RequestParam Integer pageNum,@RequestParam  Integer pageSize,
                                        HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        PageHelper.startPage(pageNum,pageSize);
        Page page = (Page) iosSignUdidCertDao.queryAccountAll(user.getAccount());
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

    @RequestMapping(value = "/queryAllIpa",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAllIpa(@RequestHeader String token,@RequestParam Integer pageNum,@RequestParam  Integer pageSize,
                                           HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        PageHelper.startPage(pageNum,pageSize);
        Page page = (Page) iosSignSoftwareDistributeDao.queryAccountAll(user.getAccount());
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }


    //修改证书
    @RequestMapping(value = "/updateIpaCertId",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateIpaCertId(@RequestHeader String token,@RequestParam(value = "certId") String certId,@RequestParam(value = "iosId") String iosId,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        iosSignSoftwareDistributeDao.updateCert(certId,iosId);
        map.put("code", 0);
        map.put("message", "更换成功");
        return map;
    }

    @RequestMapping(value = "/deleteIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteIpa(@RequestHeader String token,@RequestParam(value = "iosId") String iosId,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        Integer count =  iosSignSoftwareDistributeDao.delete(iosId,user.getAccount());
        if (count != 0) {
            File file = new File("./sign/temp/" + user.getAccount() + "/ios_sign_software/app/" + iosId ).getAbsoluteFile();
            FileSystemUtils.deleteRecursively(file);
        }
        map.put("code", 0);
        map.put("message", "删除成功");
        return map;
    }

    @RequestMapping(value = "/updateIpaAutoPageName",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateIpaAutoPageName(@RequestHeader String token,@RequestParam(value = "iosId") String iosId, @RequestParam(value = "status") @Range(max = 1,min = 0)   Integer status, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        iosSignSoftwareDistributeDao.updateAutoPageName(status,iosId,user.getAccount());

        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

    @RequestMapping(value = "/deleteCert",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteCert(@RequestHeader String token,@RequestParam(value = "certId") String certId,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        Integer count =  iosSignUdidCertDao.delete(certId,user.getAccount());
        if (count != 0) {
            File file = new File("./sign/temp/" + user.getAccount() + "/ios_sign_software/cert/" + certId ).getAbsoluteFile();
            FileSystemUtils.deleteRecursively(file);
        }
        map.put("code", 0);
        map.put("message", "删除成功");
        return map;
    }


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IosSignSoftwareDistributeStatusDao distributeStatusDao;
    //下载页面
    @RequestMapping(value = "/down/{uuid}",method = RequestMethod.GET)
    @PxCheckLogin(value = false)
    public String down(Model model,HttpServletRequest request, @PathVariable String uuid) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        IosSignSoftwareDistribute iosSignSoftwareDistribute =  iosSignSoftwareDistributeDao.query(uuid);
        IosSignUdidCert iosSignUdidCert = iosSignUdidCertDao.query(iosSignSoftwareDistribute.getCertId());
        log.info("uuid" + uuid + iosSignSoftwareDistribute);
        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        if(iosSignSoftwareDistribute == null){
            throw  new RuntimeException("应用不存在");
        }else {
            String packUuid = MyUtil.getUuid();
            IosSignSoftwareDistributeStatus ios = new IosSignSoftwareDistributeStatus(packUuid,iosSignUdidCert.getAccount(),
                    iosSignSoftwareDistribute.getIosId(),iosSignUdidCert.getCertId(),iosSignSoftwareDistribute.getAppName(),
                    iosSignSoftwareDistribute.getPageName(),
                    iosSignSoftwareDistribute.getVersion(),"","排队中",new Date(),new Date());
            distributeStatusDao.add(ios);
            log.info("应用存在");
//            if(iosSignSoftwareDistribute.getApk() == null){
//                iosSignSoftwareDistribute.setApk("no");
//            }
//            String plist = IoHandler.readTxt(new File("./sign/mode/install.plist").getAbsolutePath());
//            //bundle要随机不然有时候没进度条
//            plist = plist.replace("bundleRep", uuid);
//            plist = plist.replace("versionRep", iosSignSoftwareDistribute.getVersion());
//            plist = plist.replace("iconRep", iosSignUdidCert.getP12Path());
//            plist = plist.replace("appnameRep",iosSignSoftwareDistribute.getAppName());
//            //对ipa签名
//            String uuidTemp = MyUtil.getUuid();
//            String signPath = "./sign/mode/temp/" + uuidTemp +".ipa";
//            String cmd = "./sign/mode/zsign -k " + iosSignUdidCert.getP12Path() + " -p " + iosSignUdidCert.getP12Password() + " -m " + iosSignUdidCert.getMobileprovisionPath() + " -o " + signPath + " -z 1 " + iosSignSoftwareDistribute.getIpa();
//
//            if(iosSignSoftwareDistribute.getAutoPageName() == 1){
//                log.info("随机包名");
//                cmd = "./sign/mode/zsign -k " + iosSignUdidCert.getP12Path() + " -p " + iosSignUdidCert.getP12Password() + " -m " + iosSignUdidCert.getMobileprovisionPath() + " -o " + signPath + " -z 1 " + iosSignSoftwareDistribute.getIpa() + " -b " + new Date().getTime();
//            }
//            log.info("开始签名" + cmd);
//            Map<String,Object>  map1 =  RuntimeExec.runtimeExec(cmd);
//            log.info("签名结果" + map1.get("status").toString());
//            log.info("签名反馈" + map1.get("info").toString());
//            log.info("签名命令" + cmd);
//            //上传云端
//            String ipaUrl = distrbuteService.uploadSoftwareIpa(signPath);
//            if(null == ipaUrl){
//                ipaUrl = rootUrl + uuidTemp + ".ipa";
//            }
//            plist = plist.replace("urlRep", ipaUrl);
//            String plistName = uuidTemp + ".plist";
//            IoHandler.writeTxt(new File("./sign/mode/temp/" +  plistName).getAbsolutePath(), plist);
//            String plistUrl = "itms-services://?action=download-manifest&url=" +  rootUrl + plistName;
            iosSignSoftwareDistribute.setUrl("");

            model.addAttribute("uuid", packUuid);
            model.addAttribute("downUrl", rootUrl + "IosSignSoftwareDistribute/down/status/" + packUuid);
            model.addAttribute("softwareDistribute", iosSignSoftwareDistribute);
        }
        return "IosSignSoftwareDown";
    }


    //查询打包状态
    @RequestMapping(value = "/down/status/{uuid}",method = RequestMethod.GET)
    @ResponseBody
    @PxCheckLogin(value = false)
    public Map<String,Object> downStatus(@PathVariable String uuid,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        IosSignSoftwareDistributeStatus ios =  distributeStatusDao.query(uuid);
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", ios);
        return map;
    }

    //更新ipa
    @RequestMapping(value = "/updateIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestHeader String token,@RequestParam MultipartFile ipa,@RequestParam String uuid,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        String ipaPath = new File("./sign/mode/software/" + uuid + "/" + uuid + ".ipa").getAbsolutePath();
        ipa.transferTo(new File(ipaPath));
        String  ipaUrl = distrbuteService.uploadSoftwareIpa(ipaPath);
        //不等于null说明需要修改plist文件
        if(ipaUrl != null){
            log.info("需要修改plist");
            File plistFile = new File("./sign/mode/software/" + uuid + "/" + uuid + ".plist");
            InputStream is = new FileInputStream(plistFile);
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
            String json =  org.json.XML.toJSONObject(content).toString();
            log.info(json + "plist");
            String url = new ObjectMapper().readTree(json).get("plist").get("dict").get("array").get("dict").get("array").get("dict").get(0).get("string").get(1).asText();
            String plist = IoHandler.readTxt(plistFile.getAbsolutePath());
            plist.replace(url, ipaUrl);
            IoHandler.writeTxt(plistFile.getAbsolutePath(), plist);
        }
        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }

    //上传apk也可以更新
    @RequestMapping(value = "/uploadIntroduce",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIntroduce(@RequestHeader String token,@RequestParam @Length(max = 200,message = "最多200个字符") String introduce, @RequestParam String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        softwareDistributeDao.updateIntroduce(introduce,uuid,user.getAccount());
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

    //查询分发记录
    @RequestMapping(value = "/queryAll",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAll(@RequestHeader String token,HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);

        PageHelper.startPage(pageNum,pageSize);
        Page<User> page;

        //管理查询所有
        if(user.getType() == 1){
            page =  (Page) softwareDistributeDao.querAll();

        }else {
            page =  (Page) softwareDistributeDao.queryAccountAll(user.getAccount());
        }
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

    //查询单点分发记录
    @RequestMapping(value = "/pack/queryAll",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> pack(@RequestHeader String token,HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);

        PageHelper.startPage(pageNum,pageSize);
        Page<User> page;

        //管理查询所有
        if(user.getType() == 1){
            page =  (Page) distributeStatusDao.queryAll();

        }else {
            page =  (Page) distributeStatusDao.queryAccountAll(user.getAccount());
        }
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

    /**
     * 删除分发
     * @param uuid
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delete(@RequestHeader String token,@RequestParam  String uuid,HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        Integer res;
        if(user.getType() == 1){
            res  = softwareDistributeDao.adminDelete(uuid);
        }else {
            res = softwareDistributeDao.delete(user.getAccount(),uuid);
        }
        if(res == 1){
            //清空目录
            MyUtil.deleteDir("./sign/mode/software/" + uuid);
            new File("./sign/mode/software/" + uuid).delete();
            map.put("code", 0);
            map.put("message", "删除成功");
        }
        return map;
    }
}


