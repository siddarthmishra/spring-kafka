package com.siddarthmishra.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siddarthmishra.springboot.dto.UserDetailsDTO;
import com.siddarthmishra.springboot.service.ProducerService02;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("kafka/producer/02")
public class ProducerRestController02 {

	private ProducerService02 producerService02;

	public ProducerRestController02(ProducerService02 producerService02) {
		this.producerService02 = producerService02;
	}

	@PostMapping("users")
	public ResponseEntity<String> sendUserMessage(@RequestBody UserDetailsDTO userDetailsDTO) {
		if (userDetailsDTO.getEmailId() == null || userDetailsDTO.getEmailId().trim().length() == 0) {
			return new ResponseEntity<String>("Email Id is null/empty", HttpStatus.BAD_REQUEST);
		}
		producerService02.sendUserMessage(userDetailsDTO);
		return ResponseEntity.accepted().build();
	}
}
