package com.wlznsb.iossupersign;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class IosSuperSignApplication {

    public static void main(String[] args) {
        log.info("时间修复");
        SpringApplication.run(IosSuperSignApplication.class, args);

    }

}
