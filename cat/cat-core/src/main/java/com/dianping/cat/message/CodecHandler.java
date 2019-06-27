package com.dianping.cat.message;

import com.dianping.cat.message.spi.MessageCodec;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.message.spi.codec.NativeMessageCodec;
import io.netty.buffer.ByteBuf;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/15
 */
public class CodecHandler {

    private static MessageCodec nativeCodec = new NativeMessageCodec();

    public static MessageTree decode(ByteBuf buf) {
        byte[] data = new byte[3];
        MessageTree tree = null;

        buf.getBytes(4, data);
        String hint = new String(data);
        buf.resetReaderIndex();

        if ("PT1".equals(hint)) {
            //TODO:
        } else if ("NT1".equals(hint)) {
            tree = nativeCodec.decode(buf);
        } else {
            throw new RuntimeException("Error message type: " + hint);
        }

        return tree;
    }
}
