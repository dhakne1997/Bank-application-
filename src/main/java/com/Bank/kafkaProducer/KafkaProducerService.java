package com.Bank.kafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Bank.constant.Constants;
import com.Bank.model.BankAccount;
import com.Bank.smsmodel.SmsModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class KafkaProducerService {


	@Value("${kafka.topic}")
	private String kafkatopic;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	
	@Autowired
	Gson gson;


	public void sendAccountCreationEvent(BankAccount account) {
		try {
			kafkaTemplate.send(kafkatopic, Constants.KAFKA_TOPIC_KEY.NEW_ORDER, gson.toJson(account));
			System.out.println("AccountCreation Successful send request");
		} catch (Exception e) {
			// Handle exception
			e.printStackTrace();
		}
	}

	public void sendAccountUpdateEvent(BankAccount account) {
		try {
			kafkaTemplate.send(kafkatopic, Constants.KAFKA_TOPIC_KEY.UPDATE_REQ, gson.toJson(account));
			System.out.println("AccountUpdation Successful send request");
		} catch (Exception e) {
			// Handle exception
			e.printStackTrace();
		}

	}

	public void sendAccountDeleteEvent(int accountId, SmsModel sms) {
		try {
			  JsonObject jsonObject = new JsonObject();
		        jsonObject.addProperty("accountId", accountId);
		        String json = gson.toJson(jsonObject);
			kafkaTemplate.send(kafkatopic, Constants.KAFKA_TOPIC_KEY.DELETE_REQ, json);
			System.out.println("Account deletion event sent successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
