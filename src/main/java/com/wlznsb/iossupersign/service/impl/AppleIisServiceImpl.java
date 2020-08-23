package com.wlznsb.iossupersign.service.impl;

import com.wlznsb.iossupersign.dao.AppleIisDao;
import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.AppleIisService;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AppleIisServiceImpl implements AppleIisService {


    @Autowired
    private AppleIisDao appleIisDao;


    @Override
    @Transactional
    public int add(String iis, String kid, MultipartFile p8, HttpServletRequest request) {
        try {
            if(appleIisDao.query(iis) == null){

                User  user = (User)request.getSession().getAttribute("user");
                //p8路径
                String p8Path = new File("/sign/temp/" + user.getAccount() + "/cert/" + iis + "/" + iis +  ".p8").getAbsolutePath();
                log.info("p8路径:" + new File(p8Path).getAbsoluteFile());
                //key路径
                String keyPath = new File("/sign/mode/my.key").getAbsolutePath();
                log.info("key路径:" + keyPath);
                new File("/sign/temp/" + user.getAccount() + "/cert/" + iis).mkdirs();
                //写入p8这里的new file必须是绝对路径抽象路径无效
                p8.transferTo(new File(p8Path));
                //创建苹果api工具类
                AppleApiUtil appleApiUtil = new AppleApiUtil(iis, kid, p8Path);
                //获取证书工作目录
                String directoryPath = new File("/sign/temp/" + user.getAccount() + "/cert/" + iis + "/").getAbsolutePath();
                log.info("证书工作目录:" + directoryPath);
                if(appleApiUtil.init()){
                    //删除所有证书
                    appleApiUtil.deleCertAll();
                    //生成p12
                    Map<String,String> map=  appleApiUtil.createCert(directoryPath,keyPath,"123456");
                    String identifier = appleApiUtil.addIdentifiers("com.wlznsb.cn", "test");
                    String p12 = map.get("p12");
                    String certId = map.get("certId");
                    //写入数据库
                    AppleIis appleIis = new AppleIis(null, user.getAccount(), iis, kid,certId,identifier,p8Path,p12,1, 1, 0, 100,new Date());
                    appleIisDao.add(appleIis);
                }else {
                    throw  new RuntimeException("请检查各项信息是否填写正确");
                }

            }else {
                throw  new RuntimeException("该证书已添加");
            }

        }catch (Exception e){
            throw  new RuntimeException("添加失败" + e.toString());
        }
        return 0;
    }

    @Override
    @Transactional
    public int dele(String iis,HttpServletRequest request) {
        try {
            AppleIis appleIis = appleIisDao.query(iis);
            User user = (User) request.getSession().getAttribute("user");
            if(appleIis != null && appleIis.getAccount().equals(user.getAccount())){
                appleIisDao.dele(iis);
                File file = new File("/sign/temp/" + appleIis.getAccount() + "/cert/" + appleIis.getIis()).getAbsoluteFile();
                System.out.println(file.getAbsolutePath());
                FileSystemUtils.deleteRecursively(file);
                System.out.println(file);
            }else {
                throw  new RuntimeException("iis账号不存在");
            }

        }catch (Exception e){
            throw  new RuntimeException("删除失败," + e.toString());
        }
        return 0;
    }

    @Override
    @Transactional
    public int updateStartOrStatus(String type, String iis, int s,HttpServletRequest request) {
        try {
            AppleIis appleIis = appleIisDao.query(iis);
            User user = (User) request.getSession().getAttribute("user");
            if(appleIis != null && appleIis.getAccount().equals(user.getAccount())){
                switch (type){
                    case "status":
                        appleIisDao.updateStatus(s, iis);
                        break;
                    case "start":
                        appleIisDao.updateStart(s, iis);
                        break;
                    case "ispublic":
                        appleIisDao.updateIspublic(s,iis);
                        break;
                    default:
                        throw new RuntimeException("参数类型错误");
                }
            }else {
                throw new RuntimeException("证书不存在");
            }
        }catch (Exception e){
            throw new RuntimeException("修改失败:" + e.toString());
        }
        return 0;
    }

    @Override
    @Transactional
    public AppleIis query(String iis, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            AppleIis appleIis = appleIisDao.query(iis);
            return appleIis;
        }catch (Exception e){
            throw new RuntimeException("查询失败:" + e.toString());
        }
    }

    @Override
    @Transactional
    public List<AppleIis> queryAll(HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            List<AppleIis> appleIis = appleIisDao.queryAll();
            return appleIis;
        }catch (Exception e){
            throw new RuntimeException("查询失败:" + e.toString());
        }
    }

    @Override
    @Transactional
    public List<AppleIis> queryAccount(String account, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            List<AppleIis> appleIis = appleIisDao.queryAccount(account);
            return appleIis;
        }catch (Exception e){
            throw new RuntimeException("查询失败:" + e.toString());
        }
    }


}
