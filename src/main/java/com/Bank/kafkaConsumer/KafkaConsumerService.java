package com.Bank.kafkaConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.Bank.model.BankAccount;
import com.google.gson.Gson;

import org.apache.kafka.common.errors.SerializationException;

@Component
public class KafkaConsumerService {

    
    @Autowired
    Gson gson;
    
	@Value("${kafka.topic}")
	private String kafkatopic;

    @KafkaListener(topics = "account-creation", groupId = "test-consumer-group")
    public void listenToTopic(ConsumerRecord<String, String> record) {
        try {
        	System.out.println(record.value());
            String account = record.value(); 
             BankAccount acc=gson.fromJson(account, BankAccount.class);
            System.out.println("The  message received: " + account.toString());
        } catch (SerializationException e) {
            System.err.println("Error deserializing message: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

}
