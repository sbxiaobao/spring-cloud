package com.dianping.cat.impl;

import com.dianping.cat.CatPropertyProvider;
import org.unidal.helper.Properties;
import org.unidal.helper.Properties.PropertyAccessor;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class CatPropertyProviderDefaultImpl implements CatPropertyProvider {

    private PropertyAccessor<String> config;

    public CatPropertyProviderDefaultImpl() {
        super();
        config = Properties.forString().fromEnv().fromSystem();
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        return config.getProperty(name, defaultValue);
    }
}