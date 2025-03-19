package com.siddarthmishra.springboot.service;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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

	public void saveUserDetails(ConsumerRecord<String, UserDetailsDTO> record) {
		User savedUser;
		System.out.println("Inside ConsumerListenerService02.saveUserDetails - " + record);
		UserDetailsDTO userDTO = record.value();
		User user = new User(userDTO.getEmailId(), userDTO.getFirstName(), userDTO.getLastName());
		var violations = userValidator.validate(user);
		if (violations.isEmpty()) {
			Optional<User> dbUser = findByEmailId(user.getEmailId());
			if (dbUser.isEmpty()) {
				savedUser = userDetailsRepository.save(user);
				System.out.println("Saved - " + savedUser);
			} else {
				String message = String.format(KafkaConsumerConstants.EMAIL_ALREADY_PRESENT_FORMAT, user.getEmailId());
				throw new RuntimeException(message);
			}
		} else {
			String message = String.format(KafkaConsumerConstants.DATA_VIOLATIONS_FORMAT, violations.toString());
			throw new IllegalArgumentException(message);
		}
	}

	private Optional<User> findByEmailId(String emailId) {
		return userDetailsRepository.findByEmailId(emailId.toLowerCase());
	}
}
