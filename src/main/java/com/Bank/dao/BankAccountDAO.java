package com.Bank.dao;

import org.slf4j.Logger;

import com.Bank.model.BankAccount;

public interface BankAccountDAO {
//	String createAccount(BankAccount account);
//    BankAccount getAccountById(int accountId);
    void updateAccount(BankAccount account,Logger logger);
    void deleteAccount(int accountId,Logger logger);
//	boolean isAccountExists(int accountId);
	String createAccount(BankAccount account, Logger logger);
	boolean isAccountExists(int accountId, Logger logger);
	BankAccount getAccountById(int accountId, Logger logger);
}
