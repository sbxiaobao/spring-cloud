package com.dianping.cat.status;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/1
 */
public interface StatusExtension {

    String getDescription();

    String getId();

    Map<String, String> getProperties();
}
