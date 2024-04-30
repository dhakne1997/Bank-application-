package com.Bank.kafkaProducer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.Bank.model.BankAccount;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        
        try {
            return new DefaultKafkaProducerFactory<>(configProps);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create Kafka producer factory", ex);
        }
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		System.out.println("Kafka template Primary");
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public JsonSerializer<BankAccount> bankAccountJsonSerializer() {
		return new JsonSerializer<>();
	}

}