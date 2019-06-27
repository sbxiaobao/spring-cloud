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
public abstract class BasicTransactionSemantics implements Transaction {

    private State state;
    private long initialThreadId;

    protected void doBegin() throws InterruptedException {
    }

    protected abstract void doPut(Event event) throws InterruptedException;

    protected abstract Event doTake() throws InterruptedException;

    protected abstract void doCommit() throws InterruptedException;

    protected abstract void doRollback() throws InterruptedException;

    protected void doClose() {
    }

    protected BasicTransactionSemantics() {
        state = State.NEW;
        initialThreadId = Thread.currentThread().getId();
    }

    protected void put(Event event) {
        Preconditions.checkState(Thread.currentThread().getId() == initialThreadId, "put() called from different thread than getTransaction()!");
        Preconditions.checkState(state.equals(State.OPEN), "put() called when transaction is%s!", state);
        Preconditions.checkArgument(event != null, "put() called with null event!");

        try {
            doPut(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ChannelException(e.toString(), e);
        }
    }

    protected Event take() {
        Preconditions.checkState(Thread.currentThread().getId() == initialThreadId, "take() called from different thread than getTransaction()!");
        Preconditions.checkState(state.equals(State.OPEN), "take() called when transaction is %s!", state);

        try {
            return doTake();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    protected State getState() {
        return state;
    }

    @Override
    public void begin() {
        Preconditions.checkState(Thread.currentThread().getId() == initialThreadId, "begin() called from different thread than getTransaction()!");
        Preconditions.checkState(state.equals(State.NEW), "begin() called when transaction is " + state + "!");

        try {
            doBegin();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ChannelException(e.toString(), e);
        }
        state = State.OPEN;
    }

    @Override
    public void commit() {
        Preconditions.checkState(Thread.currentThread().getId() == initialThreadId, "commit() called from different thread than getTransaction()!");
        Preconditions.checkState(state.equals(State.OPEN), "commit() called when transaction is %s!", state);

        try {
            doCommit();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ChannelException(e.toString(), e);
        }
        state = State.COMPLETED;
    }

    @Override
    public void rollback() {
        Preconditions.checkState(Thread.currentThread().getId() == initialThreadId, "rollback() called from different thread than getTransaction()!");
        Preconditions.checkState(state.equals(State.OPEN), "rollback() called when transaction is %s!", state);

        state = State.COMPLETED;
        try {
            doRollback();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ChannelException(e.toString(), e);
        }
    }

    @Override
    public void close() {
        Preconditions.checkState(Thread.currentThread().getId() == initialThreadId, "close() called from different thread than getTransaction()!");
        Preconditions.checkState(state.equals(State.NEW) || state.equals(State.COMPLETED), "close() called when transaction is %s" + " - you must either commit or rollback first", state);

        state = State.CLOSED;
        doClose();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BasicTransactionSemantics: {");
        builder.append(" state:").append(state);
        builder.append(" initialThreadId:").append(initialThreadId);
        builder.append(" }");
        return builder.toString();
    }

    protected static enum State {
        NEW, OPEN, COMPLETED, CLOSED;
    }
}