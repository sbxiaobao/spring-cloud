package com.dianping.cat.configuration;

import com.dianping.cat.Cat;
import com.dianping.cat.configuration.client.entity.ClientConfig;
import com.dianping.cat.configuration.client.transform.DefaultSaxParser;
import com.dianping.cat.log.CatLogger;
import org.apache.commons.lang.StringUtils;
import org.unidal.helper.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/2
 */
public class ApplicationEnvironment {

    private static final String HOST = "org.cat";
    private static final String PROPERTIES_FILE = "/META-INF/app.properties";
    private static final String CACHE_FILE = "client_cache.xml";
    private static final String CLIENT_FILE = "client.xml";
    public static final String ENVIRONMENT;
    public static final String CELL;
    public static final String VERSION = "2.0.1";

    static {
        String env;
        String cell;

        try {
            String file = "/data/webapps/appenv";
            Properties pro = new Properties();

            pro.load(new FileInputStream(new File(file)));
            env = pro.getProperty("env");

            if (StringUtils.isEmpty(env)) {
                env = pro.getProperty("deployenv");
            }

            cell = pro.getProperty("cell");
        } catch (Exception e) {
            env = Cat.UNKNOWN;
            cell = "";
        }

        if (env == null) {
            env = Cat.UNKNOWN;
        }

        if (cell == null) {
            cell = "";
        }

        ENVIRONMENT = env.trim();
        CELL = cell.trim();
    }

    public static String loadAppName(String defaultDomain) {
        String appName = null;
        InputStream in = null;

        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE);

            if (in == null) {
                in = Cat.class.getResourceAsStream(PROPERTIES_FILE);
            }
            if (in != null) {
                Properties prop = new Properties();

                prop.load(in);

                appName = prop.getProperty("app.name");

                if (appName != null) {
                    return appName;
                }
            }
        } catch (Exception e) {
            // ignore
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return defaultDomain;
    }

    private static boolean isDevMode() {
        String devMode = org.unidal.helper.Properties.forString().fromEnv().fromSystem().getProperty("devMode", "false");
        return "true".equals(devMode);
    }

    public static ClientConfig loadClientConfig(String domain) {
        String xml = null;

        try {
            File cacheFile = new File(Cat.getCatHome() + CACHE_FILE);
            File configFile = new File(Cat.getCatHome() + CLIENT_FILE);

            if (cacheFile.exists() && !isDevMode()) {
                xml = Files.forIO().readFrom(cacheFile, "utf-8");
            } else if (configFile.exists()) {
                xml = Files.forIO().readFrom(configFile, "utf-8");
            } else {
//                xml = ApplicationEnvironment.loadremo
            }

            ClientConfig config = DefaultSaxParser.parse(xml);

            config.setDomain(domain);

            return config;
        } catch (Exception e) {
            CatLogger.getInstance().info("load client config error: " + xml, e);

            File cacheFile = new File(Cat.getCatHome() + CACHE_FILE);

            if (cacheFile.exists()) {
                cacheFile.delete();

                return loadClientConfig(domain);
            }

            throw new RuntimeException("Error when get cat router service, please contact cat support team for help!", e);
        }
    }
}
