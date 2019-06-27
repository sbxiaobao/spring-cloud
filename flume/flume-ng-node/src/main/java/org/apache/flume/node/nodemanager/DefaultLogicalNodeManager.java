package org.apache.flume.node.nodemanager;

import org.apache.flume.SinkRunner;
import org.apache.flume.SourceRunner;
import org.apache.flume.lifecycle.LifecycleState;
import org.apache.flume.node.LifecycleSupervisor;

import static org.apache.flume.node.LifecycleSupervisor.SupervisorPolicy.AlwaysRestartPolicy;

import org.apache.flume.node.NodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class DefaultLogicalNodeManager extends AbstractLogicalNodeManager implements NodeConfigurationAware {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLogicalNodeManager.class);

    private LifecycleSupervisor nodeSupervisor;
    private LifecycleState lifecycleState;

    public DefaultLogicalNodeManager() {
        nodeSupervisor = new LifecycleSupervisor();
        lifecycleState = LifecycleState.IDLE;
    }

    @Override
    public void start() {
        logger.info("Node manager starting");

        nodeSupervisor.start();

        logger.debug("Node manager started");

        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        logger.info("Node manager stopping");

        nodeSupervisor.stop();

        logger.debug("Node maanger stopped");

        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public void onNodeConfigurationChanged(NodeConfiguration nodeConfiguration) {
        logger.info("Node configuration change:{}", nodeConfiguration);

        for (Map.Entry<String, SinkRunner> entry : nodeConfiguration.getSinkRunners().entrySet()) {
            nodeSupervisor.supervise(entry.getValue(), new AlwaysRestartPolicy(), LifecycleState.START);
        }

        for (Map.Entry<String, SourceRunner> entry : nodeConfiguration.getSourceRunners().entrySet()) {
            nodeSupervisor.supervise(entry.getValue(), new AlwaysRestartPolicy(), LifecycleState.START);
        }
    }
}