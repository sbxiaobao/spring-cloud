package org.apache.flume.node;

import com.google.common.base.Preconditions;
import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.lifecycle.LifecycleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.flume.node.LifecycleSupervisor.SupervisorPolicy.AlwaysRestartPolicy;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class FlumeNode implements LifecycleAware {

    private static final Logger logger = LoggerFactory.getLogger(FlumeNode.class);

    private String name;
    private LifecycleState lifecycleState;
    private NodeManager nodeManager;
    private ConfigurationProvider configurationProvider;
    private LifecycleSupervisor supervisor;

    public FlumeNode() {
        supervisor = new LifecycleSupervisor();
    }

    @Override
    public void start() {
        Preconditions.checkState(name != null, "Node name can not be null");
        Preconditions.checkState(nodeManager != null, "Node manager can not be null");

        logger.info("Flume node starting - {}", name);

        supervisor.start();

        logger.info("Flume node starting - {}", name);

        supervisor.supervise(nodeManager, new AlwaysRestartPolicy(), LifecycleState.START);
        supervisor.supervise(configurationProvider, new AlwaysRestartPolicy(), LifecycleState.START);

        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        supervisor.stop();
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    public void setConfigurationProvider(ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    public void setName(String name) {
        this.name = name;
    }
}
