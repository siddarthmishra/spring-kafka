package com.siddarthmishra.springboot.service;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;

@Service
public class ProducerService01 {

	private KafkaTemplate<String, String> kafkaTemplate;

	public ProducerService01(@Qualifier("kafkaTemplate01") KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void send01(String request) {
		String uuid = UUID.randomUUID().toString();
		String value = request + " - " + uuid;
		var record = new ProducerRecord<String, String>(KafkaProducerConstants.SB_KAFKA_TOPIC_01, uuid, value);
		kafkaTemplate.send(record);
	}
}
