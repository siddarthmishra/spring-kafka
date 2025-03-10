package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

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
	ConcurrentKafkaListenerContainerFactory<String, UserDetailsDTO> containerFactory02(
			@Qualifier("defaultKafkaTemplate01") KafkaTemplate<Object, Object> template) {
		var containerFactory = new ConcurrentKafkaListenerContainerFactory<String, UserDetailsDTO>();
		containerFactory.setConsumerFactory(consumerFactory02());
		containerFactory.setRecordInterceptor(recordInterceptor02());
		// containerFactory.setCommonErrorHandler(defaultErrorHandler02_01());
		containerFactory.setCommonErrorHandler(deadLetterPublishingRecoverer02_01(template));
		updateContainerProperties(containerFactory.getContainerProperties());
		return containerFactory;
	}

	// https://docs.spring.io/spring-kafka/reference/kafka/annotation-error-handling.html#default-eh
	@SuppressWarnings("unused")
	private CommonErrorHandler defaultErrorHandler02_01() {
		/*
		 * retries the failed consumer events for 3 times (0,1,2) at interval of 5
		 * seconds (default interval). Failures are simply logged after retries are
		 * exhausted.
		 */
		var defaultErrorHandler = new DefaultErrorHandler(new FixedBackOff(FixedBackOff.DEFAULT_INTERVAL, 2));
		/*
		 * since some exceptions are unlikely to be resolved on a retried delivery, such
		 * exceptions can be excluded from retries by adding such exceptions as
		 * "Not Retryable Exceptions"
		 */
		defaultErrorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
		return defaultErrorHandler;
	}

	// https://docs.spring.io/spring-kafka/reference/kafka/annotation-error-handling.html#dead-letters
	/*
	 * The framework provides the DeadLetterPublishingRecoverer, which publishes the
	 * failed message to another topic. By default, the dead-letter record is sent
	 * to a topic named <originalTopic>-dlt (the original topic name suffixed with
	 * -dlt) and to the same partition as the original record. Therefore, when you
	 * use the default resolver, the dead-letter topic must have at least as many
	 * partitions as the original topic. We can override the default behavior of
	 * <originalTopic>-dlt by providing 'destinationResolver' through constructor.
	 */
	private CommonErrorHandler deadLetterPublishingRecoverer02_01(KafkaTemplate<Object, Object> template) {
		/*
		 * destinationResolver copied as is from DeadLetterPublishingRecoverer
		 * DEFAULT_DESTINATION_RESOLVER
		 */
		BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> destinationResolver = (cr,
				e) -> new TopicPartition(cr.topic() + "-dlt", cr.partition());
		var deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(template, destinationResolver);
		var fixedBackOff = new FixedBackOff(FixedBackOff.DEFAULT_INTERVAL, 2);
		// var defaultErrorHandler = new DefaultErrorHandler(fixedBackOff);
		var defaultErrorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer, fixedBackOff);
		defaultErrorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
		return defaultErrorHandler;
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
