package com.netease.repository;

import com.netease.repository.cache.CacheConfiguration;
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
@Import(CacheConfiguration.class)
public @interface EnableCache {
}
