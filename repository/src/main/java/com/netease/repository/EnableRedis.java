package com.netease.repository;

import com.netease.repository.redis.RedisConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfiguration.class)
public @interface EnableRedis {
}
