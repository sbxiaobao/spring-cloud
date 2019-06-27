package com.dianping.cat.status;

import com.dianping.cat.Cat;
import com.dianping.cat.configuration.ApplicationEnvironment;
import com.dianping.cat.configuration.NetworkInterfaceManager;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Heartbeat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.io.ChannelManager;
import com.dianping.cat.status.jvm.JvmInfoCollector;
import com.dianping.cat.status.jvm.ThreadInfoWriter;
import com.dianping.cat.status.model.entity.CustomInfo;
import com.dianping.cat.status.model.entity.Extension;
import com.dianping.cat.status.model.entity.StatusInfo;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.helper.Threads;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/1
 */
public class StatusUpdateTask implements Threads.Task {

    private static final Logger logger = LoggerFactory.getLogger(StatusUpdateTask.class);

    private ChannelManager channelManager = ChannelManager.getInstance();
    private boolean active = true;

    public StatusUpdateTask() {
        initialize();
    }

    private void await() {
        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
        }
    }

    private void buildExtenstion(StatusInfo status) {
        StatusExtensionRegister res = StatusExtensionRegister.getInstance();
        List<StatusExtension> extensions = res.getStatusExtension();

        for (StatusExtension extension : extensions) {
            Transaction t = Cat.newTransaction("System", "StatusExtension-" + extension.getId());

            try {
                Map<String, String> properties = extension.getProperties();

                if (properties.size() > 0) {
                    String id = extension.getId();
                    String des = extension.getDescription();
                    Extension item = status.findOrCreateExtension(id).setDescription(des);

                    for (Map.Entry<String, String> entry : properties.entrySet()) {
                        final String key = entry.getKey();
                        final String value = entry.getValue();

                        try {
                            double doubleValue = Double.parseDouble(value);

                            if (value.equalsIgnoreCase("NaN")) {
                                doubleValue = 0;
                            }

                            item.findOrCreateExtensionDetail(key).setValue(doubleValue);
                        } catch (Exception e) {
                            status.getCustomInfos().put(key, new CustomInfo().setKey(key).setValue(value));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                t.setStatus(e);
            } finally {
                t.complete();
            }
        }

        ChannelFuture future;
        if (null != (future = channelManager.channel())) {
            String localAddress = future.channel().localAddress().toString();

            status.getCustomInfos().put("localAddress", new CustomInfo().setKey("localAddress").setValue(localAddress));
            status.getCustomInfos().put("env", new CustomInfo().setKey("env").setValue(ApplicationEnvironment.ENVIRONMENT));
        }
    }

    @Override
    public String getName() {
        return "hearbeat-task";
    }

    @Override
    public void shutdown() {
        active = false;
    }

    @Override
    public void run() {
        await();
//
        String localHostAddress = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
        logger.info("Reboot {}, {}", localHostAddress, Message.SUCCESS);
        while (active) {
            buildHeartbeat(localHostAddress);
        }
    }

    private void buildHeartbeat(final String localHostAddress) {
        Transaction t = Cat.newTransaction("System", "Status");
        Heartbeat h = Cat.getProducer().newHeartbeat("Heartbeat", localHostAddress);
        StatusInfo status = new StatusInfo();
        Cat.getManager().getThreadLocalMessageTree().setDiscardPrivate(false);

        try {
            buildExtenstion(status);
            h.addData(status.toString());
            h.setStatus(Message.SUCCESS);
        } catch (Exception e) {
            h.setStatus(e);
            logger.error("Build heart beat error", e);
        } finally {
            h.complete();
        }
        String eventName = calMinuteString();

        if (logger.isDebugEnabled()) {
            logger.debug("Heartbeat jstack." + eventName, Event.SUCCESS, buildJstack());
        }

        t.setStatus(Message.SUCCESS);
        t.complete();
    }

    private String buildJstack() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();

        bean.setThreadContentionMonitoringEnabled(true);

        ThreadInfo[] threads = bean.dumpAllThreads(false, false);
        return new ThreadInfoWriter().buildThreadsInfo(threads);
    }

    private String calMinuteString() {
        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        String eventName;

        if (minute < 10) {
            eventName = "jstack-0" + minute;
        } else {
            eventName = "jstack-" + minute;
        }
        return eventName;
    }

    private void initialize() {
        JvmInfoCollector.getInstance().registerJVMCollector();
    }
}