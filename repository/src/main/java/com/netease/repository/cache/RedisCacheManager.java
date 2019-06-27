package com.netease.repository.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class RedisCacheManager extends org.springframework.data.redis.cache.RedisCacheManager {

    private boolean cacheNullValues = true;

    public RedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    public RedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        super(redisOperations, cacheNames);
    }

    public RedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames, boolean cacheNullValues) {
        super(redisOperations, cacheNames, cacheNullValues);
    }

    @Override
    protected RedisCache createCache(String cacheName) {
        long expiration = computeExpiration(cacheName);
        return new RedisCache(cacheName, (super.isUsePrefix() ? super.getCachePrefix().prefix(cacheName) : null), super.getRedisOperations(), expiration,
                cacheNullValues);
    }
}
