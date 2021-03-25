package com.baizhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient //代表将当前服务注册到nacos服务注册中心
@MapperScan("com.baizhi.dao")
public class CategoriesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CategoriesApplication.class,args);
    }
}
