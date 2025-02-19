package com.siddarthmishra.springboot.constant;

public interface KafkaProducerConstants {

	public static final String BOOTSTRAP_SERVERS_CONFIG = "0.0.0.0:9092";
	public static final String NEW_TOPIC_01 = "sb-kafka-topic-01";
	public static final String PRODUCER_ACK_FORMAT = "Acknowledged - Key=%s ; Value=%s ; Topic=%s ; Partition=%s ; Offset=%s";
	public static final String PRODUCER_FAIL_FORMAT = "Failed - Key=%s ; Value=%s ; Topic=%s ; Partition=%s ; Offset=%s ; Error=%s";
}
