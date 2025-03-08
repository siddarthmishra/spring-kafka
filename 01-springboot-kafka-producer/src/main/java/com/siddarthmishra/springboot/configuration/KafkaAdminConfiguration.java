package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;

@Order(1)
@Configuration
public class KafkaAdminConfiguration {

	@Bean("sb-kafka-admin-01")
	KafkaAdmin kafkaAdmin() {
		Map<String, Object> config = new HashMap<>();
		config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProducerConstants.BOOTSTRAP_SERVERS_CONFIG);
		KafkaAdmin kafkaAdmin = new KafkaAdmin(config);
		kafkaAdmin.setFatalIfBrokerNotAvailable(true); // The context fails to initialize when broker not available.
		return kafkaAdmin;
	}

	@Bean("sb-kafka-topic-01")
	NewTopic topic1() {
		/*
		 * Starting with version 2.6, you can omit partitions() and/or replicas() and
		 * the broker defaults will be applied to those properties. The broker version
		 * must be at least 2.4.0 to support this feature.
		 */
		// return
		// TopicBuilder.name(KafkaConstants.NEW_TOPIC_01).partitions(2).replicas(1).build();
		return TopicBuilder.name(KafkaProducerConstants.SB_KAFKA_TOPIC_01).build();
	}

	@Bean("sb-kafka-topic-02")
	NewTopic topic2() {
		return TopicBuilder.name(KafkaProducerConstants.SB_KAFKA_TOPIC_02).build();
	}
}
