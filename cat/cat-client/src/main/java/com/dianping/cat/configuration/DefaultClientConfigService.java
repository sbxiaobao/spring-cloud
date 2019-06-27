package com.dianping.cat.configuration;

import com.dianping.cat.Cat;
import com.dianping.cat.configuration.client.entity.ClientConfig;
import com.dianping.cat.configuration.client.entity.Server;
import com.dianping.cat.configuration.client.transform.DefaultSaxParser;
import com.dianping.cat.log.CatLogger;
import com.dianping.cat.message.spi.MessageTree;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/2
 */
public class DefaultClientConfigService implements ClientConfigService {

    private ClientConfig config;
    private static CatLogger logger = CatLogger.getInstance();

    public static DefaultClientConfigService INSTANCE = new DefaultClientConfigService();
    private MessageTreeTypeParser treeParser = new MessageTreeTypeParser();

    public static DefaultClientConfigService getInstance() {
        return INSTANCE;
    }

    private DefaultClientConfigService() {
        String config = System.getProperty(Cat.CLIENT_CONFIG);

        if (StringUtils.isNotEmpty(config)) {
            try {
                this.config = DefaultSaxParser.parse(config);
                logger.info("setup cat with config:" + config);
            } catch (Exception e) {
                logger.info("error in client config:" + config, e);
            }
        }

        if (this.config == null) {
            String appName = ApplicationEnvironment.loadAppName(Cat.UNKNOWN);
//            ClientConfig defaultConfig = ApplicationEnvironment.load
        }
    }

    @Override
    public List<Server> getServers() {
        return null;
    }

    @Override
    public MessageType parseMessageType(MessageTree tree) {
        if (!tree.canDiscard()) {
            return MessageType.NORMAL_MESSAGE;
        }
        return treeParser.parseMessageType(tree);
    }
}