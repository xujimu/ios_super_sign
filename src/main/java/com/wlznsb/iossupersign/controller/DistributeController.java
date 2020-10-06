package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.dao.DistributeDao;
import com.wlznsb.iossupersign.dao.DownCodeDao;
import com.wlznsb.iossupersign.dao.PackStatusDao;
import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.entity.Distribute;
import com.wlznsb.iossupersign.entity.DownCode;
import com.wlznsb.iossupersign.entity.PackStatus;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.DistrbuteServiceImpl;
import com.wlznsb.iossupersign.util.IoHandler;
import com.wlznsb.iossupersign.util.IpUtils;
import com.wlznsb.iossupersign.util.RuntimeExec;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
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

    private Map<String,Integer> tempUuid = new HashMap<>();

    @Autowired
    private DistrbuteServiceImpl distrbuteService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DistributeDao distributeDao;

    @Autowired
    private PackStatusDao packStatusDao;

    @Autowired
    private DownCodeDao downCodeDao;

    //下载页面,没有使用业务层
    @RequestMapping(value = "/down/{base64Id}",method = RequestMethod.GET)
    public String getDown(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String base64Id) throws JsonProcessingException, UnsupportedEncodingException {
        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        Integer id = Integer.valueOf(new String(Base64.getDecoder().decode(base64Id.getBytes())));
        log.info("当前id" + id);
        Distribute distribute = distributeDao.query(id);
        if(distribute.getApk() != null){
            distribute.setApk(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".apk");
        }else {
            distribute.setApk("no");
        }
        distribute.setIcon(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".png");
        distribute.setIpa(rootUrl + "/distribute/" +"getMobile?id=" + id + "&name=" + distribute.getAppName());
        model.addAttribute("distribute", distribute);
        model.addAttribute("pro", rootUrl + "app.mobileprovision");
        model.addAttribute("downCode", distribute.getDownCode());
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
    @ResponseBody
    public Map<String,Object> getMobile(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id,@RequestParam String name,String downCode) throws IOException {
        Distribute distribute =  distributeDao.query(id);
        Map<String,Object> map = new HashMap<String, Object>();
        //如果该应用启用了下载码
        if(distribute.getDownCode() == 1){
            if(null != downCode && !"".equals(downCode)){
                    DownCode downCode1 = downCodeDao.queryAccountDownCode(distribute.getAccount(),downCode);
                    if(null != downCode1){
                        if(downCode1.getStatus() == 1){
                            downCodeDao.updateDownCodeStatus(distribute.getAccount(),downCode,new Date(), 0);
                            map.put("code",0);
                            map.put("message", "验证成功");
                        }else {
                            throw new RuntimeException("下载码已被使用");
                        }
                    }else {
                        throw new RuntimeException("下载码错误");
                    }
            }else {
                throw new RuntimeException("下载码不能为空");
            }
        }else {
            map.put("code",0);
            map.put("message", "验证成功");
        }
        //临时存放,保证每次描述文件url都是动态的
        String uuid = ServerUtil.getUuid();
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
        temp = temp.replace("urlRep", tempContextUrl + "distribute/getUdid?tempuuid=" + uuid);
        temp = temp.replace("nameRep",name + " -- 点击右上角安装");
        IoHandler.writeTxt(moblicNoSignPath, temp);
        //已签名
        String moblicSignPath = new File("/sign/mode/temp/" + round + ".mobileconfig").getAbsolutePath();
        String cmd =" openssl smime -sign -in " + moblicNoSignPath + " -out " + moblicSignPath + " -signer " + serverPath + " -inkey " + keyPath + " -certfile " + rootPath + " -outform der -nodetach ";
        RuntimeExec.runtimeExec(cmd);
        //写入map
        tempUuid.put(uuid, id);
        log.info(uuid);
        log.info(tempContextUrl);
        map.put("data", tempContextUrl + round + ".mobileconfig");
        return map;
    }

    //301回调
    @RequestMapping(value = "/getUdid")
    public void getUdid(@RequestParam String tempuuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = tempUuid.get(tempuuid);
        if(id != null){
            tempUuid.remove(tempuuid);
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
            log.info(json + "plist");
            String udid = new ObjectMapper().readTree(json).get("plist").get("dict").get("string").asText();
            if(null != udid && !udid.equals("")){
                //创建状态
                PackStatus packStatus = new PackStatus(null, null, null, uuid, udid, null,new Date(), null, null, "排队中", 1,id,tempContextUrl, IpUtils.getIpAddr(request));
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
    }
    /**
     * 获取下载状态,没有使用业务层
     * @param model
     * @param request
     * @param response
     * @param base64Id
     * @param
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/downStatus/{base64Id}/{statusId}",method = RequestMethod.GET)
    public String getDownStatus(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String base64Id, @PathVariable String statusId) throws JsonProcessingException, UnsupportedEncodingException {
        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        Integer id = Integer.valueOf(new String(Base64.getDecoder().decode(base64Id.getBytes())));
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
        distrbuteService.dele(user, id);
        map.put("code", 0);
        map.put("message", "删除成功");
        return map;
    }


    //查询ipa
    @RequestMapping(value = "/queryAccountAll",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAccountAll(HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        Page<User> page;
        //如果是管理员就查询所有
        if(user.getType() == 0){
            PageHelper.startPage(pageNum,pageSize);
            page = (Page) distrbuteService.queryAccountAll(user.getAccount());
        }else {
            PageHelper.startPage(pageNum,pageSize);
            page =  (Page) distrbuteService.queryAll();
        }
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
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

    //启用下载码
    @RequestMapping(value = "/updateDownCodeStatus",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateDownCodeStatus(@RequestParam Integer id, @RequestParam  @Range(max = 1,min = 0)  Integer status, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        distributeDao.updateDownCode(user.getAccount(), id, status);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }


    //修改下载码购买地址
    @RequestMapping(value = "/updateBuyDownCodeUrl",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateBuyDownCodeUrl(@RequestParam Integer id,@NotEmpty @RequestParam String url, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        distributeDao.updateBuyDownCodeUrl(user.getAccount(), id, url);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }

    //添加下载码
    @RequestMapping(value = "/addDownCode",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addDownCode(HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        DownCode downCode = new DownCode(null, user.getAccount(), ServerUtil.getUuid(), new Date(), null, 1);
        downCodeDao.addDownCode(downCode);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }

    //查询所有下载码
    @RequestMapping(value = "/queryAllDownCode",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAllDownCode(@RequestParam Integer pageNum,@RequestParam  Integer pageSize,HttpServletRequest request)  {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        PageHelper.startPage(pageNum,pageSize);
        Page page = (Page) downCodeDao.queryAccountAllDownCode(user.getAccount());
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

    //删除下载码
    @RequestMapping(value = "/deleDownCode",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleDownCode(@RequestParam Integer id,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        downCodeDao.deleDownCode(user.getAccount(), id);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }

}


