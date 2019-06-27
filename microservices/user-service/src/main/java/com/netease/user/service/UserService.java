package com.netease.user.service;

import com.netease.common.entity.mysql.WechatUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/19
 */
@FeignClient(value = "user-service")
public interface UserService {

    String PREFIX = "/user-service";

    @PostMapping(value = PREFIX + "/insert")
    String insert(@RequestBody WechatUser wechatUser);

    @GetMapping(PREFIX + "/queryById")
    WechatUser query(@RequestParam("id") Long id);

    @GetMapping(value = PREFIX + "/demo")
    String demo();
}
