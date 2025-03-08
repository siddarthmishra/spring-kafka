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
}
