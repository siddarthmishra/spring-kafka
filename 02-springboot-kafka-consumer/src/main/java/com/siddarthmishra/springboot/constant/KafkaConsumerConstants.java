package com.siddarthmishra.springboot.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface KafkaConsumerConstants {

	public static final String BOOTSTRAP_SERVERS_CONFIG = "0.0.0.0:9092";
	public static final String SB_KAFKA_TOPIC_01 = "sb-kafka-topic-01";
	public static final String SB_KAFKA_TOPIC_02 = "sb-kafka-topic-02";
	public static final List<String> TRUSTED_PACKAGES_LIST = Collections
			.unmodifiableList(Arrays.asList("com.siddarthmishra.springboot.dto", "some.other.package"));
	public static final String COMMA = ",";
	public static final String EMAIL_ALREADY_PRESENT_FORMAT = "Email '%s' is already enrolled";
	public static final String DATA_VIOLATIONS_FORMAT = "Data violations - %s";
	public static final String SB_KAFKA_TOPIC_02_DLT = "sb-kafka-topic-02-dlt";
	public static final String CONSUMER_RECEIVED_FORMAT = "Received - Key=%s ; Value=%s ; Topic=%s ; Partition=%s ; Offset=%s";
}
