package com.dianping.cat;

import com.dianping.cat.configuration.ApplicationEnvironment;
import com.dianping.cat.configuration.ClientConfigProvider;
import com.dianping.cat.configuration.client.entity.ClientConfig;
import com.dianping.cat.configuration.client.transform.DefaultSaxParser;
import com.dianping.cat.log.CatLogger;
import com.dianping.cat.message.MessageProducer;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultMessageManager;
import com.dianping.cat.message.internal.DefaultMessageProducer;
import com.dianping.cat.message.io.TcpSocketSender;
import com.dianping.cat.message.spi.MessageManager;
import com.dianping.cat.status.StatusUpdateTask;
import org.apache.commons.lang.StringUtils;
import org.unidal.helper.Properties;
import org.unidal.helper.Threads;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class Cat {
    private static MessageProducer producer;
    private static MessageManager manager;
    private static int errorCount;
    private static final Cat instance = new Cat();
    private static volatile boolean init = false;
    private static volatile boolean enabled = true;
    public final static String CLIENT_CONFIG = "cat-client-config";
    public final static String UNKNOWN = "unknown";

    public static boolean isEnabled() {
        return enabled;
    }

    public static void checkAndInitialize() {
        try {
            if (!init) {
                ClientConfig clientConfig = getSpiClientConfig();
                if (clientConfig == null) {
                    initializeInternal();
                } else {
                    initializeInternal(clientConfig);
                }
            }
        } catch (Exception e) {
            errorHandler(e);
        }
    }

    private static void errorHandler(Exception e) {
        if (isEnabled() && errorCount < 3) {
            errorCount++;
            CatLogger.getInstance().error(e.getMessage(), e);
        }
    }

    public static MessageProducer getProducer() {
        try {
            checkAndInitialize();

            if (producer != null) {
                return producer;
            } else {
                return null;
            }
        } catch (Exception e) {
            errorHandler(e);
            return null;
        }
    }

    public static MessageManager getManager() {
        try {
            checkAndInitialize();

            if (manager != null) {
                return manager;
            } else {
                return null;
            }
        } catch (Exception e) {
            errorHandler(e);
            return null;
        }
    }

    private static ClientConfig getSpiClientConfig() {
        ServiceLoader<ClientConfigProvider> clientConfigProviders = ServiceLoader.load(ClientConfigProvider.class);
        if (clientConfigProviders == null) {
            return null;
        }

        Iterator<ClientConfigProvider> iterator = clientConfigProviders.iterator();
        if (iterator.hasNext()) {
            ClientConfigProvider clientConfigProvider = (ClientConfigProvider) iterator.next();
            return clientConfigProvider.getClientConfig();
        }
        return null;
    }

    private static void initializeInternal() {
//        validate();

        if (isEnabled()) {
            try {
                if (!init) {
                    synchronized (instance) {
                        if (!init) {
                            producer = DefaultMessageProducer.getInstance();
                            manager = DefaultMessageManager.getInstance();
                            StatusUpdateTask heartbeatTask = new StatusUpdateTask();
                            TcpSocketSender messageSender = TcpSocketSender.getInstance();

                            Threads.forGroup().start(heartbeatTask);
                            Threads.forGroup().start(messageSender);

                            CatLogger.getInstance().info("Cat is lazy initialized!");
                            init = true;
                        }
                    }
                }
            } catch (Exception e) {
                errorHandler(e);
                disable();
            }
        }
    }

    private static void initializeInternal(ClientConfig config) {
        if (isEnabled()) {
            System.setProperty(Cat.CLIENT_CONFIG, config.toString());
            CatLogger.getInstance().info("init cat with config:" + config.toString());

            initializeInternal();
        }
    }

    private static void validate() {
        String enable = Properties.forString().fromEnv().fromSystem().getProperty("CAT_ENABLED", "true");

        if ("false".equals(enable)) {
            CatLogger.getInstance().info("CAT is disable due to system environment CAT_ENABLED is false.");
            enabled = false;
        } else {
            String customDomain = getCustomDomain();

            if (customDomain == null && UNKNOWN.equals(ApplicationEnvironment.loadAppName(UNKNOWN))) {
                CatLogger.getInstance().info("CAT is disable due to no app name in resource file /META-INF/app.properties");
                enabled = false;
            }
        }
    }

    public static void disable() {
        enabled = false;
    }

    private static String getCustomDomain() {
        String config = System.getProperty(Cat.CLIENT_CONFIG);

        if (StringUtils.isNotEmpty(config)) {
            try {
                ClientConfig clientConfig = DefaultSaxParser.parse(config);

                return clientConfig.getDomain();
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    public static Transaction newTransaction(String type, String name) {
        if (isEnabled()) {
            try {
                return Cat.getProducer().newTransaction(type, name);
            } catch (Exception e) {
                errorHandler(e);
                return null;
            }
        } else {
            return null;
        }
    }

    public static void logEvent(String type, String name) {
        if (isEnabled()) {
            try {
                Cat.getProducer().logEvent(type, name);
            } catch (Exception e) {
                errorHandler(e);
            }
        }
    }

    public static String getCatHome() {
        return Properties.forString().fromEnv().fromSystem().getProperty("CAT_HOME", "/data/appdatas/cat/");
    }

    public static void main(String[] args) {
        Transaction transaction = Cat.newTransaction("App", "service");

        Cat.logEvent("demo", "demo");
        transaction.setStatus(Transaction.SUCCESS);
        transaction.complete();
    }
}