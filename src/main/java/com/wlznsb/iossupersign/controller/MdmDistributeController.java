package com.wlznsb.iossupersign.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.annotation.PxCheckLogin;
import com.wlznsb.iossupersign.constant.RedisKey;
import com.wlznsb.iossupersign.entity.*;
import com.wlznsb.iossupersign.mapper.*;
import com.wlznsb.iossupersign.service.DistrbuteServiceImpl;
import com.wlznsb.iossupersign.service.MdmDistrbuteServiceImpl;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import com.wlznsb.iossupersign.util.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Controller
@RequestMapping(value = "/mdmdistribute")
@Validated
@Slf4j
@CrossOrigin(allowCredentials="true")
@PxCheckLogin
public class MdmDistributeController {

    private Map<String,String> tempUuid = new HashMap<>();

    @Autowired
    private MdmDistrbuteServiceImpl distrbuteService;

    @Autowired
    private DomainDao domainDao;

    @Value("${apkCount}")
    private Integer apkCount;

    @Autowired
    private AppleIisDao appleIisDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MdmDistributeMapper distributeDao;

    @Autowired
    private MdmPackStatusMapper packStatusDao;

    @Autowired
    private MdmDownCodeMapper downCodeDao;

    @Autowired
    private CertInfoMapper certInfoMapper;

    //下载页面,没有使用业务层 第一步
    @RequestMapping(value = "/down/v1/{base64Id}",method = RequestMethod.GET)
    @PxCheckLogin(value = false)
    @ResponseBody
    public Map<String,Object>  getDownV1(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String base64Id) throws JsonProcessingException, UnsupportedEncodingException {
        //域名
        String rootUrl = ServerUtil.getRootUrl(request);
        log.info("当前base64Id" + base64Id);
        String id = new String(Base64.getDecoder().decode(base64Id.getBytes()));
        log.info("当前id" + id);
        MdmDistributeEntity distribute = distributeDao.query(id);
        if(distribute.getApk() != null){
            String time = Base64.getEncoder().encodeToString(Long.toString(new Date().getTime() * 1390).getBytes());
            time = Base64.getEncoder().encodeToString(time.getBytes());

            distribute.setApk(rootUrl  + distribute.getAccount() + "/mdmdistribute/" + id + "/" +  id + ".apk");
        }else {
            distribute.setApk(null);
        }
        distribute.setIcon(rootUrl  + distribute.getAccount() + "/mdmdistribute/" + id + "/" +  id + ".png");
        distribute.setIpa(rootUrl + "mdmdistribute/" +"getMobileV1?id=" + id + "&name=" + distribute.getAppName() + "&language=" +  distribute.getLanguage());

        List<String> imgs = new ArrayList<>();
//        model.addAttribute("downCode", distribute.getDownCode());
        if(null == distribute.getImages()){

            model.addAttribute("img1", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img2", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img3", rootUrl + "/images/" + "slideshow.png");
            model.addAttribute("img4", rootUrl + "/images/" + "slideshow.png");
        }else {

            imgs.add(rootUrl  + distribute.getAccount() + "/mdmdistribute/" + id + "/" + "img1.png");
            imgs.add( rootUrl + distribute.getAccount() + "/mdmdistribute/" + id + "/" + "img2.png");
            imgs.add( rootUrl  + distribute.getAccount() + "/mdmdistribute/" + id + "/" + "img3.png");
            imgs.add( rootUrl   + distribute.getAccount() + "/mdmdistribute/" + id + "/" + "img4.png");
        }

        Map<String,Object> map = new HashMap();
        map.put("code", 0);
        map.put("message", "获取成功");
        map.put("data",distribute);
        map.put("imgs",imgs.size() == 0 ?null:imgs);
        map.put("pro",rootUrl + "app.mobileprovision");
        return map;
    }


    @Autowired
    private SystemctlSettingsMapper systemctlSettingsMapper;


    //获取描述文件,没有使用业务层 第二步
    @GetMapping
    @RequestMapping("/getMobileV1")
    @PxCheckLogin(value = false)
    @ResponseBody
    public Map<String,Object> getMobileV1(HttpServletRequest request, HttpServletResponse response, @RequestParam String id,@RequestParam String name,String language) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();

        //域名
        String tempContextUrl = ServerUtil.getRootUrl(request);



        //获取可用证书
        CertInfoEntity certInfoEntity = certInfoMapper.selectOneByCertStatus(1);

        SystemctlSettingsEntity systemctlSettingsEntity = systemctlSettingsMapper.selectOne(null);


        if(null != certInfoEntity){

            OkHttpClient client = MyUtil.getOkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            okhttp3.RequestBody body;

            if(language.equals("zh")){
                body = okhttp3.RequestBody.create(mediaType, "certId="+ certInfoEntity.getCertId()  +"&des=" + "该配置文件帮助用户进行App授权安装" + "&name=" +name+ "&ziName=安装后返回浏览器&permission=4096");
            }else {
                body = okhttp3.RequestBody.create(mediaType, "certId="+ certInfoEntity.getCertId()  +"&des=" + "This configuration file helps users to authorize the installation of the App" + "&name=" +name+ "&ziName=Return to the browser after installation&permission=4096");
            }

            Request request1 = new Request.Builder()
                    .url("https://" +  systemctlSettingsEntity.getMdmDomain() + "/mdm/get_mobile_config")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response1 = client.newCall(request1).execute();
            //序列化返回
            JsonNode jsonNode = new ObjectMapper().readTree(response1.body().string());

            if(jsonNode.get("code").asText().equals("200")){
                String url = jsonNode.get("data").get("url").asText();
                String deviceId = jsonNode.get("data").get("deviceId").asText();

                map.put("code", 0);
                map.put("message", "获取成功");
                map.put("url",url);
                map.put("deviceId",deviceId);
                map.put("execUrl",tempContextUrl + "mdmdistribute/exec/v1/" + deviceId + "/" + id);

                return map;
            }else {
                throw new RuntimeException("操作失败:" + jsonNode.get("msg").asText());
            }

        }else {

            throw new RuntimeException("没有可用mdm证书");
        }

    }

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    //判断是否需要下载码,如果有就需要带下载码 第三步
    @RequestMapping(value = "/exec/v1/{deviceId}/{appId}")
    @ResponseBody
    @PxCheckLogin(value = false)
    public Map<String,Object> execv1(String downCode, @PathVariable String deviceId,@PathVariable String appId, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();

        try {
            //域名
            log.info("deviceId" + deviceId);
            String rootUrl = ServerUtil.getRootUrl(request);

            MdmPackStatusEntity mdmPackStatusEntity = packStatusDao.selectOneByDeviceId(deviceId);
            //判断是否已经存在任务 如果存在则直接返回
            if(null != mdmPackStatusEntity){

                map.put("code", 0);
                map.put("message", "验证成功");
                map.put("statusUrl", rootUrl + "mdmdistribute/getStatusV1?statusId=" + deviceId);

            }else {

                DeviceInfoEntity deviceInfoEntity = deviceInfoMapper.selectOneByDeviceId(deviceId);
                //查询设备是否注册成功
                if(null != deviceInfoEntity && deviceInfoEntity.getStatus().equals("TokenUpdate")){

                    String udid = deviceInfoEntity.getUdid();
                    MdmDistributeEntity distribute =  distributeDao.query(appId);

                    //判断要不要下载码
                    if(distribute.getDownCode() == 1){
                        log.info("需要下载码");
                        //这里暂时只使用最近的一条下载记录
                        MdmPackStatusEntity packStatus = packStatusDao.queryUdidCert(udid,distribute.getAccount());
                        //判断有没有下载记录
                        if(null != packStatus){
                            log.info("查询到下载记录");
                            AppleIis appleIis =  appleIisDao.queryIss(packStatus.getIis());
                            AppleApiUtil appleApiUtil = new AppleApiUtil(appleIis.getIis(),
                                    appleIis.getKid(),appleIis.getP8());
                            log.info("判断证书是否失效");
                            if(appleApiUtil.init()){
                                log.info("证书正常");
                                //创建状态 uuid packstatusid 和deviceId 均为 deviceId
                                MdmPackStatusEntity packStatus1 = new MdmPackStatusEntity(deviceId, distribute.getAccount(), distribute.getPageName(), deviceId, udid, packStatus.getIis(),packStatus.getP12Path(),packStatus.getMobilePath(),new Date(), null, null, "排队中", 1,distribute.getId(),rootUrl, IpUtils.getIpAddr(request),packStatus.getDownCode(),deviceId);
                                packStatusDao.insert(packStatus1);
                                map.put("code",0);
                                map.put("message", "验证成功");
                                map.put("statusUrl",  rootUrl + "mdmdistribute/getStatusV1?statusId=" + deviceId);
                            }else {
                                log.info("证书失效走正常逻辑");
                                appleIisDao.updateStatus(0,appleApiUtil.getIis());
                                if(null != downCode && !"".equals(downCode)){
                                    MdmDownCodeEntity downCode1 = downCodeDao.queryAccountDownCode(distribute.getAccount(),downCode);
                                    if(null != downCode1){
                                        if(downCode1.getStatus() == 1){
                                            downCodeDao.updateDownCodeStatus(distribute.getAccount(),downCode,new Date(), 0);
                                            MdmPackStatusEntity packStatus1 = new MdmPackStatusEntity(deviceId, distribute.getAccount(), distribute.getPageName(), deviceId, udid, null,null,null,new Date(), null, null, "排队中", 1,distribute.getId(),rootUrl, IpUtils.getIpAddr(request),downCode,deviceId);
                                            packStatusDao.insert(packStatus1);
                                            map.put("code",0);
                                            map.put("message", "验证成功");
                                            map.put("statusUrl",  rootUrl + "mdmdistribute/getStatusV1?statusId=" + deviceId);
                                        }else {
                                            map.put("code",11);
                                            map.put("message", "下载码已被使用");
                                        }
                                    }else {
                                        map.put("code",11);
                                        map.put("message", "下载码错误");
                                    }
                                }else {
                                    map.put("code",11);
                                    map.put("message", "请输入下载码");
                                }

                            }
                        }else {
                            log.info("未查询到下载记录");
                            if(null != downCode && !"".equals(downCode)){
                                MdmDownCodeEntity downCode1 = downCodeDao.queryAccountDownCode(distribute.getAccount(),downCode);
                                if(null != downCode1){
                                    if(downCode1.getStatus() == 1){
                                        downCodeDao.updateDownCodeStatus(distribute.getAccount(),downCode,new Date(), 0);
                                        MdmPackStatusEntity packStatus1 = new MdmPackStatusEntity(deviceId, distribute.getAccount(), distribute.getPageName(), deviceId, udid, null,null,null,new Date(), null, null, "排队中", 1,distribute.getId(),rootUrl, IpUtils.getIpAddr(request),downCode,deviceId);
                                        packStatusDao.insert(packStatus1);
                                        map.put("code",0);
                                        map.put("message", "验证成功");
                                        map.put("statusUrl",  rootUrl + "mdmdistribute/getStatusV1?statusId=" + deviceId);
                                    }else {
                                        map.put("code",11);
                                        map.put("message", "下载码已被使用");
                                    }
                                }else {
                                    map.put("code",11);
                                    map.put("message", "下载码错误");
                                }
                            }else {

                                map.put("code",11);
                                map.put("message", "请输入下载码");
                            }
                        }
                    }else {
                        //这里暂时只使用最近的一条下载记录
                        MdmPackStatusEntity packStatus = packStatusDao.queryUdidCert(udid,distribute.getAccount());
                        log.info("不需要下载码");
                        //判断有没有下载记录
                        if(null != packStatus) {
                            log.info("查询到下载记录");
                            AppleIis appleIis = appleIisDao.queryIss(packStatus.getIis());
                            AppleApiUtil appleApiUtil = new AppleApiUtil(appleIis.getIis(),
                                    appleIis.getKid(), appleIis.getP8());
                            if (appleApiUtil.init()) {
                                log.info("证书正常");
                                //创建状态 uuid packstatusid 和deviceId 均为 deviceId
                                MdmPackStatusEntity packStatus1 = new MdmPackStatusEntity(deviceId, distribute.getAccount(), distribute.getPageName(), deviceId, udid, packStatus.getIis(),packStatus.getP12Path(),packStatus.getMobilePath(),new Date(), null, null, "排队中", 1,distribute.getId(),rootUrl, IpUtils.getIpAddr(request),packStatus.getDownCode(),deviceId);
                                packStatusDao.insert(packStatus1);
                                map.put("code",0);
                                map.put("message", "验证成功");
                                map.put("statusUrl",  rootUrl + "mdmdistribute/getStatusV1?statusId=" + deviceId);
                            } else {
                                log.info("证书失效走正常逻辑");
                                appleIisDao.updateStatus(0,appleApiUtil.getIis());
                                MdmPackStatusEntity packStatus1 = new MdmPackStatusEntity(deviceId, distribute.getAccount(), distribute.getPageName(), deviceId, udid, null,null,null,new Date(), null, null, "排队中", 1,distribute.getId(),rootUrl, IpUtils.getIpAddr(request),null,deviceId);
                                packStatusDao.insert(packStatus1);
                                map.put("code",0);
                                map.put("message", "验证成功");
                                map.put("statusUrl",  rootUrl + "mdmdistribute/getStatusV1?statusId=" + deviceId);
                            }
                        }else {
                            log.info("未查到下载记录");
                            MdmPackStatusEntity packStatus1 = new MdmPackStatusEntity(deviceId, distribute.getAccount(), distribute.getPageName(), deviceId, udid, null,null,null,new Date(), null, null, "排队中", 1,distribute.getId(),rootUrl, IpUtils.getIpAddr(request),null,deviceId);
                            packStatusDao.insert(packStatus1);
                            map.put("code",0);
                            map.put("message", "验证成功");
                            map.put("statusUrl",  rootUrl + "mdmdistribute/getStatusV1?statusId=" + deviceId);
                        }
                    }
                }else {
                    map.put("code",10);
                    map.put("message", "等待安装描述文件");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
        }
        return map;
    }




    //查询打包状态,没有使用业务层 第四步
    @RequestMapping(value = "/getStatusV1")
    @ResponseBody
    @PxCheckLogin(value = false)
    public Map<String,Object> getStatusV1(String statusId,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        MdmPackStatusEntity packStatus =  packStatusDao.query(statusId);
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", packStatus);
        if(packStatus.getStatus().equals("点击下载")){
            map.put("install", ServerUtil.getRootUrl(request) + "mdmdistribute/install/" + statusId);
        }
        return map;
    }






    @Autowired
    private DeviceCommandTaskMapper taskMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //发送下载任务
    @RequestMapping(value = "/install/{statusId}",method = RequestMethod.GET)
    @PxCheckLogin(value = false)
    @ResponseBody
    public Map<String,Object> install(HttpServletRequest request, @PathVariable String statusId) throws IOException {
        Map<String,Object> map = new HashMap<>();

        MdmPackStatusEntity mdmPackStatusEntity = packStatusDao.selectById(statusId);

        DeviceInfoEntity deviceInfoEntity = deviceInfoMapper.selectById(mdmPackStatusEntity.getDeviceId());

        Date date = new Date();
        DeviceCommandTaskEntity taskEntity = new DeviceCommandTaskEntity();
        taskEntity.setTaskId(MyUtil.getUuid());
        taskEntity.setDeviceId(mdmPackStatusEntity.getDeviceId());
        taskEntity.setCmd("InstallApplication");
        taskEntity.setExecResult("");
        taskEntity.setCreateTime(date);
        taskEntity.setExecTime(date);
        taskEntity.setResultTime(date);
        taskEntity.setTaskStatus(0);
        taskEntity.setPushCount(0);
        taskEntity.setExecResultStatus("");
        taskEntity.setCertId(deviceInfoEntity.getCertId());
        taskEntity.setUdid(deviceInfoEntity.getUdid());
        String cmda = "{\"type\":\"ManifestURL\",\"value\":\"#plist#\"}";
        cmda = cmda.replace("#plist#",mdmPackStatusEntity.getPlist().replace("itms-services://?action=download-manifest&url=",""));
        taskEntity.setCmdAppend(cmda);
        CertInfoEntity certInfoEntity = certInfoMapper.selectById(deviceInfoEntity.getCertId());
        if(null == certInfoEntity){
            map.put("code",1);
            map.put("message", "证书不存在");
            return map;
        }
        taskEntity.setP12Path(certInfoEntity.getP12Path());
        taskEntity.setP12Password(certInfoEntity.getP12Password());
        taskEntity.setToken(deviceInfoEntity.getToken());
        taskEntity.setMagic(deviceInfoEntity.getMagic());

        stringRedisTemplate.opsForValue().set(String.format(RedisKey.TASK_PUSH,taskEntity.getTaskId()), JSON.toJSONString(taskEntity));

        map.put("code",0);
        map.put("message", "成功");
        return map;
    }

    @Autowired
    private UserServiceImpl userService;

    //上传ipa
    @RequestMapping(value = "/uploadIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestHeader String token,@RequestParam MultipartFile ipa, String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println("id" + id);
        //域名路径
//        String rootUrl = ServerUtil.getRootUrl(request);
        //随机域名
        Domain domain =  domainDao.randomDomain();
        User user = userService.getUser(token);
        MdmDistributeEntity distribute;
        //库里没有域名就是主域名
        if(domain != null){
            distribute = distrbuteService.uploadIpa(ipa, user,"https://" + domain.getDomain() + "/",id);
        }else {
            distribute = distrbuteService.uploadIpa(ipa, user,ServerUtil.getRootUrl(request),id);
        }
        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }

    //修改域名
    @RequestMapping(value = "/updateDomain",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateDomain(@RequestHeader String token,@RequestParam String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
//域名路径
//        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        MdmDistributeEntity distribute =  distributeDao.query(id);
        if(distribute != null){
            String oldDomain = new  java.net.URL(distribute.getUrl()).getHost();
            Domain domain = domainDao.randomNoDomain(oldDomain);
            log.info("老域名" + oldDomain);
            if(domain != null){
                distributeDao.updateDomain(distribute.getUrl().replace(oldDomain,domain.getDomain()),user.getAccount(),id);
            }else {
                throw  new RuntimeException("暂时没有可用域名");
            }
        }else {
            log.info("操作失败,应用不存在");
            throw  new RuntimeException("操作失败,应用不存在");
        }

        //随机域名
        map.put("code", 0);
        map.put("message", "更换成功 点击复制地址即可查看新域名");
        return map;
    }


    //上传apk
    @RequestMapping(value = "/uploadApk",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadApk(@RequestHeader String token,@RequestParam MultipartFile apk,@RequestParam String id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
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
    public Map<String,Object> deleIpa(@RequestHeader String token,@RequestParam  String id,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        distrbuteService.dele(user, id);

        map.put("code", 0);
        map.put("message", "删除成功");
        return map;
    }


    //查询ipa
    @RequestMapping(value = "/queryAccountAll",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAccountAll(@RequestHeader String token,HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
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
    public Map<String,Object> updateIntroduce(@RequestHeader String token,@RequestParam @NotEmpty String introduce, @RequestParam String id, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        distributeDao.updateIntroduce(introduce, user.getAccount(), id);
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

    //修改语言
    @RequestMapping(value = "/updateLanguage",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateLanguage(@RequestHeader String token,@RequestParam @NotEmpty String language, @RequestParam String id, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        distributeDao.updateLanguage(language, user.getAccount(), id);
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }


    //上传轮播图
    @RequestMapping(value = "/uploadImg",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateIntroduce(@RequestHeader String token,@RequestParam MultipartFile img1,@RequestParam MultipartFile img2,@RequestParam MultipartFile img3,MultipartFile img4, @RequestParam String id, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        String path = new File("./sign/temp/" + user.getAccount() + "/mdmdistribute/" + id + "/img").getAbsolutePath();
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
    public Map<String,Object> updateDownCodeStatus(@RequestHeader String token,@RequestParam String id, @RequestParam  @Range(max = 1,min = 0)  Integer downCode, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        distributeDao.updateDownCode(user.getAccount(), id, downCode);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }


    //修改下载码购买地址
    @RequestMapping(value = "/updateBuyDownCodeUrl",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateBuyDownCodeUrl(@RequestHeader String token,@RequestParam String id,@NotEmpty @RequestParam String url, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        distributeDao.updateBuyDownCodeUrl(user.getAccount(), id, url);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }

    //添加下载码
    @RequestMapping(value = "/addDownCode",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addDownCode(@RequestHeader String token,HttpServletRequest request, @RequestParam @Range(max = 100000,min = 1) Integer num) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        distrbuteService.addDownCode(user,num);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }

    //查询所有下载码
    @RequestMapping(value = "/queryAllDownCode",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAllDownCode(@RequestHeader String token,@RequestParam Integer pageNum,@RequestParam  Integer pageSize,HttpServletRequest request)  {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
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
    public Map<String,Object> deleDownCode(@RequestHeader String token,@RequestParam String id,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        downCodeDao.deleDownCode(user.getAccount(), id);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }

    //下载证书
    @RequestMapping(value = "/downCert",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> downCert(@RequestHeader String token,@RequestParam String id,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        String rootUrl = ServerUtil.getRootUrl(request);
        User user = userService.getUser(token);
        try {
            MdmPackStatusEntity packStatus = packStatusDao.queryDownCert(id, user.getAccount());
            String  tempName = new Date().getTime() + "证书密码123456.tar";
            String  mobilePath = new File(packStatus.getMobilePath()).getParent();
            String  mobilename = new File(packStatus.getMobilePath()).getName();
            String  p12Path = new File(packStatus.getP12Path()).getParent();
            String  p12name = new File(packStatus.getP12Path()).getName();

            String cmd = " tar -cvf  ./sign/mode/temp/" + tempName + " -C " + p12Path + " " + p12name + " -C " + mobilePath + " " + mobilename;
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


