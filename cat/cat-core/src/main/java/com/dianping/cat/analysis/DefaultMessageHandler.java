package com.dianping.cat.analysis;

import com.dianping.cat.message.spi.MessageTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class DefaultMessageHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageHandler.class);

    private static final DefaultMessageHandler INSTANCE = new DefaultMessageHandler();

    private MessageConsumer consumer = RealtimeConsumer.getInstance();

    public static DefaultMessageHandler getInstance() {
        return INSTANCE;
    }

    private DefaultMessageHandler() {
    }

    @Override
    public void handle(MessageTree message) {
        try {
            consumer.consume(message);
        } catch (Exception e) {
            logger.error("Error when consuming message in {}! tree:{}", consumer, message, e);
        }
    }
}