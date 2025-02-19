package com.siddarthmishra.springboot.controller;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;

@RestController
@RequestMapping("kafka/producer/01")
public class ProducerRestController01 {

	private KafkaTemplate<String, String> kafkaTemplate;

	public ProducerRestController01(@Qualifier("kafkaTemplate01") KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@PostMapping("simple")
	public ResponseEntity<String> simpleProducer(@RequestBody String request) {
		String uuid = java.util.UUID.randomUUID().toString();
		String value = request + " - " + uuid;
		var record = new ProducerRecord<String, String>(KafkaProducerConstants.NEW_TOPIC_01, uuid, value);
		kafkaTemplate.send(record);
		return ResponseEntity.accepted().build();
	}

}
