package com.Bank.serviceimpl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bank.constant.Constants;
import com.Bank.dao.BankAccountDAO;
import com.Bank.model.BankAccount;
import com.Bank.model.ResponseModel;
import com.Bank.service.BankAccountService;

@Service
public class BankAccountServiceImpl implements BankAccountService {

	@Autowired
	private BankAccountDAO bankAccountDAO;

//  account created  
	@Override
	public String createAccount(BankAccount account, Logger logger) {
		return bankAccountDAO.createAccount(account, logger);
	}

	// Check if account already exists
	@Override
	public boolean isAccountExists(int accountId, Logger logger) {
		return bankAccountDAO.isAccountExists(accountId, logger);
	}

	@Override
	public BankAccount getAccountDetails(int accountId, Logger logger) {
		return bankAccountDAO.getAccountById(accountId, logger);
	}

	 @Override
	    public double updateAccount(BankAccount account,Logger logger) {
	        bankAccountDAO.updateAccount(account,logger);
	        return account.getBalance();
	    }

	@Override
	public void deleteAccount(int accountId, Logger logger) {
		bankAccountDAO.deleteAccount(accountId, logger);
	}

	@Override
	public double withdraw(int accountId, double amount, Logger logger) {
		try {
			// Log the attempt to withdraw
			logger.info("Attempting to withdraw {} from account with ID: {}", amount, accountId);

			BankAccount account = bankAccountDAO.getAccountById(accountId, logger);
			 if (account != null && account.getBalance() >= amount) {
		            double remainingBalance = account.getBalance() - amount;
		            account.setBalance(remainingBalance);
				bankAccountDAO.updateAccount(account, logger);

				// Log successful withdrawal
				logger.info("Withdrawal of {} from account with ID {} successful", account);
				return remainingBalance;
			} else {
				throw new IllegalArgumentException("Insufficient funds or invalid account");
			}
		} catch (Exception e) {
			// Log any exceptions that occur during withdrawal
			logger.error("Error occurred while withdrawing from account with ID {}: {}", accountId, e.getMessage(), e);
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public double deposit(int accountId, double amount, Logger logger) {
		try {
			// Log the attempt to deposit
			logger.info("Attempting to deposit {} into account with ID: {}", amount, accountId);

			BankAccount account = bankAccountDAO.getAccountById(accountId, logger);
			if (account != null) {
	        	double currentBalance = account.getBalance();
	            double newBalance = currentBalance + amount;
	            account.setBalance(newBalance);
				bankAccountDAO.updateAccount(account, logger);
				return newBalance;
				// Log successful deposit
			} else {
				throw new IllegalArgumentException("Invalid account");
			}
		} catch (Exception e) {
			// Log any exceptions that occur during deposit
			logger.error("Error occurred while depositing into account with ID {}: {}", accountId, e.getMessage(), e);
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public ResponseModel transfer(int fromAccountId, int toAccountId, double amount, Logger logger) {
		ResponseModel response = new ResponseModel();
		try {

			BankAccount bankAccount = new BankAccount();
			// Log the attempt to transfer
			String transferAttemptLog = String.format(
					"Attempting to transfer %f from account with ID %d to account with ID %d", amount, fromAccountId,
					toAccountId);
			logger.info(transferAttemptLog);

			BankAccount fromAccount = bankAccountDAO.getAccountById(fromAccountId, logger);
			BankAccount toAccount = bankAccountDAO.getAccountById(toAccountId, logger);

			if (fromAccount != null && toAccount != null && fromAccount.getBalance() >= amount) {
				fromAccount.setBalance(fromAccount.getBalance() - amount);
				toAccount.setBalance(toAccount.getBalance() + amount);

				bankAccountDAO.updateAccount(fromAccount, logger);
				bankAccountDAO.updateAccount(toAccount, logger);
				// Log successful transfer
				String successLog = String.format(
						"Transfer of %f from account with ID %d to account with ID %d successful", amount,
						fromAccountId, toAccountId);
				logger.info(successLog);
				response.setMessage("Transfer successful");
				response.setErrorCode(Constants.success);
				response.setSucess(true);
				return response;
			} else {
				response.setMessage("Insufficient Balance");
				response.setSucess(false);
				response.setErrorCode(Constants.insufficientBal);
				return response;
			}
		} catch (Exception e) {
			// Log any exceptions that occur during transfer
			String exceptionLog = String.format(
					"Error occurred during transfer from account with ID %d to account with ID %d: %s", fromAccountId,
					toAccountId, e.getMessage());
			logger.error(exceptionLog, e);
			e.printStackTrace();
			return response;
		}
	}

	@Override
	public String checkBalance(int accountId, Logger logger) {
		try {
			// Log the attempt to check balance
			logger.info("Attempting to check balance for account with ID: {}", accountId);

			BankAccount account = bankAccountDAO.getAccountById(accountId, logger);
			if (account != null) {
				String balanceInfo = "AccountHolderName: " + account.getAccountHolderName() + ": " + "Balance" + " Rs."
						+ account.getBalance() + "\n" + "AccountId: " + account.getAccountId();

				// Log successful balance check
				logger.info("Balance checked successfully for account with ID {}: {}", balanceInfo);

				return balanceInfo;
			} else {
				throw new IllegalArgumentException("Invalid account");
			}
		} catch (Exception e) {
			// Log any exceptions that occur during balance check
			logger.error("Error occurred while checking balance for account with ID {}: {}", accountId, e.getMessage(),
					e);
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

}
