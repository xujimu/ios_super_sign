package com.wlznsb.iossupersign.service;

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
    int uploadIpa(MultipartFile ipa, HttpServletRequest request);

    /**
     * 获取uuid
     * @param id
     * @return
     */
    String getUuid(int id, HttpServletRequest request, HttpServletResponse response);
}
