package com.wlznsb.iossupersign;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@Slf4j
public class IosSuperSignApplication {

    public static void main(String[] args) {
        log.info("启动了去除业务层接口");
        SpringApplication.run(IosSuperSignApplication.class, args);

    }

}
