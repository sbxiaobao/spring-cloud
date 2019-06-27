package com.dianping.cat.message.spi;

import com.dianping.cat.message.Heartbeat;
import com.dianping.cat.message.Message;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/29
 */
public interface MessageTree extends Cloneable {

    boolean canDiscard();

    MessageTree copy();

    void setDomain(String domain);

    String getDomain();

    void setHostName(String hostName);

    String getHostName();

    void setIpAddress(String ipAddress);

    String getIpAddress();

    String getThreadGroupName();

    void setThreadGroupName(String threadGroupName);

    String getThreadId();

    void setThreadId(String threadId);

    String getThreadName();

    void setThreadName(String threadName);

    String getParentMessageId();

    void setParentMessageId(String parentMessageId);

    String getRootMessageId();

    void setRootMessageId(String rootMessageId);

    String getSessionToken();

    void setSessionToken(String sessionToken);

    Message getMessage();

    void setMessage(Message message);

    String getMessageId();

    void setMessageId(String messageId);

    void setDiscardPrivate(boolean discard);

    void setHitSample(boolean hitSample);

    List<Heartbeat> getHeartbeats();

    boolean isProcessLoss();

    void setProcessLoss(boolean loss);
}
