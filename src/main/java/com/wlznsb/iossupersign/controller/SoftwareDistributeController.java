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
import jnr.posix.POSIXFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/softwareDistribute")
@Validated
@Slf4j
@CrossOrigin(allowCredentials="true")
@PxCheckLogin
public class SoftwareDistributeController {

    @Autowired
    private SoftwareDistributeDao softwareDistributeDao;

    @Autowired
    private DistributeDao distributeDao;

    @Value("${sofware}")
    private Integer sofware;


    @Autowired
    private UserDao userDao;


    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DistrbuteServiceImpl distrbuteService;

    //上传ipa
    @RequestMapping(value = "/uploadIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestHeader String token,@RequestParam MultipartFile ipa,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        //域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        String uuid = MyUtil.getUuid();
        //创建目录
        new File("./sign/mode/software/" + uuid).mkdirs();
        //icon路径
        String iconPath = new File("./sign/mode/software/" + uuid + "/" + uuid + ".png").getAbsolutePath();
        //iconUrl
        String iconUrl = rootUrl + uuid + "/" + uuid + ".png";
        //ipa路径
        String ipaPath = new File("./sign/mode/software/" + uuid + "/" + uuid + ".ipa").getAbsolutePath();
        //python就来
        String pyPath = new File("./sign/mode/software/" + uuid + "/").getAbsolutePath();
        //写出
        ipa.transferTo(new File(ipaPath));
        //读取信息
        Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaPath,iconPath);
        if(mapIpa.get("code") != null){
            new File(ipaPath).delete();
            new File("./sign/mode/software/" + uuid).delete();
            throw new RuntimeException("无法读取包信息");
        }
        try {
            System.out.println(userDao.addCount(user.getAccount(), -this.sofware));;
        }catch (Exception e){
            new File(ipaPath).delete();
            new File("./sign/mode/software/" + uuid).delete();
            throw  new RuntimeException("共有池不足,自助需要扣除共有池" + this.sofware + "台");
        }
        String ipaUrl =  distrbuteService.uploadSoftwareIpa(ipaPath);
        //返回null说明没使用云储存
        if(ipaUrl == null){
            ipaUrl = rootUrl + uuid + "/" + uuid + ".ipa";
        }
        String name = mapIpa.get("displayName").toString();
        //获取plist
        String plist = IoHandler.readTxt(new File("./sign/mode/install.plist").getAbsolutePath());
        //bundle要随机不然有时候没进度条
        plist = plist.replace("bundleRep", uuid);
        plist = plist.replace("versionRep", mapIpa.get("versionName").toString());
        plist = plist.replace("iconRep", iconPath);
        plist = plist.replace("appnameRep",name);
        plist = plist.replace("urlRep", ipaUrl);
        String plistName = uuid + ".plist";
        IoHandler.writeTxt(new File("./sign/mode/software/" + uuid + "/" + plistName).getAbsolutePath(), plist);
        String plistUrl = "itms-services://?action=download-manifest&url=" +  rootUrl + uuid + "/" + plistName;
        log.info("ipaurl路径" + ipaUrl);
        String url = rootUrl + "softwareDistribute/down/"  + uuid;
        SoftwareDistribute softwareDistribute = new SoftwareDistribute(null,user.getAccount(),name,mapIpa.get("package").
                toString(),mapIpa.get("versionName").toString(),iconUrl,plistUrl,null,url,new Date(),"极速下载",uuid,"zh");
        softwareDistributeDao.add(softwareDistribute);
        //备份当前目录
        MyUtil.getIpaImg("./sign/mode/software/" + uuid  + "/" + uuid +  ".png","./sign/mode/software/" + uuid  + "/" + uuid +  ".png");

        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }




    //下载页面
    @RequestMapping(value = "/down/{uuid}",method = RequestMethod.GET)
    @PxCheckLogin(value = false)
    public String down(Model model,HttpServletRequest request, @PathVariable String uuid) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        SoftwareDistribute softwareDistribute =  softwareDistributeDao.queryUuid(uuid);
        log.info("uuid" + uuid + softwareDistribute);
        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        if(softwareDistribute == null){
            throw  new RuntimeException("应用不存在");
        }else {
            log.info("应用存在");
            if(softwareDistribute.getApk() == null){
                softwareDistribute.setApk("no");
            }
            softwareDistribute.setIcon(rootUrl  + uuid + "/" + uuid + ".png");
            model.addAttribute("pro", rootUrl + "app.mobileprovision");
            model.addAttribute("softwareDistribute", softwareDistribute);
        }
        return "softwareDown";
    }



    //上传apk也可以更新
    @RequestMapping(value = "/uploadApk",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadApk(@RequestHeader String token,@RequestParam MultipartFile apk,@RequestParam String uuid,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        String apkPath = new File("./sign/mode/software/" + uuid + "/" + uuid + ".apk").getAbsolutePath();
        apk.transferTo(new File(apkPath));
        String apkUrl = distrbuteService.uploadSoftwareApk(apkPath);
        if(apkUrl == null){
            apkUrl = rootUrl + uuid + "/" + uuid + ".apk";
        }
        softwareDistributeDao.uploadApk(apkUrl,uuid,user.getAccount());
        map.put("code", 0);
        map.put("message", "上传成功");
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


    @RequestMapping(value = "/updateLanguage",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadLanguage(@RequestHeader String token,@RequestParam @Length(max = 200,message = "最多200个字符") String language, @RequestParam String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        softwareDistributeDao.updateLanguage(language,uuid,user.getAccount());
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


