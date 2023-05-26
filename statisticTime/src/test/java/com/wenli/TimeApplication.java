package com.wenli;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wenli.mapper")
public class TimeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimeApplication.class, args);
    }
}
