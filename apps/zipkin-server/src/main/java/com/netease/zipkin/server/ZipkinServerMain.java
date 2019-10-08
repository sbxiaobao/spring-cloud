package com.netease.zipkin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.EnableZipkinServer;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/8/5
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableZipkinServer
@EnableEurekaClient
public class ZipkinServerMain {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinServerMain.class, args);
    }
}