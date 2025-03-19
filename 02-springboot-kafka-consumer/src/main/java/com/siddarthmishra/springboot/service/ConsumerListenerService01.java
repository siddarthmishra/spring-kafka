package com.siddarthmishra.springboot.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.siddarthmishra.springboot.constant.KafkaConsumerConstants;

@Service
public class ConsumerListenerService01 {

	@KafkaListener(id = "kafkaListener01", containerFactory = "containerFactory01", topics = {
			KafkaConsumerConstants.SB_KAFKA_TOPIC_01 }, groupId = "groupConsumerListener01")
	public void consumerListener01(ConsumerRecord<String, String> record) {
		System.out.println("Inside ConsumerListenerService01.consumerListener01 : " + record.toString());
	}
}
