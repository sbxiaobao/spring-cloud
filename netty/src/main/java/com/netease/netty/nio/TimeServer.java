package com.netease.netty.nio;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/11
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
