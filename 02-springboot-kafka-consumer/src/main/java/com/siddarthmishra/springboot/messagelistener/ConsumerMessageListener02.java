package com.siddarthmishra.springboot.messagelistener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

import com.siddarthmishra.springboot.dto.UserDetailsDTO;
import com.siddarthmishra.springboot.service.ConsumerListenerService02;

public class ConsumerMessageListener02 implements MessageListener<String, UserDetailsDTO> {

	private ConsumerListenerService02 consumerListenerService02;

	public ConsumerMessageListener02(ConsumerListenerService02 consumerListenerService02) {
		this.consumerListenerService02 = consumerListenerService02;
	}

	@Override
	public void onMessage(ConsumerRecord<String, UserDetailsDTO> record) {
		System.out.println("Inside ConsumerMessageListener02.onMessage - " + record);
		consumerListenerService02.saveUserDetails(record);
	}
}
