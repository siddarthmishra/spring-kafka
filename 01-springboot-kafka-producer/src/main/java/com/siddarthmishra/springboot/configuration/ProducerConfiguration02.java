package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
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
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;
import com.siddarthmishra.springboot.dto.UserDetailsDTO;

@Order(3)
@Configuration
public class ProducerConfiguration02 {

	@Bean("producerConfig02")
	Map<String, Object> producerConfig02() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProducerConstants.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		config.put(ProducerConfig.CLIENT_ID_CONFIG, "01-springboot-kafka-producer-producer02");
		return config;
	}

	@Bean("producerFactory02")
	ProducerFactory<String, UserDetailsDTO> producerFactory02() {
		var producerFactory = new DefaultKafkaProducerFactory<String, UserDetailsDTO>(producerConfig02());
		return producerFactory;
	}

	@Bean("kafkaTemplate02")
	KafkaTemplate<String, UserDetailsDTO> kafkaTemplate02() {
		var kafkaTemplate = new KafkaTemplate<String, UserDetailsDTO>(producerFactory02());
		kafkaTemplate.setProducerListener(customProducerListener02());
		return kafkaTemplate;
	}

	@Bean("customProducerListener02")
	ProducerListener<String, UserDetailsDTO> customProducerListener02() {
		return new ProducerListener<String, UserDetailsDTO>() {

			@Override
			public void onSuccess(ProducerRecord<String, UserDetailsDTO> producerRecord,
					RecordMetadata recordMetadata) {
				String ack_msg = String.format(KafkaProducerConstants.PRODUCER_ACK_FORMAT, producerRecord.key(),
						producerRecord.value(), recordMetadata.topic(), recordMetadata.partition(),
						recordMetadata.offset());
				System.out.println(ack_msg);
			}

			@Override
			public void onError(ProducerRecord<String, UserDetailsDTO> producerRecord, RecordMetadata recordMetadata,
					Exception exception) {
				String failed_msg = String.format(KafkaProducerConstants.PRODUCER_FAIL_FORMAT, producerRecord.key(),
						producerRecord.value(), recordMetadata.topic(), recordMetadata.partition(),
						recordMetadata.offset(), exception.toString());
				System.out.println(failed_msg);

			}
		};
	}
}
