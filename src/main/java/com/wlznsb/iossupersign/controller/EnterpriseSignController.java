package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.annotation.PxCheckLogin;
import com.wlznsb.iossupersign.mapper.EnterpriseSignCertDao;
import com.wlznsb.iossupersign.mapper.PackStatusEnterpriseSignDao;
import com.wlznsb.iossupersign.mapper.UserDao;
import com.wlznsb.iossupersign.entity.EnterpriseSignCert;
import com.wlznsb.iossupersign.entity.PackStatusEnterpriseSign;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.GetIpaInfoUtil;
import com.wlznsb.iossupersign.util.MyUtil;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业签名打包与证书上传
 */
@RestController
@RequestMapping(value = "/EnterpriseSign")
@Validated
@CrossOrigin(allowCredentials="true")
@Slf4j
@PxCheckLogin
public class EnterpriseSignController {

    @Autowired
    private EnterpriseSignCertDao enterpriseSignCertDao;

    @Autowired
    private PackStatusEnterpriseSignDao packStatusEnterpriseSignDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserServiceImpl userService;

    //上传证书
    @RequestMapping(value = "/uploadCert",method = RequestMethod.POST)
    public Map<String,Object> uploadCert(String token,@RequestParam  MultipartFile mobileProvision, @RequestParam MultipartFile p12, @RequestParam  String password, @RequestParam @Range(min = 0,max = 999999,message = "扣除共有池数超出范围") Integer count,@RequestParam  String remark, HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        if (user.getType() == 1){
            //获取文件md5
            String md5 =  DigestUtils.md5Hex(p12.getInputStream());
            log.info("证书md5" + md5);
            String p12Path =  new File("/sign/temp/" + user.getAccount() + "/enterprise_cert/" + md5 + "/" + md5 + ".p12").getAbsolutePath();
            //判断是否上传过
            EnterpriseSignCert enterpriseSignCert1 = enterpriseSignCertDao.queryMd5(md5);
            if(enterpriseSignCert1 != null){
                map.put("code", 1);
                map.put("message", "证书已存在");
                return map;
            }
            //创建证书目录
            String certDir = new File("/sign/temp/" + user.getAccount() + "/enterprise_cert/" + md5).getAbsolutePath();
            new File(certDir).mkdirs();
            p12.transferTo(new File(p12Path));
            //检查证书
            String data = AppleApiUtil.certVerify(p12Path, password);
            JsonNode jsonNode = new ObjectMapper().readTree(data);
            String name;
            try{
                name = jsonNode.get("data").get("name").asText();
            }catch (Exception e){
                name = null;
            }
            //不等于null说明证书没问题
            if(name != null){
                String expiredTimeS = jsonNode.get("data").get("validEnd").asText();
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date expiredTime = format.parse(expiredTimeS);
                //写入moblic文件
                String mobileProvisionPath =  new File("/sign/temp/" + user.getAccount() + "/enterprise_cert/" + md5 + "/" + md5 + ".mobileprovision").getAbsolutePath();
                mobileProvision.transferTo(new File(mobileProvisionPath));
                EnterpriseSignCert enterpriseSignCert;
                if(jsonNode.get("data").get("status").asText().equals("revoked")){
                    enterpriseSignCert = new EnterpriseSignCert(null,user.getAccount(),name,p12Path,mobileProvisionPath,password,"掉签",count,remark,new Date(),expiredTime,md5);
                }else {
                    if(System.currentTimeMillis() > expiredTime.getTime()){
                        enterpriseSignCert = new EnterpriseSignCert(null,user.getAccount(),name,p12Path,mobileProvisionPath,password,"过期",count,remark,new Date(),expiredTime,md5);
                    }else {
                        enterpriseSignCert = new EnterpriseSignCert(null,user.getAccount(),name,p12Path,mobileProvisionPath,password,"正常",count,remark,new Date(),expiredTime,md5);
                    }
                }
                enterpriseSignCertDao.addCert(enterpriseSignCert);
                map.put("code", 0);
                map.put("message", "上传成功");
            }else {
                map.put("code", 1);
                map.put("message", "证书错误");
            }

        }

        return map;
    }

    /**
     * 删除证书
     * @param md5
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteCert",method = RequestMethod.POST)
    public Map<String,Object> deleteCert(String token,@RequestParam  String md5,HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        if (user.getType() == 1){
            //删除数据
            enterpriseSignCertDao.deleteCert(md5);
            //清空目录
            MyUtil.deleteDir("/sign/temp/" + user.getAccount() + "/enterprise_cert/" + md5);
            new File("/sign/temp/" + user.getAccount() + "/enterprise_cert/" + md5).delete();
            map.put("code", 0);
            map.put("message", "删除成功");
        }

        return map;
    }

    /**
     * 查询所有证书
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryAllCert",method = RequestMethod.GET)
    public Map<String,Object> queryAllCert(String token,@RequestParam Integer pageNum, @RequestParam  Integer pageSize,HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        PageHelper.startPage(pageNum,pageSize);
        Page<User> page =  (Page) enterpriseSignCertDao.queryAllCert();
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }


    /**
     * 修改证书所需共有池和备注
     * @param remark
     * @param md5
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Map<String,Object> edit(String token,@RequestParam String remark,@RequestParam @Range(min = 0,max = 999999,message = "扣除共有池数超出范围") Integer count, @RequestParam  String md5,HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        if(user.getType() == 1){
            enterpriseSignCertDao.edit(remark,count, md5);
            map.put("code", 0);
            map.put("message", "修改成功");
        }
        return map;
    }


    /**
     * 上传ipa签名
     * @param ipa
     * @param md5
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadIpa",method = RequestMethod.POST)
    public Map<String,Object> uploadIpa(String token,@RequestParam MultipartFile ipa, @RequestParam String md5,HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        EnterpriseSignCert enterpriseSignCert = enterpriseSignCertDao.queryMd5(md5);
        User user1 =  userDao.queryAccount(user.getAccount());
        String ipaPath = new File("/sign/mode/temp/unsigned_sign" + MyUtil.getUuid() + ".ipa").getAbsolutePath();
        ipa.transferTo(new File(ipaPath));
        String iconPath = new File("/sign/mode/temp/img" + MyUtil.getUuid() + ".png").getAbsolutePath();
        //读取信息
        Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaPath,iconPath);
        if(mapIpa.get("code") != null){
            throw new RuntimeException("无法读取包信息");
        }
        log.info("用户现有共有池:" + user1.getCount());
        log.info("证书所需:" + enterpriseSignCert.getCount());
        //判断共有池是否充足
        if(user1.getCount() >= enterpriseSignCert.getCount()){
            userDao.addCount(user.getAccount(), enterpriseSignCert.getCount());
            PackStatusEnterpriseSign packStatusEnterpriseSign = new PackStatusEnterpriseSign(
                    null,enterpriseSignCert.getId(),enterpriseSignCert.getName(),user.getAccount(),new Date(),
                    mapIpa.get("displayName").toString(),mapIpa.get("package").
                    toString(),mapIpa.get("versionName").toString(),"排队中",null,ipaPath,ServerUtil.getRootUrl(request));
            packStatusEnterpriseSignDao.add(packStatusEnterpriseSign);
            map.put("code", 0);
            map.put("message", "提交成功-请前往企业签名-打包状态查看");
        }else {
            map.put("code", 1);
            map.put("message", "共有池不足!");
        }
        return map;
    }


    /**
     * 查询打包状态
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryAccountPack",method = RequestMethod.GET)
    public Map<String,Object> queryAccountPack(String token,@RequestParam Integer pageNum, @RequestParam  Integer pageSize,HttpServletRequest request) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        PageHelper.startPage(pageNum,pageSize);
        Page<User> page;
        //管理员则查询所有用户打包
        if(user.getType() == 1){
             page =  (Page) packStatusEnterpriseSignDao.queryAll();
        }else {
             page =  (Page) packStatusEnterpriseSignDao.queryAccount(user.getAccount());
        }
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }
}
