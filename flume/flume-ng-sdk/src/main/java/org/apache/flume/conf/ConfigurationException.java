package org.apache.flume.conf;

import org.apache.flume.FlumeException;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/27
 */
public class ConfigurationException extends FlumeException {

    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(Throwable t) {
        super(t);
    }

    public ConfigurationException(String msg, Throwable t) {
        super(msg, t);
    }
}
