package com.siddarthmishra.springboot.service;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;
import com.siddarthmishra.springboot.dto.ProducerMessageDTO;

@Service
public class ProducerService01 {

	private KafkaTemplate<String, String> kafkaTemplate;

	public ProducerService01(@Qualifier("kafkaTemplate01") KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void send01(ProducerMessageDTO producerMessageDTO) {
		String key = producerMessageDTO.key();
		if (key == null || key.isBlank() || key.isEmpty()) {
			key = UUID.randomUUID().toString();
		}
		String value = producerMessageDTO.message() + " - " + key;
		var record = new ProducerRecord<String, String>(KafkaProducerConstants.SB_KAFKA_TOPIC_01, key, value);
		kafkaTemplate.send(record);
	}
}
