package com.netease.repository.cache;

import com.netease.repository.redis.CacheRedisConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
@Import({CacheRedisConfiguration.class})
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Value("${cache.redis.default.expiration:86400}")
    private long defaultExpiretion;

    @Value("${cache.redis.transaction.aware:true}")
    private boolean transactionAware;
//
//    @Value("${cache.redis.version.aware:true}")
//    private boolean versionAware;

    @Bean
    public CacheManager cacheManager(@Qualifier(value = "cacheRedisTemplate") RedisTemplate<String, Object> cacheRedisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(cacheRedisTemplate);
        redisCacheManager.setDefaultExpiration(defaultExpiretion);
//        redisCacheManager.setExpires();
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setCachePrefix(new DefaultRedisCachePrefix());
        redisCacheManager.setTransactionAware(transactionAware);

        return new RedisCacheManager(cacheRedisTemplate);
    }
}