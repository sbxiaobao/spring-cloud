package com.dianping.cat;

import com.dianping.cat.analysis.TcpSocketReceiver;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/29
 */
public class CatHomeMain {

    public void setup() {
        TcpSocketReceiver messageReceiver = new TcpSocketReceiver();
        messageReceiver.init();

        DirectMemoryReporter reporter = new DirectMemoryReporter();
        reporter.init();

        reporter.startReport();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                messageReceiver.destory();
            }
        });
    }

    public static void main(String[] args) {
        CatHomeMain catHome = new CatHomeMain();
        catHome.setup();
    }
}