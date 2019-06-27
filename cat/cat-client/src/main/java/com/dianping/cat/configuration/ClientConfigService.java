package com.dianping.cat.configuration;

import com.dianping.cat.configuration.client.entity.Server;
import com.dianping.cat.message.spi.MessageTree;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/2
 */
public interface ClientConfigService {

    List<Server> getServers();

    MessageType parseMessageType(MessageTree tree);
}