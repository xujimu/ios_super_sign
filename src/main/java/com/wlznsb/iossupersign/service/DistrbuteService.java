package com.wlznsb.iossupersign.service;

import com.wlznsb.iossupersign.entity.Distribute;
import com.wlznsb.iossupersign.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface DistrbuteService {

    /**
     * 上传ipa
     * @param ipa
     * @param
     * @return
     */
    Distribute uploadIpa(MultipartFile ipa, User user,String rootUrl);

    /**
     * 上传apk
     */
    int uploadApk(MultipartFile apk,User user,Integer id);

    /**
     * 获取udid
     * @param id
     * @return
     */
    String getUuid(int id,String uuid,String url, String udid);



    /**
     * 删除ipa
     *
     */
    int dele(String account,int id);

    /**
     * 查询账号所有记录
     */
    List<Distribute> queryAccountAll(String account);

}
