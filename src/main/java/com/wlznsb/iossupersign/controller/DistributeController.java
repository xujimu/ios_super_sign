package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.dao.DistributeDao;
import com.wlznsb.iossupersign.dao.PackStatusDao;
import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.entity.Distribute;
import com.wlznsb.iossupersign.entity.PackStatus;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.DistrbuteService;
import com.wlznsb.iossupersign.util.IoHandler;
import com.wlznsb.iossupersign.util.RuntimeExec;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.util.*;


@Controller
@RequestMapping(value = "/distribute")
@Validated
@Slf4j
@CrossOrigin(allowCredentials="true")
public class DistributeController {


    @Autowired
    private DistrbuteService distrbuteService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DistributeDao distributeDao;

    @Autowired
    private PackStatusDao packStatusDao;

    //下载页面,没有使用业务层
    @RequestMapping(value = "/down/{id}",method = RequestMethod.GET)
    public String getDown(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) throws JsonProcessingException, UnsupportedEncodingException {
        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        log.info("当前id" + id);
        Distribute distribute = distributeDao.query(id);
        distribute.setIcon(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".png");
        distribute.setApk(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".apk");
        distribute.setIpa(rootUrl + "/distribute/" +"getMobile?id=" + id + "&name=" + distribute.getAppName());
        model.addAttribute("distribute", distribute);
        model.addAttribute("pro", rootUrl + "app.mobileprovision");
        //设置轮播图
        if(null == distribute.getImages()){
            model.addAttribute("img1", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img2", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img3", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img4", rootUrl + "/images/" + "slideshow.png");
        }else {
            model.addAttribute("img1", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img1.png");
            model.addAttribute("img2", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img2.png");
            model.addAttribute("img3", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img3.png");
            model.addAttribute("img4", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img4.png");
        }
        return "down";
    }

    //获取描述文件,没有使用业务层
    @GetMapping
    @RequestMapping("/getMobile")
    public void getMobile(HttpServletRequest request, HttpServletResponse response, @RequestParam String id,@RequestParam String name) throws IOException {
        //域名
        String tempContextUrl = ServerUtil.getRootUrl(request);
        String keyPath = new File("/sign/mode/cert/key.key").getAbsolutePath();
        String rootPath = new File("/sign/mode/cert/root.crt").getAbsolutePath();
        String serverPath = new File("/sign/mode/cert/server.crt").getAbsolutePath();;
        //模板
        String moblicPath =new File("/sign/mode/udid.mobileconfig").getAbsolutePath();
        //随机
        Long round = new Date().getTime();
        //未签名
        String moblicNoSignPath = new File("/sign/mode/temp/" + round + "no.mobileconfig").getAbsolutePath();
        String temp = IoHandler.readTxt(moblicPath);
        temp = temp.replace("urlRep", tempContextUrl + "distribute/getUdid?id=" + id);
        temp = temp.replace("nameRep",name + " -- 点击右上角安装");
        IoHandler.writeTxt(moblicNoSignPath, temp);
        //已签名
        String moblicSignPath = new File("/sign/mode/temp/" + round + ".mobileconfig").getAbsolutePath();
        String cmd =" openssl smime -sign -in " + moblicNoSignPath + " -out " + moblicSignPath + " -signer " + serverPath + " -inkey " + keyPath + " -certfile " + rootPath + " -outform der -nodetach ";
        RuntimeExec.runtimeExec(cmd);
        log.info(tempContextUrl);
        response.sendRedirect(tempContextUrl + round + ".mobileconfig");
    }

    //301回调
    @RequestMapping(value = "/getUdid")
    public void getUdid(@RequestParam int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建打包uuid
        String uuid = ServerUtil.getUuid();
/**
 * 这里的返回值是pist没用上
 */
        StringBuffer url = request.getRequestURL();
        //获取项目路径域名
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getSession().getServletContext().getContextPath()).append("/").toString();
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
        String json =  org.json.XML.toJSONObject(content).toString();
        String udid = new ObjectMapper().readTree(json).get("plist").get("dict").get("string").get(3).asText();
        log.info(content);
        if(null != udid && !udid.equals("")){
            //创建状态
            PackStatus packStatus = new PackStatus(null, null, null, uuid, udid, null, new Date(), null, null, "排队中", 1,id,tempContextUrl);
            packStatusDao.add(packStatus);
            //获取原来的分发地址
            Distribute distribute = distributeDao.query(id);
            String skipUrl = distribute.getUrl().replace("down", "downStatus");
            //再次请求带上uuid
            response.setHeader("Location", skipUrl + "/" + packStatus.getId());
            log.info("statusid" + packStatus.getId());
            response.setStatus(301);
        }

    }

    /**
     * 获取下载状态,没有使用业务层
     * @param model
     * @param request
     * @param response
     * @param id
     * @param
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/downStatus/{id}/{statusId}",method = RequestMethod.GET)
    public String getDownStatus(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String id, @PathVariable String statusId) throws JsonProcessingException, UnsupportedEncodingException {
        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        Distribute distribute = distributeDao.query(Integer.valueOf(id));
        distribute.setIcon(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".png");
        distribute.setApk(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".apk");
        distribute.setIpa(rootUrl + "/distribute/" +"getMobile?id=" + id + "&name=" + distribute.getAppName());
        model.addAttribute("distribute", distribute);
        model.addAttribute("statusId", statusId);
        model.addAttribute("pro", rootUrl + "app.mobileprovision");
        model.addAttribute("downUrl", rootUrl + "/distribute/getStatus?statusId=");
        //设置轮播图
        if(null == distribute.getImages()){
            model.addAttribute("img1", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img2", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img3", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img4", rootUrl + "/images/" + "slideshow.png");
        }else {
            model.addAttribute("img1", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img1.png");
            model.addAttribute("img2", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img2.png");
            model.addAttribute("img3", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img3.png");
            model.addAttribute("img4", rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" + "img4.png");
        }
        return "downStatus";
    }



    //查询打包状态,没有使用业务层
    @RequestMapping(value = "/getStatus")
    @ResponseBody
    public Map<String,Object> getStatus(String statusId,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        PackStatus packStatus =  packStatusDao.query(statusId);
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", packStatus);
        return map;
    }

    //上传ipa
    @RequestMapping(value = "/uploadIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestParam MultipartFile ipa, HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
//域名路径
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = (User)request.getSession().getAttribute("user");
        Distribute distribute = distrbuteService.uploadIpa(ipa, user,rootUrl);
        map.put("code", 0);
        map.put("message", "上传成功");
        map.put("data", distribute);
        return map;
    }


    //上传apk
    @RequestMapping(value = "/uploadApk",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadApk(@RequestParam MultipartFile apk,@RequestParam int id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        distrbuteService.uploadApk(apk,user,id);
        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }


    //删除ipa,没有使用业务层
    @RequestMapping(value = "/deleIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleIpa(@RequestParam  int id,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        distrbuteService.dele(user.getAccount(),id);
        map.put("code", 0);
        map.put("message", "删除成功");
        return map;
    }


    //查询ipa
    @RequestMapping(value = "/queryAccountAll",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAccountAll(HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        List<Distribute> distributeList =  distrbuteService.queryAccountAll(user.getAccount());
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", distributeList);
        return map;
    }

    //修改简介
    @RequestMapping(value = "/updateIntroduce",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateIntroduce(@RequestParam @NotEmpty String introduce, @RequestParam Integer id, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        distributeDao.updateIntroduce(introduce, user.getAccount(), id);
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

    //上传轮播图
    @RequestMapping(value = "/uploadImg",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateIntroduce(@RequestParam MultipartFile img1,@RequestParam MultipartFile img2,@RequestParam MultipartFile img3,MultipartFile img4, @RequestParam Integer id, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        String path = new File("/sign/temp/" + user.getAccount() + "/distribute/" + id + "/img").getAbsolutePath();
        //这里没做非空判断
        img1.transferTo(new File(path + "1.png"));
        img2.transferTo(new File(path + "2.png"));
        img3.transferTo(new File(path + "3.png"));
        img4.transferTo(new File(path + "4.png"));
        distributeDao.updateImages("已上传", user.getAccount(), id);
        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }

}


