package com.siddarthmishra.springboot.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.siddarthmishra.springboot.constant.KafkaProducerConstants;
import com.siddarthmishra.springboot.dto.UserDetailsDTO;

@Service
public class ProducerService02 {

	private KafkaTemplate<String, UserDetailsDTO> kafkaTemplate;

	public ProducerService02(@Qualifier("kafkaTemplate02") KafkaTemplate<String, UserDetailsDTO> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendUserMessage(UserDetailsDTO userDetailsDTO) {
		userDetailsDTO.setEmailId(userDetailsDTO.getEmailId().trim().toLowerCase());
		var producerRecord = new ProducerRecord<String, UserDetailsDTO>(KafkaProducerConstants.SB_KAFKA_TOPIC_02,
				userDetailsDTO.getEmailId(), userDetailsDTO);
		kafkaTemplate.send(producerRecord);
	}

}
