package com.netease.redis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/5/30
 */
public class RedisReplyEncoder extends MessageToByteEncoder<RedisReply> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RedisReply msg, ByteBuf out) throws Exception {
        System.out.println("RedisReplyEncoder: " + msg);
        msg.write(out);
    }
}