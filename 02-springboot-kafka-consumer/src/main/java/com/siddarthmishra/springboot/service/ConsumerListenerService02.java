package com.siddarthmishra.springboot.service;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.siddarthmishra.springboot.component.ObjectsValidator;
import com.siddarthmishra.springboot.constant.KafkaConsumerConstants;
import com.siddarthmishra.springboot.consumer.entity.User;
import com.siddarthmishra.springboot.consumer.repository.UserDetailsRepository;
import com.siddarthmishra.springboot.dto.UserDetailsDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConsumerListenerService02 {

	private UserDetailsRepository userDetailsRepository;

	private ObjectsValidator<User> userValidator;

	public ConsumerListenerService02(UserDetailsRepository userDetailsRepository,
			ObjectsValidator<User> userValidator) {
		this.userDetailsRepository = userDetailsRepository;
		this.userValidator = userValidator;
	}

	@KafkaListener(id = "kafkaListener02", containerFactory = "containerFactory02", topics = {
			KafkaConsumerConstants.SB_KAFKA_TOPIC_02 }, groupId = "groupConsumerListener02")
	public void saveUserDetails(ConsumerRecord<String, UserDetailsDTO> record) {
		User savedUser;
		System.out.println("Received : " + record.toString());
		UserDetailsDTO userDTO = record.value();
		User user = new User(userDTO.getEmailId(), userDTO.getFirstName(), userDTO.getLastName());
		var violations = userValidator.validate(user);
		if (violations.isEmpty()) {
			Optional<User> dbUser = findByEmailId(user.getEmailId());
			if (dbUser.isEmpty()) {
				savedUser = userDetailsRepository.save(user);
				System.out.println("Saved - " + savedUser);
			} else {
				// throw exception
				System.out.println("User is already present");
			}
		} else {
			// throw exception
			System.out.println("Errors - " + violations);
		}
	}

	private Optional<User> findByEmailId(String emailId) {
		return userDetailsRepository.findByEmailId(emailId.toLowerCase());
	}
}
