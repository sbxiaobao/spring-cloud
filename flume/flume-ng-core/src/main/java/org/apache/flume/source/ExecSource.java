package org.apache.flume.source;

import com.google.common.base.Preconditions;
import org.apache.flume.*;
import org.apache.flume.channel.AbstractChannel;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/25
 */
public class ExecSource extends AbstractSource implements EventDriverSource, Configurable {

    private static final Logger logger = LoggerFactory.getLogger(ExecSource.class);

    private String command;

    private CounterGroup counterGroup;
    private ExecutorService executor;
    private Future<?> runnerFuture;

    @Override
    public void start() {
        logger.info("Exec source starting with command:{}", command);

        executor = Executors.newSingleThreadExecutor();
        counterGroup = new CounterGroup();

        ExecRunnble runner = new ExecRunnble();

        runner.command = command;
        runner.channelProcessor = getChannelProcessor();
        runner.counterGroup = counterGroup;

        runnerFuture = executor.submit(runner);

        super.start();

        logger.debug("Exec source started");
    }

    @Override
    public void stop() {
        logger.info("Stopping exec source with command:{}", command);

        if (runnerFuture != null) {
            logger.debug("Stopping exec runner");
            runnerFuture.cancel(true);
            logger.debug("Exec runner stopped");
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            logger.debug("Waiting for exec executor service to stop");
            try {
                executor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.debug("Interrupted while waiting for exec executor service to stop. Just exiting.");
                Thread.currentThread().interrupt();
            }
        }

        super.stop();

        logger.debug("Exec source with command:{} stopped. Metrics:{}", command, counterGroup);
    }

    @Override
    public void configure(Context context) {
        command = context.getString("command");

        Preconditions.checkState(command != null, "The parameter command must be specified");
    }

    private static class ExecRunnble implements Runnable {

        private String command;
        private ChannelProcessor channelProcessor;
        private CounterGroup counterGroup;

        @Override
        public void run() {
            BufferedReader reader = null;
            try {
                String[] commandArgs = command.split("\\s+");
                Process process = new ProcessBuilder(commandArgs).start();
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    counterGroup.incrementAndGet("exec.lines.read");
                    channelProcessor.processEvent(EventBuilder.withBody(line.getBytes()));
                }
            } catch (Exception e) {
                logger.error("Failed while running command:{} - Exception follow.", command, e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        logger.error("Failed to close reader for exec source", e);
                    }
                }
            }
        }
    }
}