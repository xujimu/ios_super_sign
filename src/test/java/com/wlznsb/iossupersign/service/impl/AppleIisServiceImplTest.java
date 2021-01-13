package com.wlznsb.iossupersign.service.impl;

import com.wlznsb.iossupersign.service.AppleIisServiceImpl;
import com.wlznsb.iossupersign.util.IoHandler;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AppleIisServiceImplTest {

    @Autowired
    private AppleIisServiceImpl appleIisService;

    @Test
    void add() throws IOException {


       // System.out.println(new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX + "static/ios/my.key").getFile()).getAbsolutePath());
    }

    @Test
    void dele() throws IOException {

      //  File file = new File("C:\\sign\\123\\70e2fe2f-cb06-49a2-9696-753ca9ca7a50");
       // FileSystemUtils.deleteRecursively(file);
        System.out.println(false);
    }

    @Test
    void updateStartOrStatus() {
       // NSDictionary root = null;
       // NSDictionary iconDict = (NSDictionary) root.get("CFBundleIcons");
    }

    @Test
    void query() {
    }

    @Test
    void queryAll() {
    }

    @Test
    void queryAccount() {
    }
}
