package com.dianping.cat.message.internal;

import com.dianping.cat.message.Heartbeat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.spi.MessageTree;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/29
 */
public class DefaultMessageTree implements MessageTree {
    private ByteBuf buf;
    private Message message;
    private String domain;
    private String hostName;
    private String ipAddress;
    private String parentMessageId;
    private String rootMessageId;
    private String sessionToken;
    private String threadGroupName;
    private String threadId;
    private String threadName;
    private String messageId;
    private boolean discard = true;
    private boolean hitSample = false;
    private boolean processLoss = false;

    private List<Heartbeat> heartbeats;

    public void addHeartbeat(Heartbeat heartbeat) {
        if (heartbeats == null) {
            heartbeats = new ArrayList<>();
        }

        heartbeats.add(heartbeat);
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    @Override
    public String getRootMessageId() {
        return rootMessageId;
    }

    public void setRootMessageId(String rootMessageId) {
        this.rootMessageId = rootMessageId;
    }

    @Override
    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String getThreadGroupName() {
        return threadGroupName;
    }

    public void setThreadGroupName(String threadGroupName) {
        this.threadGroupName = threadGroupName;
    }

    @Override
    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    @Override
    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void setDiscardPrivate(boolean discard) {
        this.discard = discard;
    }

    @Override
    public void setHitSample(boolean hitSample) {
        this.hitSample = hitSample;
    }

    @Override
    public boolean canDiscard() {
        return discard;
    }

    public ByteBuf getBuffer() {
        return buf;
    }

    public void setBuffer(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public List<Heartbeat> getHeartbeats() {
        return heartbeats;
    }

    @Override
    public MessageTree copy() {
        MessageTree tree = new DefaultMessageTree();

        tree.setDomain(domain);
        tree.setHostName(hostName);
        tree.setIpAddress(ipAddress);
        tree.setMessageId(messageId);
        tree.setParentMessageId(parentMessageId);
        tree.setRootMessageId(rootMessageId);
        tree.setSessionToken(sessionToken);
        tree.setThreadGroupName(threadGroupName);
        tree.setThreadId(threadId);
        tree.setThreadName(threadName);
        tree.setMessage(message);
        tree.setDiscardPrivate(discard);
        tree.setHitSample(hitSample);
        return tree;
    }

    @Override
    public boolean isProcessLoss() {
        return processLoss;
    }

    @Override
    public void setProcessLoss(boolean processLoss) {
        this.processLoss = processLoss;
    }
}