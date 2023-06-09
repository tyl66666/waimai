package com.tyl.waimai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.tyl.waimai.mapper")
@ServletComponentScan
@EnableTransactionManagement

public class WaimaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaimaiApplication.class, args);
    }

}
