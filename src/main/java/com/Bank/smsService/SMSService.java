package com.Bank.smsService;


import com.Bank.model.BankAccount;
import com.Bank.model.ResponseModel;
import com.Bank.smsmodel.SmsModel;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.phone.number}")
   private String TWILIO_PHONE_NUMBER;

    @PostConstruct
    public void initializeTwilio() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    

    public void sendCreateSMS(SmsModel smsModel,BankAccount account) {
	
    	 String messageBody = "Dear Customer,\n\n"
    	            + "This is to inform you that your bank account has been successfully created.\n"
    	            + "Account Number: " + account.getAccountId() + "\n"
    	            + "Your Current Balance: Rs." + String.format("%.2f", account.getBalance()) + "\n\n"
    	            + "Thank you for choosing Dosti Bank.\n"
    	            + "Sincerely,\n"
    	            + "Dosti Bank";
    	 
    	 
    	// Send SMS message
 	    Message message = Message.creator(
 	            new PhoneNumber(smsModel.getToNumber()),  // To
 	            new PhoneNumber(TWILIO_PHONE_NUMBER),  // From
 	            messageBody)
 	            .create();

 	    System.out.println("Create SMS sent successfully. SID: " + message.getSid());
		
	}


	public void sendUpdateSMS(SmsModel sms, BankAccount account, double remainingBalance) {
	    String messageBody = "Dear Customer,\n\n"
	            + "This is to inform you that your account balance has been updated.\n"
	            + "Updated Balance: Rs." + String.format("%.2f", account.getBalance()) + "\n\n"
	            + "Thank you for banking with us.\n"
	            + "Sincerely,\n"
	            + "Dosti Bank";


	    // Send SMS message
	    Message message = Message.creator(
	            new PhoneNumber(sms.getToNumber()),  // To
	            new PhoneNumber(TWILIO_PHONE_NUMBER),  // From
	            messageBody)
	            .create();

	    System.out.println("Update SMS sent successfully. SID: " + message.getSid());
	}


	public void sendDeleteSms(SmsModel sms, int accountId) {
	    String messageBody = "Dear Customer,\n\n"
	            + "This is to inform you that your account with ID " + accountId + " has been deleted.\n\n"
	            + "Thank you for banking with us.\n"
	            + "Sincerely,\n"
	            + "Dosti Bank";

	    // Send SMS message
	    Message message = Message.creator(
	            new PhoneNumber(sms.getToNumber()),  // To
	            new PhoneNumber(TWILIO_PHONE_NUMBER),  // From
	            messageBody)
	            .create();

	    System.out.println("Delete SMS sent successfully. SID: ");
	}


	public void sendWithDrawSms(SmsModel sms, double amount, double remainingBalance) {
		String messageBody = "Dear Customer,\n\n";
		messageBody += "This is to inform you that a withdrawal transaction has been made from your account.\n";
		messageBody += "Transaction Details:\n";
		messageBody += "+------------------------+\n";
		messageBody += "| Amount Withdrawn: Rs." + String.format("%.2f", amount) + " |\n";
		messageBody += "| Remaining Balance: Rs." + String.format("%.2f", remainingBalance) + " |\n";
		messageBody += "+------------------------+\n\n";
		messageBody += "Thank you for banking with us.\n";
		messageBody += "Sincerely,\n";
		messageBody += "Dosti Bank";
	    
	    
		 // Send SMS message
	    Message message = Message.creator(
	            new PhoneNumber(sms.getToNumber()),  // To
	            new PhoneNumber(TWILIO_PHONE_NUMBER),  // From
	            messageBody)
	            .create();

	    System.out.println("WithDraw SMS sent successfully. SID: " + message.getSid());
	}


	

	public void sendDepositeSms(SmsModel sms, double amount, double remainingBalance) {
		 String messageBody = "Dear Customer,\n\n";
		 messageBody += "This is to inform you that a deposit transaction has been made into your account.\n";
		 messageBody += "Transaction Details:\n";
		 messageBody += "+------------------------+\n";
		 messageBody += "| Amount Deposited: Rs." + String.format("%.2f", amount) + " |\n";
		 messageBody += "| Remaining Balance: Rs." + String.format("%.2f", remainingBalance) + " |\n";
		 messageBody += "+------------------------+\n\n";
		 messageBody += "Thank you for banking with us.\n";
		 messageBody += "Sincerely,\n";
		 messageBody += "Dosti Bank";
		    
		    // Send SMS message
		    Message message = Message.creator(
		            new PhoneNumber(sms.getToNumber()),  // To
		            new PhoneNumber(TWILIO_PHONE_NUMBER),  // From
		            messageBody)
		            .create();

		    System.out.println("WithDraw SMS sent successfully. SID: " + message.getSid());
		}


	public void sendTransferSms(SmsModel sms, double amount, ResponseModel transfer) {
		// Get the current date and time
	    LocalDateTime currentTime = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedDateTime = currentTime.format(formatter);
		
		
	    String messageBody = "Dear recipient,\n\n"
	                        + "We're writing to inform you that a transfer of Rs." + amount + " has been successfully deposited into your account.\n\n"
	                        + "Transaction details:\n"
	                        + "Amount: Rs." + amount + "\n"
	                        + "Remaining Balance: Rs." + transfer + "\n"
	                        + "Transaction Date: " + formattedDateTime + "\n\n"
	                        + "If you have any questions or concerns, please feel free to contact us.\n\n"
	                        + "Thank you for choosing our services.\n\n"
	                        + "Sincerely,\nDosti Bank";
		
	 // Send SMS message
	    Message message = Message.creator(
	            new PhoneNumber(sms.getToNumber()),  // To
	            new PhoneNumber(TWILIO_PHONE_NUMBER),  // From
	            messageBody)
	            .create();

	    System.out.println("WithDraw SMS sent successfully. SID: " + message.getSid());
	
	}


	public void sendCheckBalanceSms(SmsModel sms, int accountId, double balance) {

	    String messageBody = "Dear Customer,\n\n"
	            + "This is to inform you that you have checked your account balance.\n"
	            + "Account Number: " + accountId + "\n"
	            + "Your Current Balance: Rs." + String.format("%.2f", balance) + "\n\n"
	            + "Thank you for choosing Dosti Bank.\n"
	            + "Sincerely,\n"
	            + "Dosti Bank";
		
	 // Send SMS message
	    Message message = Message.creator(
	            new PhoneNumber(sms.getToNumber()),  // To
	            new PhoneNumber(TWILIO_PHONE_NUMBER),  // From
	            messageBody)
	            .create();

	    System.out.println("WithDraw SMS sent successfully. SID: " + message.getSid());
	
	}


	
		
	}
		
	


	
	

