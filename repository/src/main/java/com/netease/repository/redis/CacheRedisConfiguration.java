package com.netease.repository.redis;

import com.netease.repository.cache.RedisCacheKeySerializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
@Configuration
public class CacheRedisConfiguration {

    @Bean(name = "cacheConnectionFactory")
    public JedisConnectionFactory cacheConnectionFactory(@Value("${redis.host:127.0.0.1}") String host,
                                                    @Value("${redis.port:6379}") int port,
                                                    @Value("${redis.password:}") String password,
                                                    @Value("${redis.pool.maxTotal:1500}") Integer maxTotal,
                                                    @Value("${redis.pool.maxIdle:200}") Integer maxIdle,
                                                    @Value("${redis.pool.minIdle:100}") Integer minIdle,
                                                    @Value("${redis.pool.maxWaitMillis:10}") Integer maxWaitMillis,
                                                    @Value("${redis.pool.testOnBorrow:false}") Boolean testOnBorrow,
                                                    @Value("${redis.pool.testOnReturn:false}") Boolean testOnReturn,
                                                    @Value("${redis.pool.timeBetweenEvictionRunsMillis:30000}") Integer timeBetweenEvictionRunsMillis,
                                                    @Value("${redis.pool.testWhileIdle:true}") Boolean testWhileIdle,
                                                    @Value("${redis.pool.numTestsPerEvictionRun:50}") Integer numTestsPerEvictionRun) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        poolConfig.setTestWhileIdle(testWhileIdle);
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
        connectionFactory.setUsePool(true);
        connectionFactory.setDatabase(0);
        connectionFactory.setHostName(host);
        connectionFactory.setPort(port);
        if (StringUtils.isNotBlank(password)) {
            connectionFactory.setPassword(password);
        }
        return connectionFactory;
    }

    @Bean(name = "cacheRedisTemplate")
    public RedisTemplate<String, Object> cacheRedisTemplate(@Qualifier(value = "cacheConnectionFactory") JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        GenericJackson2JsonRedisSerializer defaultSerializer = new GenericJackson2JsonRedisSerializer();

        RedisCacheKeySerializer redisCacheKeySerializer = new RedisCacheKeySerializer(null);
        redisTemplate.setConnectionFactory(connectionFactory);

        redisTemplate.setDefaultSerializer(redisCacheKeySerializer);
        redisTemplate.setKeySerializer(redisCacheKeySerializer);
        redisTemplate.setHashKeySerializer(redisCacheKeySerializer);
        redisTemplate.setValueSerializer(defaultSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}