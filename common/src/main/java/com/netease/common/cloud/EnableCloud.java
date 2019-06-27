package com.netease.common.cloud;

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
@Inherited
@Import({
        ResponseBodyWrapFactoryBean.class
})
public @interface EnableCloud {
}
