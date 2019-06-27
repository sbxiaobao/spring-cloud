package com.netease.user;

import com.netease.common.cloud.EnableCloud;
import com.netease.repository.EnableCache;
import com.netease.repository.EnableMysql;
import com.netease.repository.EnableRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/19
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableMysql
@EnableRedis
@EnableCache
@EnableCloud
public class UserServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceMain.class, args);
    }
}
