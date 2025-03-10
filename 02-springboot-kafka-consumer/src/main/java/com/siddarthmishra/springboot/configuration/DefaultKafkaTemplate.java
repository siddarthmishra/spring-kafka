package com.siddarthmishra.springboot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.siddarthmishra.springboot.constant.KafkaConsumerConstants;

@Order(0)
@Configuration
public class DefaultKafkaTemplate {

	@Bean("defaultDafkaTemplateConfig01")
	Map<String, Object> defaultDafkaTemplateConfig01() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConstants.BOOTSTRAP_SERVERS_CONFIG);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		config.put(ProducerConfig.CLIENT_ID_CONFIG, "02-springboot-kafka-consumer-defaultKafkaTemplate");
		return config;
	}

	@Bean("defaultKafkaTemplateProducerFactory01")
	ProducerFactory<Object, Object> defaultKafkaTemplateProducerFactory01() {
		var producerFactory = new DefaultKafkaProducerFactory<Object, Object>(defaultDafkaTemplateConfig01());
		return producerFactory;
	}

	@Bean("defaultKafkaTemplate01")
	KafkaTemplate<Object, Object> defaultKafkaTemplate01() {
		var kafkaTemplate = new KafkaTemplate<Object, Object>(defaultKafkaTemplateProducerFactory01());
		return kafkaTemplate;
	}
}
