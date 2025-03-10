package com.siddarthmishra.springboot.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import com.siddarthmishra.springboot.constant.KafkaConsumerConstants;

@Service
public class DefaultConsumerService {

	@KafkaListener(id = "defaultConsumer01", containerFactory = "defaultContainerFactory01", topics = {
			KafkaConsumerConstants.SB_KAFKA_TOPIC_02_DLT }, groupId = "defaultConsumerGroup01")
	public void defaultComsumer01(ConsumerRecord<?, ?> record,
			@org.springframework.messaging.handler.annotation.Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String dltExceptionMessage) {
		String message = String.format(KafkaConsumerConstants.CONSUMER_RECEIVED_FORMAT, record.key(), record.value(),
				record.topic(), record.partition(), record.offset());
		System.out.println("Default Consumer: " + message);
		System.out.println("Dead Letter Topic Exception Message - " + dltExceptionMessage);
		Header[] headerArray = record.headers().toArray();
		Map<String, String> headerMap = Arrays.stream(headerArray).collect(Collectors.toMap(Header::key,
				header -> new String(header.value()), (oldValue, newValue) -> newValue, LinkedHashMap::new));
		System.out.println("Headers - " + headerMap);
	}
}
