package org.apache.flume.conf.properties;

import org.apache.flume.*;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.channel.ChannelSelectorFactory;
import org.apache.flume.conf.Configurables;
import org.apache.flume.conf.file.AbstractFileConfigurationProvider;
import org.apache.flume.conf.file.SimpleNodeConfiguration;
import org.apache.flume.node.NodeConfiguration;
import org.apache.flume.sink.DefaultSinkProcessor;
import org.apache.flume.sink.SinkGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.apache.flume.conf.properties.FlumeConfiguration.AgentConfiguration;
import static org.apache.flume.conf.properties.FlumeConfiguration.ComponentConfiguration;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/18
 */
public class PropertiesFileConfigurationProvider extends AbstractFileConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PropertiesFileConfigurationProvider.class);

    @Override
    protected void load() {
        File propertiesFile = getFile();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(propertiesFile));
            Properties properties = new Properties();
            properties.load(reader);

            NodeConfiguration conf = new SimpleNodeConfiguration();
            FlumeConfiguration fconfig = new FlumeConfiguration(properties);
            AgentConfiguration agentConf = fconfig.getConfigurationFor(getNodeName());

            if (agentConf != null) {
                loadChannels(agentConf, conf);
                loadSources(agentConf, conf);
                loadSinks(agentConf, conf);

                getConfigurationAware().onNodeConfigurationChanged(conf);
            } else {
                LOGGER.warn("No configuration found for this host:{}", getNodeName());
            }
        } catch (IOException ex) {
            LOGGER.error("Unable to load file:" + propertiesFile
                    + " (I/O failure) - Exception follows.", ex);
        } catch (InstantiationException ex) {
            LOGGER.error("Unable to load file:" + propertiesFile
                    + " (failed to instantiate component) - Exception follows.", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOGGER.warn(
                            "Unable to close file reader for file: " + propertiesFile, ex);
                }
            }
        }
    }

    private void loadChannels(AgentConfiguration agentConf,
                              NodeConfiguration conf) throws InstantiationException {

        for (ComponentConfiguration comp : agentConf.getChannels()) {
            Context context = new Context();

            Channel channel = getChannelFactory().create(comp.getComponentName(),
                    comp.getConfiguration().get("type"));

            for (Map.Entry<String, String> entry : comp.getConfiguration().entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }

            Configurables.configure(channel, context);

            conf.getChannels().put(comp.getComponentName(), channel);
        }
    }

    private void loadSources(AgentConfiguration agentConf, NodeConfiguration conf)
            throws InstantiationException {

        for (ComponentConfiguration comp : agentConf.getSources()) {
            Context context = new Context();

            Map<String, String> componentConfig = comp.getConfiguration();

            Source source = getSourceFactory().create(comp.getComponentName(),
                    componentConfig.get("type"));

            for (Map.Entry<String, String> entry : comp.getConfiguration().entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }

            Configurables.configure(source, context);

            String channelNames = comp.getConfiguration().get("channels");
            List<Channel> channels = new ArrayList<>();

            for (String chName : channelNames.split(" ")) {
                channels.add(conf.getChannels().get(chName));
            }

            Map<String, String> selectorConfig = comp.getSubconfiguration("selector");

            ChannelSelector selector = ChannelSelectorFactory.create(
                    channels, selectorConfig);

            ChannelProcessor channelProcessor = new ChannelProcessor(selector);

            source.setChannelProcessor(channelProcessor);
            conf.getSourceRunners().put(comp.getComponentName(),
                    SourceRunner.forSource(source));
        }
    }

    private void loadSinks(AgentConfiguration agentConf, NodeConfiguration conf)
            throws InstantiationException {

        Map<String, Sink> sinks = new HashMap<String, Sink>();
        for (ComponentConfiguration comp : agentConf.getSinks()) {
            Context context = new Context();
            Map<String, String> componentConfig = comp.getConfiguration();


            Sink sink = getSinkFactory().create(comp.getComponentName(),
                    componentConfig.get("type"));

            for (Map.Entry<String, String> entry : comp.getConfiguration().entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }

            Configurables.configure(sink, context);

            sink.setChannel(conf.getChannels().get(
                    componentConfig.get("channel")));
            sinks.put(comp.getComponentName(), sink);
        }

        loadSinkGroups(agentConf, sinks, conf);
    }

    private void loadSinkGroups(AgentConfiguration agentConf,
                                Map<String, Sink> sinks, NodeConfiguration conf)
            throws InstantiationException {
        Map<String, String> usedSinks = new HashMap<String, String>();
        for (ComponentConfiguration comp : agentConf.getSinkGroups()) {
            Context context = new Context();
            String groupName = comp.getComponentName();
            Map<String, String> groupConf = comp.getConfiguration();
            for (Map.Entry<String, String> ent : groupConf.entrySet()) {
                context.put(ent.getKey(), ent.getValue());
            }
            String groupSinkList = groupConf.get("sinks");
            StringTokenizer sinkTokenizer = new StringTokenizer(groupSinkList, " \t");
            List<Sink> groupSinks = new ArrayList<Sink>();
            while (sinkTokenizer.hasMoreTokens()) {
                String sinkName = sinkTokenizer.nextToken();
                Sink s = sinks.remove(sinkName);
                if (s == null) {
                    String sinkUser = usedSinks.get(sinkName);
                    if (sinkUser != null) {
                        throw new InstantiationException(String.format(
                                "Sink %s of group %s already " +
                                        "in use by group %s", sinkName, groupName, sinkUser));
                    } else {
                        throw new InstantiationException(String.format(
                                "Sink %s of group %s does "
                                        + "not exist or is not properly configured", sinkName,
                                groupName));
                    }
                }
                groupSinks.add(s);
                usedSinks.put(sinkName, groupName);
            }
            SinkGroup group = new SinkGroup(groupSinks);
            Configurables.configure(group, context);
            conf.getSinkRunners().put(comp.getComponentName(),
                    new SinkRunner(group.getProcessor()));
        }
        // add any unasigned sinks to solo collectors
        for (Map.Entry<String, Sink> entry : sinks.entrySet()) {
            if (!usedSinks.containsValue(entry.getKey())) {
                SinkProcessor pr = new DefaultSinkProcessor();
                List<Sink> sinkMap = new ArrayList<Sink>();
                sinkMap.add(entry.getValue());
                pr.setSinks(sinkMap);
                Configurables.configure(pr, new Context());
                conf.getSinkRunners().put(entry.getKey(),
                        new SinkRunner(pr));
            }
        }
    }
}