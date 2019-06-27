package com.netease.repository;

import com.netease.repository.mysql.MysqlConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MysqlConfiguration.class)
public @interface EnableMysql {
}