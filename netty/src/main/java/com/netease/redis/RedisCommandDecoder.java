package com.netease.redis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;
import java.util.UUID;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/5/30
 */
public class RedisCommandDecoder extends ReplayingDecoder<Void> {

    private byte[][] cmds;

    private int arg;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (cmds == null) {
            if (byteBuf.readByte() == '*') {
                doDecodeNumOfArgs(byteBuf);
            }
        } else {
            doDecodeArgs(byteBuf);
        }

        if (isComplete()) {
            doSendCmdToHandler(list);
            doCleanUp();
        }
    }

    private void doDecodeNumOfArgs(ByteBuf in) {
        int numOfArgs = readInt(in);
        System.out.println("RedisCommandDecoder NumOfArgs: " + numOfArgs);
        cmds = new byte[numOfArgs][];

        checkpoint();
    }

    private void doDecodeArgs(ByteBuf in) {
        for (int i = arg; i < cmds.length; i++) {
            if (in.readByte() == '$') {
                int lenOfBulkStr = readInt(in);
                System.out.println("RedisCommandDecoder LenOfBulkStr[" + i + "]: " + lenOfBulkStr);

                cmds[i] = new byte[lenOfBulkStr];
                in.readBytes(cmds[i]);

                // Skip CRLF(\r\n)
                in.skipBytes(2);

                arg++;
                checkpoint();
            } else {
                throw new IllegalStateException("Invalid argument");
            }
        }
    }

    private boolean isComplete() {
        return (cmds != null)
                && (arg > 0)
                && (arg == cmds.length);
    }

    private void doSendCmdToHandler(List<Object> out) {
        System.out.println("RedisCommandDecoder: Send command to next handler");
        if (cmds.length == 2) {
            out.add(new RedisCommand(new String(cmds[0]), cmds[1]));
        } else if (cmds.length == 3) {
            out.add(new RedisCommand(new String(cmds[0]), cmds[1], cmds[2]));
        } else {
            throw new IllegalStateException("Unknown command");
        }
    }

    private void doCleanUp() {
        this.cmds = null;
        this.arg = 0;
    }

    private int readInt(ByteBuf in) {
        int integer = 0;
        char c;
        while ((c = (char) in.readByte()) != '\r') {
            integer = (integer * 10) + (c - '0');
        }

        if (in.readByte() != '\n') {
            throw new IllegalStateException("Invalid number");
        }
        return integer;
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }
}