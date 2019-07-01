package org.apache.flume.sources.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.flume.Context;
import org.apache.flume.conf.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/27
 */
public class KafkaSourceUtil {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSourceUtil.class);

    public static Properties getKafkaProperties(Context context) {
        logger.info("context = {}", context.toString());
        Properties props = generateDefaultKafkaProps();
        setKafkaProps(context, props);
        addDocumentedKakfaProps(context, props);
        return props;
    }

    public static ConsumerConnector getConsumer(Properties kafkaProps) {
        ConsumerConfig consumerConfig = new ConsumerConfig(kafkaProps);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);

        return consumer;
    }

    private static Properties generateDefaultKafkaProps() {
        Properties props = new Properties();
        props.put(KafkaSourceConstants.AUTO_COMMIT_ENABLED, KafkaSourceConstants.DEFAULT_AUTO_COMMIT);
        props.put(KafkaSourceConstants.CONSUMER_TIMEOUT, KafkaSourceConstants.DEFAULT_CONSUMER_TIMEOUT);
        props.put(KafkaSourceConstants.GROUP_ID, KafkaSourceConstants.DEFAULT_GROUP_ID);

        return props;
    }

    private static void setKafkaProps(Context context, Properties kafkaProps) {
        Map<String, String> kafkaProperties = context.getSubProperties(KafkaSourceConstants.PROPERTY_PREFIX);

        for (Map.Entry<String, String> prop : kafkaProperties.entrySet()) {
            kafkaProps.put(prop.getKey(), prop.getValue());
            if (logger.isDebugEnabled()) {
                logger.debug("Reading a kakfa producer property: key:{}, value:{}", prop.getKey(), prop.getValue());
            }
        }
    }

    private static void addDocumentedKakfaProps(Context context, Properties KafkaProps) {
        String zookeeperConnect = context.getString(KafkaSourceConstants.ZOOKEEPER_CONNECT_FLUME);
        if (zookeeperConnect == null) {
            throw new ConfigurationException("ZookeeperConnect mus contain at least one Zookeeper server");
        }
        KafkaProps.put(KafkaSourceConstants.ZOOKEEPER_CONNECT, zookeeperConnect);

        String groupID = context.getString(KafkaSourceConstants.GROUP_ID_FLUME);

        if (groupID != null) {
            KafkaProps.put(KafkaSourceConstants.GROUP_ID, groupID);
        }
    }
}