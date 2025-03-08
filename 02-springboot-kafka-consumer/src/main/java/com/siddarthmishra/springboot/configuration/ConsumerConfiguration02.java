package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.siddarthmishra.springboot.constant.KafkaConsumerConstants;
import com.siddarthmishra.springboot.dto.UserDetailsDTO;

@Order(2)
@Configuration
public class ConsumerConfiguration02 {

	@Bean("consumerConfig02")
	Map<String, Object> consumerConfig02() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConstants.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		config.put(ConsumerConfig.CLIENT_ID_CONFIG, "02-springboot-kafka-consumer-consumer02");
		String trustedPackages = KafkaConsumerConstants.TRUSTED_PACKAGES_LIST.stream()
				.collect(Collectors.joining(KafkaConsumerConstants.COMMA));
		config.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackages);
		return config;
	}

	@Bean("consumerFactory02")
	ConsumerFactory<String, UserDetailsDTO> consumerFactory02() {
		var consumerFactory = new DefaultKafkaConsumerFactory<String, UserDetailsDTO>(consumerConfig02());
		return consumerFactory;
	}

	@Bean("containerFactory02")
	ConcurrentKafkaListenerContainerFactory<String, UserDetailsDTO> containerFactory02() {
		var containerFactory = new ConcurrentKafkaListenerContainerFactory<String, UserDetailsDTO>();
		containerFactory.setConsumerFactory(consumerFactory02());
		containerFactory.setRecordInterceptor(recordInterceptor02());
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

	@Bean("recordInterceptor02")
	RecordInterceptor<String, UserDetailsDTO> recordInterceptor02() {
		return new RecordInterceptor<String, UserDetailsDTO>() {

			@Override
			public void success(ConsumerRecord<String, UserDetailsDTO> record,
					Consumer<String, UserDetailsDTO> consumer) {
				System.out.println("Inside RecordInterceptor.success");
			}

			@Override
			public void failure(ConsumerRecord<String, UserDetailsDTO> record, Exception exception,
					Consumer<String, UserDetailsDTO> consumer) {
				System.out.println("Inside RecordInterceptor.failure - " + exception.toString());
			}

			@Override
			public void afterRecord(ConsumerRecord<String, UserDetailsDTO> record,
					Consumer<String, UserDetailsDTO> consumer) {
				System.out.println("Inside RecordInterceptor.afterRecord");
			}

			@Override
			public ConsumerRecord<String, UserDetailsDTO> intercept(ConsumerRecord<String, UserDetailsDTO> record,
					Consumer<String, UserDetailsDTO> consumer) {
				System.out.println("Inside RecordInterceptor.intercept");
				return record;
				// return null; // If the interceptor returns null, the listener is not called
			}

		};
	}
}
