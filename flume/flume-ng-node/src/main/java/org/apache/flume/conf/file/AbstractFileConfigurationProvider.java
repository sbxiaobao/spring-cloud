package org.apache.flume.conf.file;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.flume.ChannelFactory;
import org.apache.flume.CounterGroup;
import org.apache.flume.SinkFactory;
import org.apache.flume.SourceFactory;
import org.apache.flume.lifecycle.LifecycleState;
import org.apache.flume.node.ConfigurationProvider;
import org.apache.flume.node.nodemanager.NodeConfigurationAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/18
 */
public abstract class AbstractFileConfigurationProvider implements ConfigurationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private File file;
    private ChannelFactory channelFactory;
    private SourceFactory sourceFactory;
    private SinkFactory sinkFactory;
    private String nodeName;
    private NodeConfigurationAware configurationAware;

    private LifecycleState lifecycleState;
    private ScheduledExecutorService executorService;
    private CounterGroup counterGroup;

    public AbstractFileConfigurationProvider() {
        lifecycleState = LifecycleState.IDLE;
        counterGroup = new CounterGroup();
    }

    @Override
    public void start() {
        logger.info("Configuration provider starting");

        Preconditions.checkState(file != null, "The parameter file must not be null");

        executorService = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("conf-file-poller-%d").build());

        FileWatcherRunnable fileWatcherRunnable = new FileWatcherRunnable();

        fileWatcherRunnable.file = file;
        fileWatcherRunnable.counterGroup = counterGroup;

        executorService.scheduleAtFixedRate(fileWatcherRunnable, 0, 30, TimeUnit.SECONDS);

        lifecycleState = LifecycleState.START;

        logger.debug("Configuration provider started");
    }

    @Override
    public void stop() {
        logger.info("Configuration provider stopping");

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            try {
                logger.debug("Waiting for file watcher to terminate");
                executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.debug("Interrupted while waiting for file watcher to terminate");
                Thread.currentThread().interrupt();
            }
        }

        lifecycleState = LifecycleState.STOP;

        logger.debug("Configuration provider stopped");
    }

    protected abstract void load();

    private synchronized void doLoad() {
        Preconditions.checkState(nodeName != null, "No node name specified - Unable to determine what part of the config to load");
        Preconditions.checkState(channelFactory != null, "No channel factory configured");
        Preconditions.checkState(sourceFactory != null, "No source factory configured");

        load();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    public SourceFactory getSourceFactory() {
        return sourceFactory;
    }

    public ChannelFactory getChannelFactory() {
        return channelFactory;
    }

    public SinkFactory getSinkFactory() {
        return sinkFactory;
    }

    public class FileWatcherRunnable implements Runnable {

        private File file;
        private CounterGroup counterGroup;

        private long lastChange;

        @Override
        public void run() {
            logger.debug("Checking file:{} for changes", file);
            counterGroup.incrementAndGet("file.checks");

            long lastModified = file.lastModified();

            if (lastModified > lastChange) {
                logger.info("Reloading configuration file:{}", file);

                counterGroup.incrementAndGet("file.loads");

                lastChange = lastModified;

                try {
                    doLoad();
                } catch (Exception e) {
                    logger.error("Failed to load configuration data. Exception follows", e);
                }
            }
        }
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public NodeConfigurationAware getConfigurationAware() {
        return configurationAware;
    }

    public void setConfigurationAware(NodeConfigurationAware configurationAware) {
        this.configurationAware = configurationAware;
    }

    public void setSinkFactory(SinkFactory sinkFactory) {
        this.sinkFactory = sinkFactory;
    }

    public void setChannelFactory(ChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    public void setSourceFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public String toString() {
        return "{ file:" + file + " counterGroup:" + counterGroup + "  provider:"
                + getClass().getCanonicalName() + " nodeName:" + nodeName + " }";
    }
}