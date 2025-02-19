package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;

@Order(1)
@Configuration
public class ProducerConfiguration01 {

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
		return TopicBuilder.name(KafkaProducerConstants.NEW_TOPIC_01).build();
	}

	@Bean("producerConfig01")
	Map<String, Object> producerConfig01() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProducerConstants.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return config;
	}

	@Bean("producerFactory01")
	ProducerFactory<String, String> producerFactory01() {
		var producerFactory = new DefaultKafkaProducerFactory<String, String>(producerConfig01());
		// producerFactory.setProducerPerThread(true);
		return producerFactory;
	}

	@Bean("kafkaTemplate01")
	KafkaTemplate<String, String> kafkaTemplate01() {
		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory01());
		kafkaTemplate.setProducerListener(customProducerListener01());
		return kafkaTemplate;
	}

	@Bean("customProducerListener01")
	ProducerListener<String, String> customProducerListener01() {
		return new ProducerListener<String, String>() {

			@Override
			public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
				String ack_msg = String.format(KafkaProducerConstants.PRODUCER_ACK_FORMAT, producerRecord.key(),
						producerRecord.value(), recordMetadata.topic(), recordMetadata.partition(),
						recordMetadata.offset());
				System.out.println(ack_msg);
			}

			@Override
			public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata,
					Exception exception) {
				String failed_msg = String.format(KafkaProducerConstants.PRODUCER_FAIL_FORMAT, producerRecord.key(),
						producerRecord.value(), recordMetadata.topic(), recordMetadata.partition(),
						recordMetadata.offset(), exception.toString());
				System.out.println(failed_msg);
			}
		};
	}
}
