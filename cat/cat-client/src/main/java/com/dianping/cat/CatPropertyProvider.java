package com.dianping.cat;

import java.util.ServiceLoader;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public interface CatPropertyProvider {
    CatPropertyProvider INST = ServiceLoader.load(CatPropertyProvider.class).iterator().next();

    String getProperty(String name, String defaultValue);
}
