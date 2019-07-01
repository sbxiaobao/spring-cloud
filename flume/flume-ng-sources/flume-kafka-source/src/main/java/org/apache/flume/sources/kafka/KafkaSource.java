package org.apache.flume.sources.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.flume.Context;
import org.apache.flume.FlumeException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/27
 */
public class KafkaSource extends AbstractSource implements Configurable, PollableSource {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSource.class);

    private Context context;
    private String topic;
    private ConsumerIterator<byte[], byte[]> it;
    private ConsumerConnector consumer;

    private Properties kafkaProps;

    @Override
    public Status process() {
        return null;
    }

    @Override
    public void configure(Context context) {
        this.context = context;
        context.getString(KafkaSourceConstants.TOPIC);

        topic = context.getString(KafkaSourceConstants.TOPIC);

        if (topic == null) {
            throw new ConfigurationException("kafka topic must be specified.");
        }

        kafkaProps = KafkaSourceUtil.getKafkaProperties(context);
    }

    @Override
    public void start() {
        logger.info("Starting {}...", this);

        try {
            consumer = KafkaSourceUtil.getConsumer(kafkaProps);
        } catch (Exception e) {
            throw new FlumeException("Unable to create consumer. Check whether the Zookeeper server is up and that the Flume agent can connect to it.", e);
        }

        try {
            Map<String, Integer> topicCountMap = new HashMap<>();
            topicCountMap.put(topic, 1);

            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
            List<KafkaStream<byte[], byte[]>> topicList = consumerMap.get(topic);
            KafkaStream<byte[], byte[]> stream = topicList.get(0);
            it = stream.iterator();
        } catch (Exception e) {
            throw new FlumeException("Unable to get message iterator from Kafka", e);
        }
        logger.info("Kafka source {} started.", getName());
        super.start();
    }
}