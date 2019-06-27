package org.apache.flume;

import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.source.EventDriverSourceRunner;
import org.apache.flume.source.PollableSourceRunner;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public abstract class SourceRunner implements LifecycleAware {

    private Source source;

    public static SourceRunner forSource(Source source) {
        SourceRunner runner;

        if (source instanceof PollableSource) {
            runner = new PollableSourceRunner();
            runner.setSource((PollableSource) source);
        } else if (source instanceof EventDriverSource) {
            runner = new EventDriverSourceRunner();
            runner.setSource((EventDriverSource) source);
        } else {
            throw new IllegalArgumentException("No known runner type for source " + source);
        }
        return runner;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
