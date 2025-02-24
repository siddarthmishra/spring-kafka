package com.siddarthmishra.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siddarthmishra.springboot.service.ProducerService01;

@RestController
@RequestMapping("kafka/producer/01")
public class ProducerRestController01 {

	private ProducerService01 producerService01;

	public ProducerRestController01(ProducerService01 producerService01) {
		this.producerService01 = producerService01;
	}

	@PostMapping("simple")
	public ResponseEntity<String> simpleProducer(@RequestBody String request) {
		producerService01.send01(request);
		return ResponseEntity.accepted().build();
	}

}
