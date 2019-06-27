package com.dianping.cat.analysis;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/8
 */
public class BufReleseHelper {

    private static final Logger logger = LoggerFactory.getLogger(BufReleseHelper.class);

    public static void release(ByteBuf buf) {
        try {
            if (buf != null) {
                ReferenceCountUtil.release(buf);
            }
        } catch (Exception e) {
            logger.error("Release buf error:", e);
        }
    }
}