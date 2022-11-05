package com.xingchen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("com.xingchen.*")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
@EnableScheduling
@MapperScan("com.xingchen.dao")
public class MorepractiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MorepractiseApplication.class, args);
    }

}
