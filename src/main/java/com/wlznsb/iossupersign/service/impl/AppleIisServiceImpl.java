package com.wlznsb.iossupersign.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.dao.AppleIisDao;
import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.AppleIisService;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

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
    public int add(String iis, String kid, MultipartFile p8, User user ) {
        //证书目录
        String certRoot = null;
        try {
            if(appleIisDao.query(user.getAccount(),iis) == null){

                //p8路径
                String p8Path = new File("/sign/temp/" + user.getAccount() + "/cert/" + iis + "/" + iis +  ".p8").getAbsolutePath();
                log.info("p8路径:" + new File(p8Path).getAbsoluteFile());
                //key路径
                String keyPath = new File("/sign/mode/my.key").getAbsolutePath();
                log.info("key路径:" + keyPath);
                //创建证书目录
                certRoot = new File("/sign/temp/" + user.getAccount() + "/cert/" + iis).getAbsolutePath();
                new File(certRoot).mkdirs();
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
                    //随机bunild id
                    String buildId = ServerUtil.getUuid();
                    String identifier = appleApiUtil.addIdentifiers(buildId,buildId);
                    String p12 = map.get("p12");
                    String certId = map.get("certId");
                    //查询剩余设备
                    int count = 100 - new ObjectMapper().readTree(appleApiUtil.queryDevices()).get("meta").get("paging").get("total").asInt();
                    //写入数据库
                    AppleIis appleIis = new AppleIis(null, user.getAccount(), iis, kid,certId,identifier,p8Path,p12,1, 1, 0, count,new Date());
                    appleIisDao.add(appleIis);
                }else {
                    //如果失败就删除证书目录
                    FileSystemUtils.deleteRecursively(new File(certRoot));
                    throw  new RuntimeException("请检查各项信息是否填写正确");
                }
            }else {
                throw  new RuntimeException("该证书已添加");
            }

        }catch (Exception e){
            log.info(e.toString());
            throw  new RuntimeException("添加失败:" + e.getMessage());
        }
        return 0;
    }

    @Override
    @Transactional
    public int dele(String iis,User user) {
        try {
            AppleIis appleIis = appleIisDao.query(user.getAccount(),iis);
            if(appleIis != null){
                appleIisDao.dele(user.getAccount(),iis);
                File file = new File("/sign/temp/" + appleIis.getAccount() + "/cert/" + appleIis.getIis()).getAbsoluteFile();
                System.out.println(file.getAbsolutePath());
                FileSystemUtils.deleteRecursively(file);
                System.out.println(file);
            }else {
                throw  new RuntimeException("iis账号不存在");
            }

        }catch (Exception e){
            log.info(e.toString());
            throw  new RuntimeException("删除失败:" + e.getMessage());
        }
        return 0;
    }

    @Override
    @Transactional
    public int updateStartOrStatus(String type, String iis, int s,User user) {
        try {
            AppleIis appleIis = appleIisDao.query(user.getAccount(),iis);
            if(appleIis != null){
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
            log.info(e.toString());
            throw new RuntimeException("修改失败:" + e.getMessage());
        }
        return 0;
    }

    @Override
    @Transactional
    public AppleIis query(String iis, User user) {
        try {
            AppleIis appleIis = appleIisDao.query(user.getAccount(),iis);
            return appleIis;
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("查询失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<AppleIis> queryAll() {
        try {
            List<AppleIis> appleIis = appleIisDao.queryAll();
            return appleIis;
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("查询失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<AppleIis> queryAccount(String account) {
        try {
            List<AppleIis> appleIis = appleIisDao.queryAccount(account);
            return appleIis;
        }catch (Exception e){
            log.info(e.toString());
            throw new RuntimeException("查询失败:" + e.getMessage());
        }
    }


}
