package com.dianping.cat;

import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import sun.lwawt.PlatformEventNotifier;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/5/14
 */
public class DirectMemoryReporter {

    private static final Logger logger = LoggerFactory.getLogger(DirectMemoryReporter.class);

    private static final int _1k = 1024;
    private final static String BUSINESS_KEY = "netty_direct_memory";

    private AtomicLong directMemory;

    public void init() {
        Field field = ReflectionUtils.findField(PlatformDependent.class, "DIRECT_MEMORY_COUNTER");
        field.setAccessible(true);

        try {
            directMemory = (AtomicLong) field.get(PlatformEventNotifier.class);
        } catch (IllegalAccessException e) {

        }
    }

    public void startReport() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::doReport, 0, 1, TimeUnit.SECONDS);
    }

    private void doReport() {
        int memoryInKb = (int) (directMemory.get() / _1k);
        logger.info("{}: {}k", BUSINESS_KEY, memoryInKb);
    }
}
