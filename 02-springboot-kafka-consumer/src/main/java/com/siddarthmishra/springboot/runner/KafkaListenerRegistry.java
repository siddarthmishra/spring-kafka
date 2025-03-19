package com.siddarthmishra.springboot.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

import com.siddarthmishra.springboot.dto.UserDetailsDTO;

@Component
public class KafkaListenerRegistry implements CommandLineRunner {

	private ConcurrentKafkaListenerContainerFactory<String, UserDetailsDTO> containerFactory02;

	private KafkaListenerEndpoint kafkaListenerEndpoint02;

	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry; // spring provided

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Inside KafkaListenerRegistry.run");
		kafkaListenerEndpointRegistry.registerListenerContainer(kafkaListenerEndpoint02, containerFactory02, true);
	}

	public ConcurrentKafkaListenerContainerFactory<String, UserDetailsDTO> getContainerFactory02() {
		return containerFactory02;
	}

	public KafkaListenerEndpoint getKafkaListenerEndpoint02() {
		return kafkaListenerEndpoint02;
	}

	public KafkaListenerEndpointRegistry getKafkaListenerEndpointRegistry() {
		return kafkaListenerEndpointRegistry;
	}

	@Autowired
	public void setContainerFactory02(
			@Qualifier("containerFactory02") ConcurrentKafkaListenerContainerFactory<String, UserDetailsDTO> containerFactory02) {
		this.containerFactory02 = containerFactory02;
	}

	@Autowired
	public void setKafkaListenerEndpoint02(
			@Qualifier("kafkaListenerEndpoint02") KafkaListenerEndpoint kafkaListenerEndpoint02) {
		this.kafkaListenerEndpoint02 = kafkaListenerEndpoint02;
	}

	@Autowired
	public void setKafkaListenerEndpointRegistry(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
		this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
	}

}
