package com.wlznsb.iossupersign.service;

import com.wlznsb.iossupersign.entity.Distribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DistrbuteService {

    /**
     * 上传ipa
     * @param ipa
     * @param request
     * @return
     */
    Distribute uploadIpa(MultipartFile ipa, HttpServletRequest request);

    /**
     * 获取udid
     * @param id
     * @return
     */
    String getUuid(int id,String uuid,HttpServletRequest request, HttpServletResponse response);


}
