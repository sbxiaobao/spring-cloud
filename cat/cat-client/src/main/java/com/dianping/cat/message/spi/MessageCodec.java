package com.dianping.cat.message.spi;

import io.netty.buffer.ByteBuf;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/9
 */
public interface MessageCodec {

    MessageTree decode(ByteBuf buf);

    ByteBuf encode(MessageTree tree);

    void reset();
}