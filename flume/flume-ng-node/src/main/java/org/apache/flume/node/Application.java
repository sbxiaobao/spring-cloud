package org.apache.flume.node;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.common.base.Preconditions;
import org.apache.commons.cli.*;
import org.apache.flume.ChannelFactory;
import org.apache.flume.Constants;
import org.apache.flume.SinkFactory;
import org.apache.flume.SourceFactory;
import org.apache.flume.channel.DefaultChannelFactory;
import org.apache.flume.conf.file.AbstractFileConfigurationProvider;
import org.apache.flume.conf.properties.PropertiesFileConfigurationProvider;
import org.apache.flume.node.nodemanager.DefaultLogicalNodeManager;
import org.apache.flume.sink.DefaultSinkFactory;
import org.apache.flume.source.DefaultSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private String[] args;
    private File configurationFile;
    private String nodeName;

    private SourceFactory sourceFactory;
    private ChannelFactory channelFactory;
    private SinkFactory sinkFactory;

    public Application() {
        sourceFactory = new DefaultSourceFactory();
        channelFactory = new DefaultChannelFactory();
        sinkFactory = new DefaultSinkFactory();
    }

    public static void main(String[] args) throws Exception {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        configurator.doConfigure("/Users/apple/work/workspace/spring-cloud/flume/flume-ng-node/src/main/resources/logback.xml");
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);

        Application application = new Application();
        application.setArgs(args);

        try {
            if (application.parseOptions()) {
                application.run();
            }
        } catch (ParseException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("A fatal error occurred while running. Exception follows.", e);
        }
    }

    public boolean parseOptions() throws ParseException {
        Options options = new Options();

        Option option = new Option("n", "name", true, "the name of this node");
        options.addOption(option);

        option = new Option("f", "conf-file", true, "specify a conf file");
        options.addOption(option);

        option = new Option("h", "help", false, "display help text");
        options.addOption(option);

        CommandLineParser parser = new GnuParser();
        CommandLine commandLine = parser.parse(options, args);

        if (commandLine.hasOption('f')) {
            configurationFile = new File(commandLine.getOptionValue('f'));

            if (!configurationFile.exists()) {
                if (System.getProperty(Constants.SYSPROP_CALLED_FROM_SERVICE) == null) {
                    String path = configurationFile.getPath();
                    try {
                        path = configurationFile.getCanonicalPath();
                    } catch (IOException e) {
                        logger.error("Failed to read canonical path for file:" + path, e);
                    }
                    throw new ParseException("The specified configuration file does not exist: " + path);
                }
            }
        }

        if (commandLine.hasOption('n')) {
            nodeName = commandLine.getOptionValue('n');
        }

        if (commandLine.hasOption('h')) {
            new HelpFormatter().printHelp("flume-ng node", options, true);

            return false;
        }
        return true;
    }

    public void run() {
        FlumeNode node = new FlumeNode();
        DefaultLogicalNodeManager nodeManager = new DefaultLogicalNodeManager();
        AbstractFileConfigurationProvider configurationProvider = new PropertiesFileConfigurationProvider();

        configurationProvider.setChannelFactory(channelFactory);
        configurationProvider.setSourceFactory(sourceFactory);
        configurationProvider.setSinkFactory(sinkFactory);

        configurationProvider.setNodeName(nodeName);
        configurationProvider.setConfigurationAware(nodeManager);
        configurationProvider.setFile(configurationFile);

        Preconditions.checkState(configurationFile != null, "Configuration file not specified");
        Preconditions.checkState(nodeName != null, "Node name not specified");

        node.setName(nodeName);
        node.setNodeManager(nodeManager);
        node.setConfigurationProvider(configurationProvider);

        Runtime.getRuntime().addShutdownHook(new Thread("node-shutdownHook") {
            @Override
            public void run() {
                node.stop();
            }
        });

        node.start();
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }
}