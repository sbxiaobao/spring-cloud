package com.dianping.cat.message.internal;

import com.dianping.cat.configuration.NetworkInterfaceManager;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.io.TcpSocketSender;
import com.dianping.cat.message.spi.MessageManager;
import com.dianping.cat.message.spi.MessageTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.lookup.annotation.Named;

import java.util.Stack;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
@Named(type = MessageManager.class)
public class DefaultMessageManager implements MessageManager {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageManager.class);

    private long throttleTimes;
    private ThreadLocal<Context> context = new ThreadLocal<>();
    private String domain;
    private String hostName;
    private String ip;
    private static MessageManager INSTANCE = new DefaultMessageManager();
    private TcpSocketSender sender = TcpSocketSender.getInstance();
    private MessageIdFactory factory = MessageIdFactory.getInstance();

    private DefaultMessageManager() {
        initialize();
    }

    private void initialize() {
        domain = "127.0.0.1";
        hostName = NetworkInterfaceManager.INSTANCE.getLocalHostName().intern();
        ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress().intern();

        try {
            factory.initialize(domain);
        } catch (Exception e) {
            logger.error("Error when create mark file", e);
        }
    }

    @Override
    public void add(Message message) {
        Context ctx = getContext();

        if (ctx != null) {
            ctx.add(message);
        }
    }

    @Override
    public void setup() {
        Context ctx = new Context(domain, hostName, ip);
//        double samplingRate =
        //TODO:
        context.set(ctx);
    }

    @Override
    public boolean hasContext() {
        Context ctx = context.get();

        return ctx != null;
    }

    public static MessageManager getInstance() {
        return INSTANCE;
    }

    @Override
    public MessageTree getThreadLocalMessageTree() {
        Context ctx = context.get();

        if (ctx == null) {
            setup();
        }
        ctx = context.get();

        return ctx.tree;
    }

    @Override
    public void end(Transaction transaction) {
        Context ctx = getContext();

        if (ctx != null) {
            ctx.end(this, transaction);
        }
    }

    public void flush(MessageTree tree, boolean clearContext) {
        if (sender != null) {
            sender.send(tree);

            if (clearContext) {
                reset();
            }
        } else {
            throttleTimes++;

            if (throttleTimes % 10000 == 0 || throttleTimes == 1) {
                logger.info("Cat message is throttled! Times:{}", throttleTimes);
            }
        }
    }

    public Context getContext() {
        Context ctx = context.get();

        if (ctx != null) {
            return ctx;
        } else {
            ctx = new Context(domain, hostName, ip);

            context.set(ctx);
            return ctx;
        }
    }

    @Override
    public void reset() {
        Context ctx = context.get();

        if (ctx != null) {
            ctx.reset();
        }
    }

    class Context {
        private MessageTree tree;

        private Stack<Transaction> stack;

        public Context(String domain, String hostname, String ipAddress) {
            tree = new DefaultMessageTree();
            stack = new Stack<>();

            Thread thread = Thread.currentThread();
            String groupName = thread.getThreadGroup().getName();

            tree.setThreadGroupName(groupName);
            tree.setThreadId(String.valueOf(thread.getId()));
            tree.setDomain(domain);
            tree.setHostName(hostName);
            tree.setIpAddress(ipAddress);
        }

        public void add(Message message) {
            if (stack.isEmpty()) {
                MessageTree tree = this.tree.copy();
                tree.setMessage(message);
                flush(tree, true);
            } else {
                Transaction parent = stack.peek();
                //TODO:
            }
        }

        public boolean end(DefaultMessageManager manager, Transaction transaction) {
            if (!stack.isEmpty()) {
            }

            return false;
        }

        public void reset() {
            stack.clear();

            tree.setDomain(domain);
            tree.setIpAddress(ip);
            tree.setHostName(hostName);
            tree.setMessage(null);
            tree.setMessageId(null);
            tree.setRootMessageId(null);
            tree.setParentMessageId(null);
            tree.setSessionToken(null);
            tree.setDiscardPrivate(true);
        }
    }
}
