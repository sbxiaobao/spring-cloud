package com.netease.gateway;

import com.netease.gateway.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/12
 */
@Configuration
public class GatewayConfiguration {

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }
}