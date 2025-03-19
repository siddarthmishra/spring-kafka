# spring-kafka

- [Apache Kafka - Basics](https://github.com/siddarthmishra/apache-kafka/blob/master/README.md)
- [Spring for Apache Kafka - Official Documentation](https://docs.spring.io/spring-kafka/reference/index.html)
- [Spring for Apache Kafka Samples](https://github.com/spring-projects/spring-kafka/tree/main/samples)
- [Apache Kafka using Spring Boot - HowToDoInJava](https://howtodoinjava.com/spring-boot/apache-kafka-using-spring-boot/)

####(Dynamic Kafka Listener(Consumer without `@KafkaListener`) creation with Spring)[https://medium.com/@putnin.v/dynamic-kafka-listener-consumer-creation-with-spring-4f8f359d715e]
Create the configurations like `ConsumerConfig`, `DefaultKafkaConsumerFactory`, `ConcurrentKafkaListenerContainerFactory`, `DefaultErrorHandler`, `RecordInterceptor` and so on as is. Now, DO NOT annotate the method with `@KafkaListener`. Instead do the following.
1. Create a template class by implementing the `MessageListener` interface (or similar [Message Listeners](https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages/message-listeners.html) as needed). The class `ConsumerMessageListener02` which implements `MessageListener` is created. Also, create a bean using `@Bean` or `@Component`.
2. Create `KafkaListenerEndpoint` with the template (from point 1). Here I have used `MethodKafkaListenerEndpoint`. A bean with name `kafkaListenerEndpoint02` is created in the configuration class `ConsumerConfiguration02.java`. All the required values have been set.
3. Register the `KafkaListenerEndpoint` and `ConcurrentKafkaListenerContainerFactory` (or `KafkaListenerContainerFactory`) with the `KafkaListenerEndpointRegistry` (a bean provided by spring). So , here, I have created a custom class `KafkaListenerRegistry` which is implementation of `CommandLineRunner`. The `run` method registers the end point and the container listener.
4. Optional - I have intergated a service class `ConsumerListenerService02` in the template class (from point 1). This service connects to the database.
5. Test the consumer. The messages will be consumed without using `@KafkaListener`.
