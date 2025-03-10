package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.siddarthmishra.springboot.constant.KafkaConsumerConstants;

@Order(3)
@Configuration
public class DefaultConsumerConfiguration {

	@Bean("defaultConsumerConfig01")
	Map<String, Object> defaultConsumerConfig01() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConstants.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		config.put(ConsumerConfig.CLIENT_ID_CONFIG, "02-springboot-kafka-consumer-defaultConsumer");
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return config;
	}

	@Bean("defaultConsumerFactory01")
	ConsumerFactory<Object, Object> defaultConsumerFactory01() {
		var consumerFactory = new DefaultKafkaConsumerFactory<Object, Object>(defaultConsumerConfig01());
		return consumerFactory;
	}

	@Bean("defaultContainerFactory01")
	ConcurrentKafkaListenerContainerFactory<Object, Object> defaultContainerFactory01() {
		var containerFactory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		containerFactory.setConsumerFactory(defaultConsumerFactory01());
		updateContainerProperties(containerFactory.getContainerProperties());
		return containerFactory;
	}

	void updateContainerProperties(ContainerProperties containerProperties) {
		containerProperties.setAckMode(AckMode.RECORD);
		/*
		 * Since version 2.1.1, a new property called logContainerConfig is available.
		 * When true and INFO logging is enabled each listener container writes a log
		 * message summarizing its configuration properties.
		 */
		containerProperties.setLogContainerConfig(true);
		/* if syncCommits is true (default), commitSync() is used else commitAsync(). */
		containerProperties.setSyncCommits(false);
		/*
		 * setCommitCallback to get the results of asynchronous commits; the default
		 * callback is the LoggingCommitCallback which logs errors (and successes at
		 * debug level).
		 */
		containerProperties.setCommitCallback((offsets, exception) -> {
			if (exception != null) {
				System.out.println("Exception in Commit Callback");
				exception.printStackTrace();
			} else {
				System.out.println("SUCCESS - Commit Callback : " + offsets);
			}
		});
	}
}
