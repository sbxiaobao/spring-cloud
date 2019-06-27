package com.netease.redis;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/5/30
 */
public interface RedisReply<T> {

    byte[] CRLF = new byte[]{'\r', '\n'};

    T data();

    void write(ByteBuf out) throws IOException;
}