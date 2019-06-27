package com.netease.configcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/12
 */
@EnableConfigServer
@SpringBootApplication
@EnableEurekaServer
public class ConfigCenterMain {

    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterMain.class, args);
    }

}
