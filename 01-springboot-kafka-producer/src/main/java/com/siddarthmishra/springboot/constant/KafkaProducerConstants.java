package com.siddarthmishra.springboot.constant;

public interface KafkaProducerConstants {

	public static final String BOOTSTRAP_SERVERS_CONFIG = "0.0.0.0:9092";
	public static final String SB_KAFKA_TOPIC_01 = "sb-kafka-topic-01";
	public static final String PRODUCER_ACK_FORMAT = "Acknowledged - Key=%s ; Value=%s ; Topic=%s ; Partition=%s ; Offset=%s";
	public static final String PRODUCER_FAIL_FORMAT = "Failed - Key=%s ; Value=%s ; Topic=%s ; Partition=%s ; Offset=%s ; Error=%s";
	public static final String FAIL_IT_CONST = "[FAIL IT]";
	public static final String FAIL_IT_1_CONST = "[FAIL IT 1]";
	public static final String FAIL_IT_REGEX = "\\[FAIL IT\\]";
	public static final String FAIL_IT_1_REGEX = "\\[FAIL IT 1\\]";
}
