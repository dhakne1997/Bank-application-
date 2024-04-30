package com.Bank.service;

import org.slf4j.Logger;

import com.Bank.model.BankAccount;
import com.Bank.model.ResponseModel;

public interface BankAccountService {
	 String createAccount(BankAccount account, Logger logger);
	    
	    BankAccount getAccountDetails(int accountId, Logger logger);
	    
	    double updateAccount(BankAccount account,Logger logger);
	    
	    void deleteAccount(int accountId, Logger logger);
	    
//	    void withdraw(int accountId, double amount);
	    
//	    void deposit(int accountId, double amount);
	    
//	    void transfer(int fromAccountId, int toAccountId, double amount);
	    
//	    String checkBalance(int accountId);

		boolean isAccountExists(int accountId, Logger logger);//

		double withdraw(int accountId, double amount, Logger logger);

		double deposit(int accountId, double amount, Logger logger);

		ResponseModel transfer(int fromAccountId, int toAccountId, double amount, Logger logger) ;
		String checkBalance(int accountId, Logger logger);



}
