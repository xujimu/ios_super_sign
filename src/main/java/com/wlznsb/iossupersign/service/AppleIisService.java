package com.wlznsb.iossupersign.service;


import com.wlznsb.iossupersign.entity.AppleIis;
import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppleIisService {

    /**
     *
     *添加iis账号
     * @param appleIis 需要添加的appleiis实体
     * @return
     */
    int add(String iis,String kid,MultipartFile p8,HttpServletRequest request);


    /**
     * 删除iis账号
     * @param iis 需要查询的iis
     * @return
     */
    int dele(String iis,HttpServletRequest request);


    /**
     * 修改status或者start或者ispublic
     * @param type  status  start  ispublic
     * @param iis 账号
     * @param s 修改成几 0启用 1不启用
     * @return
     */
    int updateStartOrStatus(String type,String iis,int s,HttpServletRequest request);


    /**
     * 查询一个iis证书
     * @return iis 需要查询的iis
     */
    AppleIis query(String iis,HttpServletRequest request);

    /**
     *
     * 查询所有iis证书
     *
     */
    List<AppleIis> queryAll(HttpServletRequest request);


    /**
     * 查询一个账号的证书
     * @param account 需要查询的账号
     * @return
     */
    List<AppleIis> queryAccount(String account,HttpServletRequest request);






}
