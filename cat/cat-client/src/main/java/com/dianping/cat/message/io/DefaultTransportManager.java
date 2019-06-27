package com.dianping.cat.message.io;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.annotation.Named;
import org.unidal.lookup.logging.LogEnabled;
import org.unidal.lookup.logging.Logger;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/29
 */
@Named(type = TransportManager.class)
public class DefaultTransportManager implements TransportManager, Initializable, LogEnabled {

    @Override
    public void initialize() throws InitializationException {

    }

    @Override
    public void enableLogging(Logger logger) {

    }
}
