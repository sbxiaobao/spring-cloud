package com.dianping.cat.status;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/1
 */
public abstract class AbstraceCollector implements StatusExtension{

    protected Map<String, String> convert(Map<String, Number> map) {
        Map<String, String> result = new LinkedHashMap<String, String>();

        for (Map.Entry<String, Number> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }

    public String getDescription() {
        return getId();
    }
}