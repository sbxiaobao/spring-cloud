package com.netease.repository.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
@Configuration
public class RedisConfiguration {

    @Autowired
    private StringRedisSerializer stringRedisSerializer;

    @Bean(name = "redisConnectionFactory")
    public JedisConnectionFactory redisConnectionFactory(@Value("${redis.host:127.0.0.1}") String host,
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

    @Bean(name = "stringRedisTemplate")
    public RedisTemplate<String, String> redisTemplate(@Qualifier("redisConnectionFactory") JedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }
}
