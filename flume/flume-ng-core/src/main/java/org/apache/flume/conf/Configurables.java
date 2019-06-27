package org.apache.flume.conf;

import org.apache.flume.Context;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public class Configurables {

    public static boolean configure(Object target, Context context) {
        if (target instanceof Configurable) {
            ((Configurable) target).configure(context);
            return true;
        }

        return false;
    }

    public static void ensureRequiredNonNull(Context context, String... keys) {
        for (String key : keys) {
            if (!context.getParameters().containsKey(key)
                    || context.getParameters().get(key) == null) {

                throw new IllegalArgumentException("Required parameter " + key
                        + " must exist and may not be null");
            }
        }
    }

    public static void ensureOptionalNonNull(Context context, String... keys) {
        for (String key : keys) {
            if (context.getParameters().containsKey(key)
                    && context.getParameters().get(key) == null) {

                throw new IllegalArgumentException("Optional parameter " + key
                        + " may not be null");
            }
        }
    }
}