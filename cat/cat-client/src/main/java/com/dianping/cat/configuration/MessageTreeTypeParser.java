package com.dianping.cat.configuration;

import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.spi.MessageTree;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/10
 */
public class MessageTreeTypeParser {

    private ConcurrentHashMap<String, MessageType> messageTypes = new ConcurrentHashMap<>();

    public MessageType parseMessageType(MessageTree tree) {
        Message message = tree.getMessage();

        if (message instanceof Transaction) {
            String type = message.getType();
            MessageType messageType = messageTypes.get(type);

            if (messageType != null) {
                return messageType;
            } else {
                if (messageType == null) {
                    messageType = messageType.SMALL_TRANSACTION;
                }

                messageTypes.put(type, messageType);
                return messageType;
            }
        } else if (message instanceof Event) {
            return MessageType.STAND_ALONE_EVENT;
        }
        return MessageType.NORMAL_MESSAGE;
    }
}