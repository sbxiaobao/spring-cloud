package org.apache.flume.channel;

import com.google.common.base.Preconditions;
import org.apache.flume.ChannelException;
import org.apache.flume.Event;
import org.apache.flume.Transaction;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public abstract class BasicChannelSemantics extends AbstractChannel {

    private ThreadLocal<BasicTransactionSemantics> currentTransaction = new ThreadLocal<>();

    private boolean initialized = false;

    protected void initialized() {
    }

    protected abstract BasicTransactionSemantics createTransaction();

    @Override
    public void put(Event event) throws ChannelException {
        BasicTransactionSemantics transaction = currentTransaction.get();
        Preconditions.checkState(transaction != null, "No transaction exists for this thread");
        transaction.put(event);
    }

    @Override
    public Event take() throws ChannelException {
        BasicTransactionSemantics transaction = currentTransaction.get();
        Preconditions.checkState(transaction != null, "No transaction exists for this thread");
        return transaction.take();
    }

    @Override
    public Transaction getTransaction() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    initialized();
                    initialized = true;
                }
            }
        }

        BasicTransactionSemantics transaction = currentTransaction.get();
        if (transaction == null || transaction.getState().equals(BasicTransactionSemantics.State.CLOSED)) {
            transaction = createTransaction();
            currentTransaction.set(transaction);
        }
        return transaction;
    }
}