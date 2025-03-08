package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;

@Order(2)
@Configuration
public class ProducerConfiguration01 {

	@Bean("producerConfig01")
	Map<String, Object> producerConfig01() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProducerConstants.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		config.put(ProducerConfig.CLIENT_ID_CONFIG, "01-springboot-kafka-producer-producer01");
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
		kafkaTemplate.setProducerInterceptor(producerInterceptor01());
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

	@Bean
	ProducerInterceptor<String, String> producerInterceptor01() {
		return new ProducerInterceptor<String, String>() {

			@Override
			public void configure(Map<String, ?> configs) {
				System.out.println("ProducerInterceptor.configure() called");
			}

			@Override
			public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
				System.out.println("ProducerInterceptor.onSend() called");
				String originalValue = record.value();
				if (originalValue.contains(KafkaProducerConstants.FAIL_IT_1_CONST)) {
					originalValue = originalValue.replaceAll(KafkaProducerConstants.FAIL_IT_1_REGEX, "");
				} else if (originalValue.contains(KafkaProducerConstants.FAIL_IT_CONST)) {
					originalValue = originalValue.replaceAll(KafkaProducerConstants.FAIL_IT_REGEX,
							KafkaProducerConstants.FAIL_IT_1_CONST);
				}
				return new ProducerRecord<String, String>(record.topic(), record.partition(), record.timestamp(),
						record.key(), originalValue, record.headers());
			}

			@Override
			public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
				System.out.println("ProducerInterceptor.onAcknowledgement() called");
			}

			@Override
			public void close() {
				System.out.println("ProducerInterceptor.close() called");
			}
		};
	}
}
