package com.Bank.model;

import com.Bank.smsmodel.SmsModel;

import lombok.Data;

@Data
public class BankAccount {
	private int accountId;
	private String accountHolderName;
	private double balance;
	private String toNumber;
	private double amount;
	SmsModel sms;

}
