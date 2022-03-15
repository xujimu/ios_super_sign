package com.wlznsb.iossupersign.config;

import com.wlznsb.iossupersign.util.IoHandler;
import com.wlznsb.iossupersign.util.RuntimeExec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author lnj
 * createTime 2018-11-07 22:37
 **/
@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Value("${mqpassword}")
    private String password;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //自动转换jks为apache证书
//        if(!new File("/sign/mode/cert/server.crt").exists()){
//            String cmd = "/sign/mode/cert.sh /sign/mode/cert.jks " + password +
//                    " /sign/mode/p12.p12 /sign/mode/cert/server.crt /sign/mode/cert/root.crt /sign/mode/cert/key.key";
//            log.info("执行命令" + cmd);
//            RuntimeExec.runtimeExec(cmd);
//
//        }
    }
}
