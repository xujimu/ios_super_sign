package com.wlznsb.iossupersign.controller;

import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.util.IoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class GetUdidController {

    @Autowired
    private UserDao userDao;

    //获取描述文件
    @GetMapping
    @RequestMapping("/udid/getMobile")
    public String getMobile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(userDao.queryAll()+"111111111111111");
        return "redirect:/ios/udid.mobileconfig";
    }


    @PutMapping
    @RequestMapping("/udid/getUdid")
    public void getUdid(HttpServletResponse response) throws IOException {
        response.setHeader("Location", "https://www.baidu.com");
        response.setStatus(301);
    }
}

