package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.dao.*;
import com.wlznsb.iossupersign.entity.*;
import com.wlznsb.iossupersign.service.DistrbuteServiceImpl;
import com.wlznsb.iossupersign.util.*;
import jnr.ffi.annotations.In;
import jnr.posix.POSIXFactory;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${apkCount}")
    private Integer apkCount;

    @Autowired
    private AppleIisDao appleIisDao;

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
            String time = Base64.getEncoder().encodeToString(Long.toString(new Date().getTime() * 1390).getBytes());
            time = Base64.getEncoder().encodeToString(time.getBytes());
            distribute.setApk(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".apk?token=" + time);
        }else {
            distribute.setApk("no");
        }
        distribute.setIcon(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".png");
        distribute.setIpa(rootUrl + "/distribute/" +"getMobile?id=" + id + "&name=" + distribute.getAppName());
        model.addAttribute("distribute", distribute);
        model.addAttribute("pro", rootUrl + "app.mobileprovision");
//        model.addAttribute("downCode", distribute.getDownCode());
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
    public void getMobile(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id,@RequestParam String name) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
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
        response.sendRedirect(tempContextUrl + round + ".mobileconfig");
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
                PackStatus packStatus = new PackStatus(null, null, null, uuid, udid, null,null,null,new Date(), null, null, "待验证", 1,id,tempContextUrl, IpUtils.getIpAddr(request),null);
                packStatusDao.add(packStatus);
                //获取原来的分发地址
                Distribute distribute = distributeDao.query(id);
                String skipUrl = distribute.getUrl().replace("down", "downStatus");
                //再次请求带上uuid和打包状态id
                response.setHeader("Location", skipUrl + "/" + packStatus.getId() + "/"  + uuid + "/" + udid);
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
    @RequestMapping(value = "/downStatus/{base64Id}/{statusId}/{uuid}/{udid}")
    public String getDownStatus(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String base64Id, @PathVariable String statusId, @PathVariable String uuid, @PathVariable String udid) throws JsonProcessingException, UnsupportedEncodingException {

        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        Integer id = Integer.valueOf(new String(Base64.getDecoder().decode(base64Id.getBytes())));
        Distribute distribute = distributeDao.query(Integer.valueOf(id));
        distribute.setIcon(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".png");
        distribute.setApk(rootUrl  + "/" + distribute.getAccount() + "/distribute/" + id + "/" +  id + ".apk");
        distribute.setIpa(rootUrl + "/distribute/" +"getMobile?id=" + id + "&name=" + distribute.getAppName());
        if(distribute.getBuyDownCodeUrl() == null){
            distribute.setBuyDownCodeUrl(rootUrl);
        }
        model.addAttribute("distribute", distribute);
        model.addAttribute("statusId", statusId);
        model.addAttribute("pro", rootUrl + "app.mobileprovision");
        model.addAttribute("downUrl", rootUrl + "/distribute/exec/" + id + "/" + uuid + "/" + udid);
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

    //判断是否需要下载码,如果有就需要带下载码
    @RequestMapping(value = "/exec/{id}/{uuid}/{udid}")
    @ResponseBody
    public Map<String,Object> exec(String downCode, @PathVariable String uuid, @PathVariable Integer id, HttpServletRequest request, @PathVariable String udid) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            //域名
            log.info("udid" + udid);
            String rootUrl = ServerUtil.getRootUrl(request);
            Distribute distribute =  distributeDao.query(id);
            //判断要不要下载码
            if(distribute.getDownCode() == 1){
                log.info("需要下载码");
                //这里暂时只使用最近的一条下载记录
                PackStatus packStatus = packStatusDao.queryUdidCert(udid,distribute.getAccount());
                //判断有没有下载记录
                if(null != packStatus){
                    log.info("查询到下载记录");
                    AppleIis appleIis =  appleIisDao.queryIss(packStatus.getIis());
                    AppleApiUtil appleApiUtil = new AppleApiUtil(appleIis.getIis(),
                            appleIis.getKid(),appleIis.getP8());
                    if(appleApiUtil.init()){
                        packStatusDao.updateStatusExecS("排队中", packStatus.getIis(),packStatus.getDownCode(),packStatus.getP12Path(),packStatus.getMobilePath(),uuid,"待验证");
                        map.put("code",0);
                        map.put("message", "验证成功");
                        map.put("statusUrl",  rootUrl + "/distribute/getStatus?statusId=");
                    }else {
                        appleIisDao.updateStatus(0,appleApiUtil.getIis());
                        throw new RuntimeException("证书失效");
                    }
                }else {
                    log.info("未查询到下载记录");
                    if(null != downCode && !"".equals(downCode)){
                        DownCode downCode1 = downCodeDao.queryAccountDownCode(distribute.getAccount(),downCode);
                        if(null != downCode1){
                            if(downCode1.getStatus() == 1){
                                downCodeDao.updateDownCodeStatus(distribute.getAccount(),downCode,new Date(), 0);
                                packStatusDao.updateStatusExec("排队中",downCode, uuid,"待验证");
                                map.put("code",0);
                                map.put("message", "验证成功");
                                map.put("statusUrl",  rootUrl + "/distribute/getStatus?statusId=");
                            }else {
                                throw new RuntimeException("下载码已被使用");
                            }
                        }else {
                            throw new RuntimeException("下载码错误");
                        }
                    }else {
                        throw new RuntimeException("下载码不能为空");
                    }
                }
            }else {
                log.info("不需要下载码");
                System.out.println("uuid" + uuid);
                packStatusDao.updateStatusExec("排队中",null, uuid,"待验证");
                map.put("code",0);
                map.put("message", "验证成功");
                map.put("statusUrl",  rootUrl + "/distribute/getStatus?statusId=");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
        }
        return map;
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
        try {
            System.out.println(userDao.addCount(user.getAccount(), -this.apkCount));;
        }catch (Exception e){
            throw  new RuntimeException("共有池不足,上传安卓需要扣除共有池" + this.apkCount + "台");
        }
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
    public Map<String,Object> updateDownCodeStatus(@RequestParam Integer id, @RequestParam  @Range(max = 1,min = 0)  Integer downCode, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        distributeDao.updateDownCode(user.getAccount(), id, downCode);
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
    public Map<String,Object> addDownCode(HttpServletRequest request, @RequestParam @Range(max = 100000,min = 1) Integer num) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        distrbuteService.addDownCode(user,num);
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

    //下载证书
    @RequestMapping(value = "/downCert",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> downCert(@RequestParam Integer id,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = (User) request.getSession().getAttribute("user");
        try {
            PackStatus packStatus = packStatusDao.queryDownCert(id, user.getAccount());
            String  tempName = new Date().getTime() + "证书密码123456.zip";
            String  mobilePath = new File(packStatus.getMobilePath()).getParent();
            String  mobilename = new File(packStatus.getMobilePath()).getName();
            String  p12Path = new File(packStatus.getP12Path()).getParent();
            String  p12name = new File(packStatus.getP12Path()).getName();

            String cmd = " tar -cvf  /sign/mode/temp/" + tempName + " -C " + p12Path + " " + p12name + " -C " + mobilePath + " " + mobilename;
            log.info("打包命令" + cmd);
            RuntimeExec.runtimeExec(cmd);
            map.put("code", 0);
            map.put("message", "操作成功");
            map.put("url", rootUrl + tempName);
        }catch (Exception e){
            throw  new RuntimeException("操作失败");
        }
        return map;
    }

}


