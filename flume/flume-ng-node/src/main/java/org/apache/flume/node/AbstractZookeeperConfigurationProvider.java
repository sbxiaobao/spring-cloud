package org.apache.flume.node;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/27
 */
public class AbstractZookeeperConfigurationProvider {

    static final String DEFAULT_ZK_BASE_PATH = "flume";

    protected final String basePath;
    protected final String zkConnString;

    protected AbstractZookeeperConfigurationProvider(String agentName, String zkConnString, String basePath) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(zkConnString), "Invalid Zookeeper Connection String %s", zkConnString);
        this.zkConnString = zkConnString;
        if (basePath == null || basePath.isEmpty()) {
            this.basePath = DEFAULT_ZK_BASE_PATH;
        } else {
            this.basePath = basePath;
        }
    }

    protected CuratorFramework createClient() {
        return CuratorFrameworkFactory.newClient(zkConnString, new ExponentialBackoffRetry(1000, 1));
    }
}
