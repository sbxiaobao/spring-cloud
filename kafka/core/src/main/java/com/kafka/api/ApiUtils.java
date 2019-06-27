package com.kafka.api;

import com.kafka.common.KafkaException;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public class ApiUtils {

    private static final String ProtocolEncoding = "UTF-8";

    public static String readShortString(ByteBuffer buffer) {
        int size = buffer.getShort();
        if (size < 0) {
            return null;
        }
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        try {
            return new String(bytes, ProtocolEncoding);
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        return null;
    }

    public static void writeShortString(ByteBuffer buffer, String string) {
        if (string == null) {
            buffer.putShort((short) -1);
        } else {
            try {
                byte[] encodedString = string.getBytes(ProtocolEncoding);
                if (encodedString.length > Short.MAX_VALUE) {
                    throw new KafkaException("String exceeds the maximum size of " + Short.MAX_VALUE + ".");
                } else {
                    buffer.putShort((short) encodedString.length);
                    buffer.put(encodedString);
                }
            } catch (Exception e) {
                throw new KafkaException(e);
            }
        }
    }

    public static int shortStringLength(String string) {
        if (string == null) {
            return 2;
        }
        try {
            byte[] encodeString = string.getBytes(ProtocolEncoding);
            if (encodeString.length > Short.MAX_VALUE) {
                throw new KafkaException("String exceeds the maximum size of " + Short.MAX_VALUE + ".");
            }
            return 2 + encodeString.length;
        } catch (Exception e) {
            throw new KafkaException(e);
        }
    }
}