package org.apache.flume;

import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.lifecycle.LifecycleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public class SinkRunner implements LifecycleAware {
    private static final Logger logger = LoggerFactory
            .getLogger(SinkRunner.class);
    private static final long backoffSleepIncrement = 1000;
    private static final long maxBackoffSleep = 5000;

    private CounterGroup counterGroup;
    private PollingRunner runner;
    private Thread runnerThread;
    private LifecycleState lifecycleState;

    private SinkProcessor policy;

    public SinkRunner() {
        counterGroup = new CounterGroup();
        lifecycleState = LifecycleState.IDLE;
    }

    public SinkRunner(SinkProcessor policy) {
        this();
        setSink(policy);
    }

    public SinkProcessor getPolicy() {
        return policy;
    }

    public void setSink(SinkProcessor policy) {
        this.policy = policy;
    }

    @Override
    public void start() {
        SinkProcessor policy = getPolicy();

        policy.start();

        runner = new PollingRunner();

        runner.policy = policy;
        runner.counterGroup = counterGroup;
        runner.shouldStop = new AtomicBoolean();

        runnerThread = new Thread(runner);
        runnerThread.setName("SinkRunner-PollingRunner-" +
                policy.getClass().getSimpleName());
        runnerThread.start();

        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {

        getPolicy().stop();

        if (runnerThread != null) {
            runner.shouldStop.set(true);
            runnerThread.interrupt();

            while (runnerThread.isAlive()) {
                try {
                    logger.debug("Waiting for runner thread to exit");
                    runnerThread.join(500);
                } catch (InterruptedException e) {
                    logger
                            .debug(
                                    "Interrupted while waiting for runner thread to exit. Exception follows.",
                                    e);
                }
            }
        }

        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public String toString() {
        return "SinkRunner: { policy:" + getPolicy() + " counterGroup:"
                + counterGroup + " }";
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    /**
     * {@link Runnable} that {@linkplain SinkProcessor#process() polls} a
     * {@link SinkProcessor} and manages event delivery notification,
     * {@link Sink.Status BACKOFF} delay handling, etc.
     */
    public static class PollingRunner implements Runnable {

        private SinkProcessor policy;
        private AtomicBoolean shouldStop;
        private CounterGroup counterGroup;

        @Override
        public void run() {
            logger.debug("Polling sink runner starting");

            while (!shouldStop.get()) {
                try {
                    if (policy.process().equals(Sink.Status.BACKOFF)) {
                        counterGroup.incrementAndGet("runner.backoffs");

                        Thread.sleep(Math.min(
                                counterGroup.incrementAndGet("runner.backoffs.consecutive")
                                        * backoffSleepIncrement, maxBackoffSleep));
                    } else {
                        counterGroup.set("runner.backoffs.consecutive", 0L);
                    }
                } catch (InterruptedException e) {
                    logger.debug("Interrupted while processing an event. Exiting.");
                    counterGroup.incrementAndGet("runner.interruptions");
                } catch (EventDeliveryException e) {
                    logger.error("Unable to deliver event. Exception follows.", e);
                    counterGroup.incrementAndGet("runner.deliveryErrors");
                } catch (Exception e) {
                    counterGroup.incrementAndGet("runner.errors");
                    logger.error("Unhandled exception, logging and sleeping for " +
                            maxBackoffSleep + "ms", e);
                    try {
                        Thread.sleep(maxBackoffSleep);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            logger.debug("Polling runner exiting. Metrics:{}", counterGroup);
        }

    }
}
