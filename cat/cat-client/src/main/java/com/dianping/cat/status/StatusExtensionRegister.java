package com.dianping.cat.status;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/1
 */
public class StatusExtensionRegister {

    private List<StatusExtension> extensions = new CopyOnWriteArrayList<>();
    private static final StatusExtensionRegister register = new StatusExtensionRegister();

    public static StatusExtensionRegister getInstance() {
        return register;
    }

    private StatusExtensionRegister() {
    }

    public List<StatusExtension> getStatusExtension() {
        return extensions;
    }

    public void register(StatusExtension extension) {
        extensions.add(extension);
    }

    public void unregister(StatusExtension extension) {
        extensions.remove(extension);
    }
}
