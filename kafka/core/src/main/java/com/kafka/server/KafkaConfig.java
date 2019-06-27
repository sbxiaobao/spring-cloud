package com.kafka.server;

import com.kafka.common.config.AbstractConfig;
import com.kafka.common.config.ConfigDef;
import com.kafka.common.config.SaslConfigs;
import com.kafka.common.config.SslConfigs;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/5/5
 */
public class KafkaConfig extends AbstractConfig {

    private static final String LogConfigPrefix = "log.";

    public KafkaConfig(Map props, boolean doLog) {
        super(null, props, doLog);
    }

    public static int ZkSessionTimeoutMs = 6000;
    public static int ZkSyncTimeMs = 2000;
    public static boolean ZkEnableSecureAcls = false;

    /**
     * ******** General Configuration
     ***********/
    public static boolean BrokerIdGenerationEnable = true;
    public static int MaxReservedBrokerId = 1000;
    public static int BrokerId = -1;
    //    public static int MessageMaxBytes = 1000000 + MessageSet.LogOverhead;
    public static int NumNetworkThreads = 3;
    public static int NumIoThreads = 8;
    public static int BackgroundThreads = 10;
    public static int QueuedMaxRequests = 500;

    /************* Authorizer Configuration ***********/
    public static String AuthorizerClassName = "";

    /**
     * ******** Socket Server Configuration
     ***********/
    public static int Port = 9092;
    public static String HostName = new String("");

    public static int SocketSendBufferBytes = 100 * 1024;
    public static int SocketReceiveBufferBytes = 100 * 1024;
    public static int SocketRequestMaxBytes = 100 * 1024 * 1024;
    public static int MaxConnectionsPerIp = Integer.MAX_VALUE;
    public static String MaxConnectionsPerIpOverrides = "";
    public static Long ConnectionsMaxIdleMs = 10 * 60 * 1000L;
    public static int RequestTimeoutMs = 30000;

    /**
     * ******** Log Configuration
     ***********/
    public static int NumPartitions = 1;
    public static String LogDir = "/tmp/kafka-logs";
    public static int LogSegmentBytes = 1 * 1024 * 1024 * 1024;
    public static int LogRollHours = 24 * 7;
    public static int LogRollJitterHours = 0;
    public static int LogRetentionHours = 24 * 7;

    public static Long LogRetentionBytes = -1L;
    public static Long LogCleanupIntervalMs = 5 * 60 * 1000L;
    public static String Delete = "delete";
    public static String Compact = "compact";
    public static String LogCleanupPolicy = Delete;
    public static int LogCleanerThreads = 1;
    public static Double LogCleanerIoMaxBytesPerSecond = Double.MAX_VALUE;
    public static Long LogCleanerDedupeBufferSize = 128 * 1024 * 1024L;
    public static int LogCleanerIoBufferSize = 512 * 1024;
    public static double LogCleanerDedupeBufferLoadFactor = 0.9d;
    public static int LogCleanerBackoffMs = 15 * 1000;
    public static double LogCleanerMinCleanRatio = 0.5d;
    public static boolean LogCleanerEnable = true;
    public static long LogCleanerDeleteRetentionMs = 24 * 60 * 60 * 1000L;
    public static int LogIndexSizeMaxBytes = 10 * 1024 * 1024;
    public static int LogIndexIntervalBytes = 4096;
    public static Long LogFlushIntervalMessages = Long.MAX_VALUE;
    public static int LogDeleteDelayMs = 60000;
    public static Long LogFlushSchedulerIntervalMs = Long.MAX_VALUE;
    public static int LogFlushOffsetCheckpointIntervalMs = 60000;
    public static boolean LogPreAllocateEnable = false;
    // lazy val as `InterBrokerProtocolVersion` is defined later
//    lazy val
//    LogMessageFormatVersion =InterBrokerProtocolVersion
    public static String LogMessageTimestampType = "CreateTime";
    public static Long LogMessageTimestampDifferenceMaxMs = Long.MAX_VALUE;
    public static int NumRecoveryThreadsPerDataDir = 1;
    public static boolean AutoCreateTopicsEnable = true;
    public static int MinInSyncReplicas = 1;

    /**
     * ******** Replication configuration
     ***********/
    public static int ControllerSocketTimeoutMs = RequestTimeoutMs;
    public static int ControllerMessageQueueSize = Integer.MAX_VALUE;
    public static int DefaultReplicationFactor = 1;
    public static Long ReplicaLagTimeMaxMs = 10000L;
//    val ReplicaSocketTimeoutMs = ConsumerConfig.SocketTimeout;
//    val ReplicaSocketReceiveBufferBytes = ConsumerConfig.SocketBufferSize
//    val ReplicaFetchMaxBytes = ConsumerConfig.FetchSize
public static int ReplicaFetchWaitMaxMs = 500;
    public static int ReplicaFetchMinBytes = 1;
    public static int NumReplicaFetchers = 1;
    public static int ReplicaFetchBackoffMs = 1000;
    public static Long ReplicaHighWatermarkCheckpointIntervalMs = 5000L;
    public static int FetchPurgatoryPurgeIntervalRequests = 1000;
    public static int ProducerPurgatoryPurgeIntervalRequests = 1000;
    public static boolean AutoLeaderRebalanceEnable = true;
    public static int LeaderImbalancePerBrokerPercentage = 10;
    public static int LeaderImbalanceCheckIntervalSeconds = 300;
    public static boolean UncleanLeaderElectionEnable = true;
//    val InterBrokerSecurityProtocol = SecurityProtocol.PLAINTEXT.toString
//    val InterBrokerProtocolVersion = ApiVersion.latestVersion.toString

    /**
     * ******** Controlled shutdown configuration
     ***********/
    public static int ControlledShutdownMaxRetries = 3;
    public static int ControlledShutdownRetryBackoffMs = 5000;
    public static boolean ControlledShutdownEnable = true;

    /**
     * ******** Group coordinator configuration
     ***********/
    public static int GroupMinSessionTimeoutMs = 6000;
    public static int GroupMaxSessionTimeoutMs = 300000;

    /**
     * ******** Offset management configuration
     ***********/
//    val OffsetMetadataMaxSize = OffsetConfig.DefaultMaxMetadataSize
//    val OffsetsLoadBufferSize = OffsetConfig.DefaultLoadBufferSize
//    val OffsetsTopicReplicationFactor = OffsetConfig.DefaultOffsetsTopicReplicationFactor
//    val OffsetsTopicPartitions:Int =OffsetConfig.DefaultOffsetsTopicNumPartitions
//    val OffsetsTopicSegmentBytes:Int =OffsetConfig.DefaultOffsetsTopicSegmentBytes
//    val OffsetsTopicCompressionCodec:Int =OffsetConfig.DefaultOffsetsTopicCompressionCodec.codec
//    val OffsetsRetentionMinutes:Int =24*60
//    val OffsetsRetentionCheckIntervalMs:Long =OffsetConfig.DefaultOffsetsRetentionCheckIntervalMs
//    val OffsetCommitTimeoutMs = OffsetConfig.DefaultOffsetCommitTimeoutMs
//    val OffsetCommitRequiredAcks = OffsetConfig.DefaultOffsetCommitRequiredAcks

    /**
     * ******** Quota Configuration
     ***********/
//    val ProducerQuotaBytesPerSecondDefault = ClientQuotaManagerConfig.QuotaBytesPerSecondDefault
//    val ConsumerQuotaBytesPerSecondDefault = ClientQuotaManagerConfig.QuotaBytesPerSecondDefault
//    val NumQuotaSamples:Int =ClientQuotaManagerConfig.DefaultNumQuotaSamples
//    val QuotaWindowSizeSeconds:Int =ClientQuotaManagerConfig.DefaultQuotaWindowSizeSeconds
//
//    val DeleteTopicEnable = false
//
//    val CompressionType = "producer"

    /**
     * ******** Kafka Metrics Configuration
     ***********/
//    val MetricNumSamples = 2
//    val MetricSampleWindowMs = 30000
//    val MetricReporterClasses = ""

    /**
     * ******** SSL configuration
     ***********/
//    val PrincipalBuilderClass = SslConfigs.DEFAULT_PRINCIPAL_BUILDER_CLASS
//    val SslProtocol = SslConfigs.DEFAULT_SSL_PROTOCOL
//    val SslEnabledProtocols = SslConfigs.DEFAULT_SSL_ENABLED_PROTOCOLS
//    val SslKeystoreType = SslConfigs.DEFAULT_SSL_KEYSTORE_TYPE
//    val SslTruststoreType = SslConfigs.DEFAULT_SSL_TRUSTSTORE_TYPE
//    val SslKeyManagerAlgorithm = SslConfigs.DEFAULT_SSL_KEYMANGER_ALGORITHM
//    val SslTrustManagerAlgorithm = SslConfigs.DEFAULT_SSL_TRUSTMANAGER_ALGORITHM
//    val SslClientAuthRequired = "required"
//    val SslClientAuthRequested = "requested"
//    val SslClientAuthNone = "none"
//    val SslClientAuth = SslClientAuthNone

    /**
     * ******** Sasl configuration
     ***********/
//    val SaslMechanismInterBrokerProtocol = SaslConfigs.DEFAULT_SASL_MECHANISM
//    val SaslEnabledMechanisms = SaslConfigs.DEFAULT_SASL_ENABLED_MECHANISMS
//    val SaslKerberosKinitCmd = SaslConfigs.DEFAULT_KERBEROS_KINIT_CMD
//    val SaslKerberosTicketRenewWindowFactor = SaslConfigs.DEFAULT_KERBEROS_TICKET_RENEW_WINDOW_FACTOR
//    val SaslKerberosTicketRenewJitter = SaslConfigs.DEFAULT_KERBEROS_TICKET_RENEW_JITTER
//    val SaslKerberosMinTimeBeforeRelogin = SaslConfigs.DEFAULT_KERBEROS_MIN_TIME_BEFORE_RELOGIN
//    val SaslKerberosPrincipalToLocalRules = SaslConfigs.DEFAULT_SASL_KERBEROS_PRINCIPAL_TO_LOCAL_RULES


    String ZkConnectProp = "zookeeper.connect";
    String ZkSessionTimeoutMsProp = "zookeeper.session.timeout.ms";
    String ZkConnectionTimeoutMsProp = "zookeeper.connection.timeout.ms";
    String ZkSyncTimeMsProp = "zookeeper.sync.time.ms";
    String ZkEnableSecureAclsProp = "zookeeper.set.acl";
    /**
     * ******** General Configuration
     ***********/
    String BrokerIdGenerationEnableProp = "broker.id.generation.enable";
    String MaxReservedBrokerIdProp = "reserved.broker.max.id";
    String BrokerIdProp = "broker.id";
    String MessageMaxBytesProp = "message.max.bytes";
    String NumNetworkThreadsProp = "num.network.threads";
    String NumIoThreadsProp = "num.io.threads";
    String BackgroundThreadsProp = "background.threads";
    String QueuedMaxRequestsProp = "queued.max.requests";
//    String RequestTimeoutMsProp = CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG;
    /************* Authorizer Configuration ***********/
    String AuthorizerClassNameProp = "authorizer.class.name";
    /**
     * ******** Socket Server Configuration
     ***********/
    String PortProp = "port";
    String HostNameProp = "host.name";
    String ListenersProp = "listeners";
    String AdvertisedHostNameProp = "advertised.host.name";
    String AdvertisedPortProp = "advertised.port";
    String AdvertisedListenersProp = "advertised.listeners";
    String SocketSendBufferBytesProp = "socket.send.buffer.bytes";
    String SocketReceiveBufferBytesProp = "socket.receive.buffer.bytes";
    String SocketRequestMaxBytesProp = "socket.request.max.bytes";
    String MaxConnectionsPerIpProp = "max.connections.per.ip";
    String MaxConnectionsPerIpOverridesProp = "max.connections.per.ip.overrides";
    String ConnectionsMaxIdleMsProp = "connections.max.idle.ms";
    /***************** rack configuration *************/
    String RackProp = "broker.rack";
    /**
     * ******** Log Configuration
     ***********/
    String NumPartitionsProp = "num.partitions";
    String LogDirsProp = "log.dirs";
    String LogDirProp = "log.dir";
    String LogSegmentBytesProp = "log.segment.bytes";

    String LogRollTimeMillisProp = "log.roll.ms";
    String LogRollTimeHoursProp = "log.roll.hours";

    String LogRollTimeJitterMillisProp = "log.roll.jitter.ms";
    String LogRollTimeJitterHoursProp = "log.roll.jitter.hours";

    String LogRetentionTimeMillisProp = "log.retention.ms";
    String LogRetentionTimeMinutesProp = "log.retention.minutes";
    String LogRetentionTimeHoursProp = "log.retention.hours";

    String LogRetentionBytesProp = "log.retention.bytes";
    String LogCleanupInterStringMsProp = "log.retention.check.interString.ms";
    String LogCleanupPolicyProp = "log.cleanup.policy";
    String LogCleanerThreadsProp = "log.cleaner.threads";
    String LogCleanerIoMaxBytesPerSecondProp = "log.cleaner.io.max.bytes.per.second";
    String LogCleanerDedupeBufferSizeProp = "log.cleaner.dedupe.buffer.size";
    String LogCleanerIoBufferSizeProp = "log.cleaner.io.buffer.size";
    String LogCleanerDedupeBufferLoadFactorProp = "log.cleaner.io.buffer.load.factor";
    String LogCleanerBackoffMsProp = "log.cleaner.backoff.ms";
    String LogCleanerMinCleanRatioProp = "log.cleaner.min.cleanable.ratio";
    String LogCleanerEnableProp = "log.cleaner.enable";
    String LogCleanerDeleteRetentionMsProp = "log.cleaner.delete.retention.ms";
    String LogIndexSizeMaxBytesProp = "log.index.size.max.bytes";
    String LogIndexInterStringBytesProp = "log.index.interString.bytes";
    String LogFlushInterStringMessagesProp = "log.flush.interString.messages";
    String LogDeleteDelayMsProp = "log.segment.delete.delay.ms";
    String LogFlushSchedulerInterStringMsProp = "log.flush.scheduler.interString.ms";
    String LogFlushInterStringMsProp = "log.flush.interString.ms";
    String LogFlushOffsetCheckpointInterStringMsProp = "log.flush.offset.checkpoint.interString.ms";
    String LogPreAllocateProp = "log.preallocate";
    //    String LogMessageFormatVersionProp = LogConfigPrefix + LogConfig.MessageFormatVersionProp
//    String LogMessageTimestampTypeProp = LogConfigPrefix + LogConfig.MessageTimestampTypeProp
//    String LogMessageTimestampDifferenceMaxMsProp = LogConfigPrefix + LogConfig.MessageTimestampDifferenceMaxMsProp
    String NumRecoveryThreadsPerDataDirProp = "num.recovery.threads.per.data.dir";
    String AutoCreateTopicsEnableProp = "auto.create.topics.enable";
    String MinInSyncReplicasProp = "min.insync.replicas";
    /**
     * ******** Replication configuration
     ***********/
    String ControllerSocketTimeoutMsProp = "controller.socket.timeout.ms";
    String DefaultReplicationFactorProp = "default.replication.factor";
    String ReplicaLagTimeMaxMsProp = "replica.lag.time.max.ms";
    String ReplicaSocketTimeoutMsProp = "replica.socket.timeout.ms";
    String ReplicaSocketReceiveBufferBytesProp = "replica.socket.receive.buffer.bytes";
    String ReplicaFetchMaxBytesProp = "replica.fetch.max.bytes";
    String ReplicaFetchWaitMaxMsProp = "replica.fetch.wait.max.ms";
    String ReplicaFetchMinBytesProp = "replica.fetch.min.bytes";
    String ReplicaFetchBackoffMsProp = "replica.fetch.backoff.ms";
    String NumReplicaFetchersProp = "num.replica.fetchers";
    String ReplicaHighWatermarkCheckpointInterStringMsProp = "replica.high.watermark.checkpoint.interString.ms";
    String FetchPurgatoryPurgeInterStringRequestsProp = "fetch.purgatory.purge.interString.requests";
    String ProducerPurgatoryPurgeInterStringRequestsProp = "producer.purgatory.purge.interString.requests";
    String AutoLeaderRebalanceEnableProp = "auto.leader.rebalance.enable";
    String LeaderImbalancePerBrokerPercentageProp = "leader.imbalance.per.broker.percentage";
    String LeaderImbalanceCheckInterStringSecondsProp = "leader.imbalance.check.interString.seconds";
    String UncleanLeaderElectionEnableProp = "unclean.leader.election.enable";
    String InterBrokerSecurityProtocolProp = "security.inter.broker.protocol";
    String InterBrokerProtocolVersionProp = "inter.broker.protocol.version";
    /**
     * ******** Controlled shutdown configuration
     ***********/
    String ControlledShutdownMaxRetriesProp = "controlled.shutdown.max.retries";
    String ControlledShutdownRetryBackoffMsProp = "controlled.shutdown.retry.backoff.ms";
    String ControlledShutdownEnableProp = "controlled.shutdown.enable";
    /**
     * ******** Group coordinator configuration
     ***********/
    String GroupMinSessionTimeoutMsProp = "group.min.session.timeout.ms";
    String GroupMaxSessionTimeoutMsProp = "group.max.session.timeout.ms";
    /**
     * ******** Offset management configuration
     ***********/
    String OffsetMetadataMaxSizeProp = "offset.metadata.max.bytes";
    String OffsetsLoadBufferSizeProp = "offsets.load.buffer.size";
    String OffsetsTopicReplicationFactorProp = "offsets.topic.replication.factor";
    String OffsetsTopicPartitionsProp = "offsets.topic.num.partitions";
    String OffsetsTopicSegmentBytesProp = "offsets.topic.segment.bytes";
    String OffsetsTopicCompressionCodecProp = "offsets.topic.compression.codec";
    String OffsetsRetentionMinutesProp = "offsets.retention.minutes";
    String OffsetsRetentionCheckInterStringMsProp = "offsets.retention.check.interString.ms";
    String OffsetCommitTimeoutMsProp = "offsets.commit.timeout.ms";
    String OffsetCommitRequiredAcksProp = "offsets.commit.required.acks";
    /**
     * ******** Quota Configuration
     ***********/
    String ProducerQuotaBytesPerSecondDefaultProp = "quota.producer.default";
    String ConsumerQuotaBytesPerSecondDefaultProp = "quota.consumer.default";
    String NumQuotaSamplesProp = "quota.window.num";
    String QuotaWindowSizeSecondsProp = "quota.window.size.seconds";

    String DeleteTopicEnableProp = "delete.topic.enable";
    String CompressionTypeProp = "compression.type";

    /**
     * ******** Kafka Metrics Configuration
     ***********/
//    String MetricSampleWindowMsProp = CommonClientConfigs.METRICS_SAMPLE_WINDOW_MS_CONFIG
//    String MetricNumSamplesProp:String =CommonClientConfigs.METRICS_NUM_SAMPLES_CONFIG
//    String MetricReporterClassesProp:String =CommonClientConfigs.METRIC_REPORTER_CLASSES_CONFIG

    /**
     * ******** SSL Configuration
     ****************/
    String PrincipalBuilderClassProp = SslConfigs.PRINCIPAL_BUILDER_CLASS_CONFIG;
    String SslProtocolProp = SslConfigs.SSL_PROTOCOL_CONFIG;
    String SslProviderProp = SslConfigs.SSL_PROVIDER_CONFIG;
    String SslCipherSuitesProp = SslConfigs.SSL_CIPHER_SUITES_CONFIG;
    String SslEnabledProtocolsProp = SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG;
    String SslKeystoreTypeProp = SslConfigs.SSL_KEYSTORE_TYPE_CONFIG;
    String SslKeystoreLocationProp = SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
    String SslKeystorePasswordProp = SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
    String SslKeyPasswordProp = SslConfigs.SSL_KEY_PASSWORD_CONFIG;
    String SslTruststoreTypeProp = SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG;
    String SslTruststoreLocationProp = SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
    String SslTruststorePasswordProp = SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
    String SslKeyManagerAlgorithmProp = SslConfigs.SSL_KEYMANAGER_ALGORITHM_CONFIG;
    String SslTrustManagerAlgorithmProp = SslConfigs.SSL_TRUSTMANAGER_ALGORITHM_CONFIG;
    String SslEndpointIdentificationAlgorithmProp = SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG;
    String SslSecureRandomImplementationProp = SslConfigs.SSL_SECURE_RANDOM_IMPLEMENTATION_CONFIG;
    String SslClientAuthProp = SslConfigs.SSL_CLIENT_AUTH_CONFIG;

    /**
     * ******** SASL Configuration
     ****************/
    String SaslMechanismInterBrokerProtocolProp = "sasl.mechanism.inter.broker.protocol";
    String SaslEnabledMechanismsProp = SaslConfigs.SASL_ENABLED_MECHANISMS;
    String SaslKerberosServiceNameProp = SaslConfigs.SASL_KERBEROS_SERVICE_NAME;
    String SaslKerberosKinitCmdProp = SaslConfigs.SASL_KERBEROS_KINIT_CMD;
    String SaslKerberosTicketRenewWindowFactorProp = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR;
    String SaslKerberosTicketRenewJitterProp = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_JITTER;
    String SaslKerberosMinTimeBeforeReloginProp = SaslConfigs.SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN;
    String SaslKerberosPrincipalToLocalRulesProp = SaslConfigs.SASL_KERBEROS_PRINCIPAL_TO_LOCAL_RULES;

    /* DOC;umentation */
    /**
     * ******** Zookeeper Configuration
     ***********/
    String ZkConnectDOC = "Zookeeper host string";
    String ZkSessionTimeoutMsDOC = "Zookeeper session timeout";
    String ZkConnectionTimeoutMsDOC = "The max time that the client waits to establish a connection to zookeeper. If not set, the Stringue in " + ZkSessionTimeoutMsProp + " is used";
    String ZkSyncTimeMsDOC = "How far a ZK follower can be behind a ZK leader";
    String ZkEnableSecureAclsDOC = "Set client to use secure ACLs";
    /**
     * ******** General Configuration
     ***********/
    String BrokerIdGenerationEnableDOC = "Enable automatic broker id generation on the server? When enabled the Stringue configured for $MaxReservedBrokerIdProp should be reviewed.";
    String MaxReservedBrokerIdDOC = "Max number that can be used for a broker.id";
    String BrokerIdDOC = "The broker id for this server. If unset, a unique broker id will be generated." +
            "To avoid conflicts between zookeeper generated broker id's and user configured broker id's, generated broker ids" +
            "start from " + MaxReservedBrokerIdProp + " + 1.";
    String MessageMaxBytesDOC = "The maximum size of message that the server can receive";
    String NumNetworkThreadsDOC = "the number of network threads that the server uses for handling network requests";
    String NumIoThreadsDOC = "The number of io threads that the server uses for carrying out network requests";
    String BackgroundThreadsDOC = "The number of threads to use for various background processing tasks";
    String QueuedMaxRequestsDOC = "The number of queued requests allowed before blocking the network threads";
//    String RequestTimeoutMsDOC = CommonClientConfigs.REQUEST_TIMEOUT_MS_DOC;
    /************* Authorizer Configuration ***********/
    String AuthorizerClassNameDOC = "The authorizer class that should be used for authorization";
    /**
     * ******** Socket Server Configuration
     ***********/
    String PortDOC = "DEPRECATED: only used when `listeners` is not set. " +
            "Use `listeners` instead. \n" +
            "the port to listen and accept connections on";
    String HostNameDOC = "DEPRECATED: only used when `listeners` is not set. " +
            "Use `listeners` instead. \n" +
            "hostname of broker. If this is set, it will only bind to this address. If this is not set, it will bind to all interfaces";
    String ListenersDOC = "Listener List - Comma-separated list of URIs we will listen on and their protocols.\n" +
            " Specify hostname as 0.0.0.0 to bind to all interfaces.\n" +
            " Leave hostname empty to bind to default interface.\n" +
            " Examples of legal listener lists:\n" +
            " PLAINTEXT://myhost:9092,TRACE://:9091\n" +
            " PLAINTEXT://0.0.0.0:9092, TRACE://localhost:9093\n";
    String AdvertisedHostNameDOC = "DEPRECATED: only used when `advertised.listeners` or `listeners` are not set. " +
            "Use `advertised.listeners` instead. \n" +
            "Hostname to publish to ZooKeeper for clients to use. In IaaS environments, this may " +
            "need to be different from the interface to which the broker binds. If this is not set, " +
            "it will use the Stringue for `host.name` if configured. Otherwise " +
            "it will use the Stringue returned from java.net.InetAddress.getCanonicalHostName().";
    String AdvertisedPortDOC = "DEPRECATED: only used when `advertised.listeners` or `listeners` are not set. " +
            "Use `advertised.listeners` instead. \n" +
            "The port to publish to ZooKeeper for clients to use. In IaaS environments, this may " +
            "need to be different from the port to which the broker binds. If this is not set, " +
            "it will publish the same port that the broker binds to.";
    String AdvertisedListenersDOC = "Listeners to publish to ZooKeeper for clients to use, if different than the listeners above." +
            " In IaaS environments, this may need to be different from the interface to which the broker binds." +
            " If this is not set, the Stringue for `listeners` will be used.";
    String SocketSendBufferBytesDOC = "The SO_SNDBUF buffer of the socket sever sockets";
    String SocketReceiveBufferBytesDOC = "The SO_RCVBUF buffer of the socket sever sockets";
    String SocketRequestMaxBytesDOC = "The maximum number of bytes in a socket request";
    String MaxConnectionsPerIpDOC = "The maximum number of connections we allow from each ip address";
    String MaxConnectionsPerIpOverridesDOC = "Per-ip or hostname overrides to the default maximum number of connections";
    String ConnectionsMaxIdleMsDOC = "Idle connections timeout: the server socket processor threads close the connections that idle more than this";
    /************* Rack Configuration **************/
    String RackDOC = "Rack of the broker. This will be used in rack aware replication assignment for fault tolerance. Examples: `RACK1`, `us-east-1d`";
    /**
     * ******** Log Configuration
     ***********/
    String NumPartitionsDOC = "The default number of log partitions per topic";
    String LogDirDOC = "The directory in which the log data is kept (supplemental for " + LogDirsProp + " property)";
    String LogDirsDOC = "The directories in which the log data is kept. If not set, the Stringue in " + LogDirProp + " is used";
    String LogSegmentBytesDOC = "The maximum size of a single log file";
    String LogRollTimeMillisDOC = "The maximum time before a new log segment is rolled out (in milliseconds). If not set, the Stringue in " + LogRollTimeHoursProp + " is used";
    String LogRollTimeHoursDOC = "The maximum time before a new log segment is rolled out (in hours), secondary to " + LogRollTimeMillisProp + " property";

    String LogRollTimeJitterMillisDOC = "The maximum jitter to subtract from logRollTimeMillis (in milliseconds). If not set, the Stringue in " + LogRollTimeJitterHoursProp + " is used";
    String LogRollTimeJitterHoursDOC = "The maximum jitter to subtract from logRollTimeMillis (in hours), secondary to " + LogRollTimeJitterMillisProp + " property";

    String LogRetentionTimeMillisDOC = "The number of milliseconds to keep a log file before deleting it (in milliseconds), If not set, the Stringue in " + LogRetentionTimeMinutesProp + " is used";
    String LogRetentionTimeMinsDOC = "The number of minutes to keep a log file before deleting it (in minutes), secondary to " + LogRetentionTimeMillisProp + " property. If not set, the Stringue in " + LogRetentionTimeHoursProp + " is used";
    String LogRetentionTimeHoursDOC = "The number of hours to keep a log file before deleting it (in hours), tertiary to " + LogRetentionTimeMillisProp + " property";

    String LogRetentionBytesDOC = "The maximum size of the log before deleting it";
    String LogCleanupInterStringMsDOC = "The frequency in milliseconds that the log cleaner checks whether any log is eligible for deletion";
    String LogCleanupPolicyDOC = "The default cleanup policy for segments beyond the retention window, must be either \"delete\" or \"compact\"";
    String LogCleanerThreadsDOC = "The number of background threads to use for log cleaning";
    String LogCleanerIoMaxBytesPerSecondDOC = "The log cleaner will be throttled so that the sum of its read and write i/o will be less than this Stringue on average";
    String LogCleanerDedupeBufferSizeDOC = "The total memory used for log deduplication across all cleaner threads";
    String LogCleanerIoBufferSizeDOC = "The total memory used for log cleaner I/O buffers across all cleaner threads";
    String LogCleanerDedupeBufferLoadFactorDOC = "Log cleaner dedupe buffer load factor. The percentage full the dedupe buffer can become. A higher Stringue " +
            "will allow more log to be cleaned at once but will lead to more hash collisions";
    String LogCleanerBackoffMsDOC = "The amount of time to sleep when there are no logs to clean";
    String LogCleanerMinCleanRatioDOC = "The minimum ratio of dirty log to total log for a log to eligible for cleaning";
    String LogCleanerEnableDOC = "Enable the log cleaner process to run on the server? Should be enabled if using any topics with a cleanup.policy=compact including the internal offsets topic. If disabled those topics will not be compacted and continually grow in size.";
    String LogCleanerDeleteRetentionMsDOC = "How long are delete records retained?";
    String LogIndexSizeMaxBytesDOC = "The maximum size in bytes of the offset index";
    String LogIndexInterStringBytesDOC = "The interString with which we add an entry to the offset index";
    String LogFlushInterStringMessagesDOC = "The number of messages accumulated on a log partition before messages are flushed to disk ";
    String LogDeleteDelayMsDOC = "The amount of time to wait before deleting a file from the filesystem";
    String LogFlushSchedulerInterStringMsDOC = "The frequency in ms that the log flusher checks whether any log needs to be flushed to disk";
    String LogFlushInterStringMsDOC = "The maximum time in ms that a message in any topic is kept in memory before flushed to disk. If not set, the Stringue in " + LogFlushSchedulerInterStringMsProp + " is used";
    String LogFlushOffsetCheckpointInterStringMsDOC = "The frequency with which we update the persistent record of the last flush which acts as the log recovery point";
    String LogPreAllocateEnableDOC = "Should pre allocate file when create new segment? If you are using Kafka on Windows, you probably need to set it to true.";
    String LogMessageFormatVersionDOC = "Specify the message format version the broker will use to append messages to the logs. The Stringue should be a Stringid ApiVersion. " +
            "Some examples are: 0.8.2, 0.9.0.0, 0.10.0, check ApiVersion for more details. By setting a particular message format version, the " +
            "user is certifying that all the existing messages on disk are smaller or equal than the specified version. Setting this Stringue incorrectly " +
            "will cause consumers with older versions to break as they will receive messages with a format that they don't understand.";
    String LogMessageTimestampTypeDOC = "Define whether the timestamp in the message is message create time or log append time. The Stringue should be either " +
            "`CreateTime` or `LogAppendTime`";
    String LogMessageTimestampDifferenceMaxMsDOC = "The maximum difference allowed between the timestamp when a broker receives " +
            "a message and the timestamp specified in the message. If message.timestamp.type=CreateTime, a message will be rejected " +
            "if the difference in timestamp exceeds this threshold. This configuration is ignored if message.timestamp.type=LogAppendTime.";
    String NumRecoveryThreadsPerDataDirDOC = "The number of threads per data directory to be used for log recovery at startup and flushing at shutdown";
    String AutoCreateTopicsEnableDOC = "Enable auto creation of topic on the server";
    String MinInSyncReplicasDOC = "define the minimum number of replicas in ISR needed to satisfy a produce request with acks=all (or -1)";
    /**
     * ******** Replication configuration
     ***********/
    String ControllerSocketTimeoutMsDOC = "The socket timeout for controller-to-broker channels";
    String ControllerMessageQueueSizeDOC = "The buffer size for controller-to-broker-channels";
    String DefaultReplicationFactorDOC = "default replication factors for automatically created topics";
    String ReplicaLagTimeMaxMsDOC = "If a follower hasn't sent any fetch requests or hasn't consumed up to the leaders log end offset for at least this time," +
            " the leader will remove the follower from isr";
    String ReplicaSocketTimeoutMsDOC = "The socket timeout for network requests. Its Stringue should be at least replica.fetch.wait.max.ms";
    String ReplicaSocketReceiveBufferBytesDOC = "The socket receive buffer for network requests";
    String ReplicaFetchMaxBytesDOC = "The number of bytes of messages to attempt to fetch";
    String ReplicaFetchWaitMaxMsDOC = "max wait time for each fetcher request issued by follower replicas. This Stringue should always be less than the " +
            "replica.lag.time.max.ms at all times to prevent frequent shrinking of ISR for low throughput topics";
    String ReplicaFetchMinBytesDOC = "Minimum bytes expected for each fetch response. If not enough bytes, wait up to replicaMaxWaitTimeMs";
    String NumReplicaFetchersDOC = "Number of fetcher threads used to replicate messages from a source broker. " +
            "Increasing this Stringue can increase the degree of I/O parallelism in the follower broker.";
    String ReplicaFetchBackoffMsDOC = "The amount of time to sleep when fetch partition error occurs.";
    String ReplicaHighWatermarkCheckpointInterStringMsDOC = "The frequency with which the high watermark is saved out to disk";
    String FetchPurgatoryPurgeInterStringRequestsDOC = "The purge interString (in number of requests) of the fetch request purgatory";
    String ProducerPurgatoryPurgeInterStringRequestsDOC = "The purge interString (in number of requests) of the producer request purgatory";
    String AutoLeaderRebalanceEnableDOC = "Enables auto leader balancing. A background thread checks and triggers leader balance if required at regular interStrings";
    String LeaderImbalancePerBrokerPercentageDOC = "The ratio of leader imbalance allowed per broker. The controller would trigger a leader balance if it goes above this Stringue per broker. The Stringue is specified in percentage.";
    String LeaderImbalanceCheckInterStringSecondsDOC = "The frequency with which the partition rebalance check is triggered by the controller";
    String UncleanLeaderElectionEnableDOC = "Indicates whether to enable replicas not in the ISR set to be elected as leader as a last resort, even though doing so may result in data loss";
    String InterBrokerSecurityProtocolDOC = "Security protocol used to communicate between brokers. Stringid Stringues are: " + "${SecurityProtocol.nonTestingStringues.asScala.toSeq.map(_.name).mkString(\"\")}.";
    String InterBrokerProtocolVersionDOC = "Specify which version of the inter-broker protocol will be used.\n" +
            " This is typically bumped after all brokers were upgraded to a new version.\n" +
            " Example of some Stringid Stringues are: 0.8.0, 0.8.1, 0.8.1.1, 0.8.2, 0.8.2.0, 0.8.2.1, 0.9.0.0, 0.9.0.1 Check ApiVersion for the full list.";
    /**
     * ******** Controlled shutdown configuration
     ***********/
    String ControlledShutdownMaxRetriesDOC = "Controlled shutdown can fail for multiple reasons. This determines the number of retries when such failure happens";
    String ControlledShutdownRetryBackoffMsDOC = "Before each retry, the system needs time to recover from the state that caused the previous failure (Controller fail over, replica lag etc). This config determines the amount of time to wait before retrying.";
    String ControlledShutdownEnableDOC = "Enable controlled shutdown of the server";
    /**
     * ******** Consumer coordinator configuration
     ***********/
    String GroupMinSessionTimeoutMsDOC = "The minimum allowed session timeout for registered consumers. Shorter timeouts leader to quicker failure detection at the cost of more frequent consumer heartbeating, which can overwhelm broker resources.";
    String GroupMaxSessionTimeoutMsDOC = "The maximum allowed session timeout for registered consumers. Longer timeouts give consumers more time to process messages in between heartbeats at the cost of a longer time to detect failures.";
    /**
     * ******** Offset management configuration
     ***********/
    String OffsetMetadataMaxSizeDOC = "The maximum size for a metadata entry associated with an offset commit";
    String OffsetsLoadBufferSizeDOC = "Batch size for reading from the offsets segments when loading offsets into the cache.";
    String OffsetsTopicReplicationFactorDOC = "The replication factor for the offsets topic (set higher to ensure availability). " +
            "To ensure that the effective replication factor of the offsets topic is the configured Stringue, " +
            "the number of alive brokers has to be at least the replication factor at the time of the " +
            "first request for the offsets topic. If not, either the offsets topic creation will fail or " +
            "it will get a replication factor of min(alive brokers, configured replication factor)";
    String OffsetsTopicPartitionsDOC = "The number of partitions for the offset commit topic (should not change after deployment)";
    String OffsetsTopicSegmentBytesDOC = "The offsets topic segment bytes should be kept relatively small in order to facilitate faster log compaction and cache loads";
    String OffsetsTopicCompressionCodecDOC = "Compression codec for the offsets topic - compression may be used to achieve \"atomic\" commits";
    String OffsetsRetentionMinutesDOC = "Log retention window in minutes for offsets topic";
    String OffsetsRetentionCheckInterStringMsDOC = "Frequency at which to check for stale offsets";
    String OffsetCommitTimeoutMsDOC = "Offset commit will be delayed until all replicas for the offsets topic receive the commit " +
            "or this timeout is reached. This is similar to the producer request timeout.";
    String OffsetCommitRequiredAcksDOC = "The required acks before the commit can be accepted. In general, the default (-1) should not be overridden";
    /**
     * ******** Quota Configuration
     ***********/
    String ProducerQuotaBytesPerSecondDefaultDOC = "Any producer distinguished by clientId will get throttled if it produces more bytes than this Stringue per-second";
    String ConsumerQuotaBytesPerSecondDefaultDOC = "Any consumer distinguished by clientId/consumer group will get throttled if it fetches more bytes than this Stringue per-second";
    String NumQuotaSamplesDOC = "The number of samples to retain in memory";
    String QuotaWindowSizeSecondsDOC = "The time span of each sample";

    String DeleteTopicEnableDOC = "Enables delete topic. Delete topic through the admin tool will have no effect if this config is turned off";
    String CompressionTypeDOC = "Specify the final compression type for a given topic. This configuration accepts the standard compression codecs " +
            "('gzip', 'snappy', 'lz4'). It additionally accepts 'uncompressed' which is equiStringent to no compression; and " +
            "'producer' which means retain the original compression codec set by the producer.";

    /**
     * ******** Kafka Metrics Configuration
     ***********/
//    String MetricSampleWindowMsDOC; = CommonClientConfigs.METRICS_SAMPLE_WINDOW_MS_DOC;
//    String MetricNumSamplesDOC; = CommonClientConfigs.METRICS_NUM_SAMPLES_DOC;
//    String MetricReporterClassesDOC; = CommonClientConfigs.METRIC_REPORTER_CLASSES_DOC;

    /**
     * ******** SSL Configuration
     ****************/
    String PrincipalBuilderClassDOC = SslConfigs.PRINCIPAL_BUILDER_CLASS_DOC;
    String SslProtocolDOC = SslConfigs.SSL_PROTOCOL_DOC;
    String SslProviderDOC = SslConfigs.SSL_PROVIDER_DOC;
    String SslCipherSuitesDOC = SslConfigs.SSL_CIPHER_SUITES_DOC;
    String SslEnabledProtocolsDOC = SslConfigs.SSL_ENABLED_PROTOCOLS_DOC;
    String SslKeystoreTypeDOC = SslConfigs.SSL_KEYSTORE_TYPE_DOC;
    String SslKeystoreLocationDOC = SslConfigs.SSL_KEYSTORE_LOCATION_DOC;
    String SslKeystorePasswordDOC = SslConfigs.SSL_KEYSTORE_PASSWORD_DOC;
    String SslKeyPasswordDOC = SslConfigs.SSL_KEY_PASSWORD_DOC;
    String SslTruststoreTypeDOC = SslConfigs.SSL_TRUSTSTORE_TYPE_DOC;
    String SslTruststorePasswordDOC = SslConfigs.SSL_TRUSTSTORE_PASSWORD_DOC;
    String SslTruststoreLocationDOC = SslConfigs.SSL_TRUSTSTORE_LOCATION_DOC;
    String SslKeyManagerAlgorithmDOC = SslConfigs.SSL_KEYMANAGER_ALGORITHM_DOC;
    String SslTrustManagerAlgorithmDOC = SslConfigs.SSL_TRUSTMANAGER_ALGORITHM_DOC;
    String SslEndpointIdentificationAlgorithmDOC = SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_DOC;
    String SslSecureRandomImplementationDOC = SslConfigs.SSL_SECURE_RANDOM_IMPLEMENTATION_DOC;
    String SslClientAuthDOC = SslConfigs.SSL_CLIENT_AUTH_DOC;

    /**
     * ******** Sasl Configuration
     ****************/
    String SaslMechanismInterBrokerProtocolDOC = "SASL mechanism used for inter-broker communication. Default is GSSAPI.";
    String SaslEnabledMechanismsDOC = SaslConfigs.SASL_ENABLED_MECHANISMS_DOC;
    String SaslKerberosServiceNameDOC = SaslConfigs.SASL_KERBEROS_SERVICE_NAME_DOC;
    String SaslKerberosKinitCmdDOC = SaslConfigs.SASL_KERBEROS_KINIT_CMD_DOC;
    String SaslKerberosTicketRenewWindowFactorDOC = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_WINDOW_FACTOR_DOC;
    String SaslKerberosTicketRenewJitterDOC = SaslConfigs.SASL_KERBEROS_TICKET_RENEW_JITTER_DOC;
    String SaslKerberosMinTimeBeforeReloginDOC = SaslConfigs.SASL_KERBEROS_MIN_TIME_BEFORE_RELOGIN_DOC;
    String SaslKerberosPrincipalToLocalRulesDOC = SaslConfigs.SASL_KERBEROS_PRINCIPAL_TO_LOCAL_RULES_DOC;
}