package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.RecordInterceptor;

import com.siddarthmishra.springboot.constant.KafkaConsumerConstants;

@Order(1)
@Configuration
public class ConsumerConfiguration01 {

	@Bean("containerFactory01")
	ConcurrentKafkaListenerContainerFactory<String, String> containerFactory01(
			@Qualifier("comsumerFactory01") ConsumerFactory<String, String> consumerFactory) {
		var containerFactory = new ConcurrentKafkaListenerContainerFactory<String, String>();
		containerFactory.setConsumerFactory(consumerFactory);
		containerFactory.setRecordInterceptor(recordInterceptor01());
		/*
		 * Here, concurrency property creates two KafkaMessageListenerContainer
		 * instances
		 */
		// containerFactory.setConcurrency(2);
		updateContainerProperties(containerFactory.getContainerProperties());
		return containerFactory;
	}

	@Bean("comsumerFactory01")
	ConsumerFactory<String, String> comsumerFactory01() {
		var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerConfig01());
		return consumerFactory;
	}

	@Bean("consumerConfig01")
	Map<String, Object> consumerConfig01() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConstants.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		config.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		config.put(ConsumerConfig.CLIENT_ID_CONFIG, "02-springboot-kafka-consumer-consumer01");
		return config;
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
				System.out.println("ConsumerConfiguration01 - Exception in Commit Callback");
				exception.printStackTrace();
			} else {
				System.out.println("ConsumerConfiguration01 - SUCCESS - Commit Callback : " + offsets);
			}
		});
	}

	@Bean
	RecordInterceptor<String, String> recordInterceptor01() {
		return new RecordInterceptor<String, String>() {

			@Override
			public void success(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
				System.out.println("Inside ConsumerConfiguration01 RecordInterceptor.success");
			}

			@Override
			public void failure(ConsumerRecord<String, String> record, Exception exception,
					Consumer<String, String> consumer) {
				System.out.println("Inside ConsumerConfiguration01 RecordInterceptor.failure - " + exception.toString());
			}

			@Override
			public void afterRecord(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
				System.out.println("Inside ConsumerConfiguration01 RecordInterceptor.afterRecord");
			}

			@Override
			public ConsumerRecord<String, String> intercept(ConsumerRecord<String, String> record,
					Consumer<String, String> consumer) {
				System.out.println("Inside ConsumerConfiguration01 RecordInterceptor.intercept");
				return record;
				// return null; // If the interceptor returns null, the listener is not called
			}

		};
	}
}
