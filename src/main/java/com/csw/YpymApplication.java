package com.csw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.com.csw.dao")
public class YpymApplication {

    public static void main(String[] args) {
        SpringApplication.run(YpymApplication.class, args);
    }

}
