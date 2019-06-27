package com.netease.repository.cache;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class RedisCacheKeySerializer implements RedisSerializer {

    private String prefix;

    private String separator = ".";

    public RedisCacheKeySerializer(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            this.prefix = prefix;
        }
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        Assert.notNull(o, "Null key to serialize");
        if (StringUtils.isNotBlank(prefix)) {
            return (prefix + separator + o).toString().getBytes(StandardCharsets.UTF_8);
        } else {
            return o.toString().getBytes(StandardCharsets.UTF_8);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        String key = bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
        if (StringUtils.isNotBlank(prefix)) {
            return StringUtils.replace(key, prefix + separator, "", 1);
        } else {
            return key;
        }
    }
}
