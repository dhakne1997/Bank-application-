package com.Bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Bank.constant.Constants;
import com.Bank.kafkaProducer.KafkaProducerService;
import com.Bank.model.BankAccount;
import com.Bank.model.ResponseModel;
import com.Bank.service.BankAccountService;
import com.Bank.smsService.SMSService;
import com.Bank.smsmodel.SmsModel;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

	 @Autowired
	    private BankAccountService bankAccountService;

	    @Autowired
	    private KafkaProducerService kafkaProducerService;
	    
//	    @Autowired
//	    private EmailService emailService;
	    
	    @Autowired
	    private SMSService smsService;
	    
	    SmsModel smsModel=new SmsModel();
	 
	private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);

	@PostMapping("/create")
	public ResponseEntity<String> createAccount(@RequestBody BankAccount account) {
		try {
			logger.info("Received request to create account with ID: {}", account.getAccountId());
			if (bankAccountService.isAccountExists(account.getAccountId(), logger)) {
				logger.warn("Account creation failed: Account {} already exists", account.getAccountId());
				return ResponseEntity.badRequest().body("Account already exists!");
			}
			String message = bankAccountService.createAccount(account, logger);
		       kafkaProducerService.sendAccountCreationEvent(account);
		       smsService.sendCreateSMS(account.getSms(),account);
			logger.info("Account created successfully with ID: {}", account.getAccountId());
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			logger.error("Error occurred while creating account: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while creating account: " + e.getMessage());
		}
	}

	
	// Get account details by ID
	@GetMapping("/{accountId}")
	public ResponseEntity<?> getAccountDetails(@PathVariable int accountId) {
		try {
			logger.info("Received request to get account details for ID: {}", accountId);
			if (!bankAccountService.isAccountExists(accountId, logger)) {
				logger.info("No such account present in Database for ID: {}", accountId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not present");
			}
			BankAccount account = bankAccountService.getAccountDetails(accountId, logger);
			logger.info("Account details retrieved successfully for ID: {}", accountId);
			return ResponseEntity.ok(account);
		} catch (Exception e) {
			logger.error("Error occurred while fetching account details for ID {}: {}", accountId, e.getMessage(), e);
			throw e;
		}
	}

	// Update account details
	@PutMapping("/update")
	public String updateAccount(@RequestBody BankAccount account) {
		try {
			logger.info("Received request to update account details for ID: {}", account.getAccountId());
			if (!bankAccountService.isAccountExists(account.getAccountId(), logger)) {
				logger.info("No such account present in Database for ID: {}", account.getAccountId());
				return ("No such account present");
			}
			
			double remainingBalance=bankAccountService.updateAccount(account, logger);
			logger.info("Account details updated successfully for ID: {}", account.getAccountId());
			 kafkaProducerService.sendAccountUpdateEvent(account);
		        smsService.sendUpdateSMS(account.getSms(),account,remainingBalance);
		        return "Account updated successfully!";
		} catch (Exception e) {
			logger.error("Error occurred while updating account details for ID {}: {}", account.getAccountId(),
					e.getMessage(), e);
			throw e;
		}
	}

	// Delete account by ID
	@DeleteMapping("/delete/{accountId}")
	public String deleteAccount(@PathVariable int accountId,@RequestBody SmsModel sms) {
		int acc=Integer.valueOf(accountId);
    	BankAccount account=new BankAccount();
		try {
			logger.info("Received request to delete account with ID: {}", accountId);
			if (!bankAccountService.isAccountExists(accountId, logger)) {
				logger.info("No such account present in Database for deletion with ID: {}", accountId);
				return "No such account present in Database";
			}
			bankAccountService.deleteAccount(accountId, logger);
			logger.info("Account with ID {} deleted successfully", accountId);
			kafkaProducerService.sendAccountDeleteEvent(acc, sms);
			smsService.sendDeleteSms(sms,accountId);
			return "Account deleted successfully!";
		} catch (Exception e) {
			logger.error("An error occurred while deleting the account with ID {}: {}", accountId, e.getMessage(), e);
			return "An error occurred while deleting the account: " + e.getMessage();
		}
	}

	// Withdraw money from account
	@PostMapping("/withdraw")
	public String withdraw(@RequestParam int accountId, @RequestParam double amount,@RequestBody SmsModel sms) {
		try {
			BankAccount account = new BankAccount();
			logger.info("Received request to withdraw {} from account with ID: {}", amount, accountId);

			if (!bankAccountService.isAccountExists(accountId, logger)) {
				logger.info("No such account present in Database for withdraw with ID: {}", accountId);
				return "No such account withdraw present in Database";
			}
			double remainingBalance =bankAccountService.withdraw(accountId, amount, logger);
			logger.info("Amount {} withdrawn successfully from account with ID: {}", amount, accountId);
			   smsService.sendWithDrawSms(sms,amount, remainingBalance);
			return "Amount withdrawn successfully!";
		} catch (IllegalArgumentException e) {
			logger.error("Insufficient funds or invalid account while withdrawing from account with ID {}: {}",
					accountId, e.getMessage(), e);
			return "Insufficient funds or invalid account";
		} catch (Exception e) {
			logger.error("An error occurred while withdrawing from account with ID {}: {}", accountId, e.getMessage(),
					e);
			return "An error occurred while withdrawing amount: " + e.getMessage();
		}
	}

	// Deposit money into account
	@PostMapping("/deposit")
	public String deposit(@RequestParam int accountId, @RequestParam double amount,@RequestBody SmsModel sms) {
		try {
			logger.info("Received request to deposit {} into account with ID: {}", amount, accountId);
			if (!bankAccountService.isAccountExists(accountId, logger)) {
				logger.info("No such account present in Database for deposit with ID: {}", accountId);
				return "You can't deposit because the account is not present.";
			}
			double remainingBalance=bankAccountService.deposit(accountId, amount, logger);
			logger.info("Amount {} deposited successfully into account with ID: {}", amount, accountId);
			 smsService.sendDepositeSms(sms,amount, remainingBalance);
			return "Amount deposited successfully!";
		} catch (Exception e) {
			logger.error("Error occurred while depositing amount into account with ID {}: {}", accountId,
					e.getMessage(), e);
			return "Error occurred while depositing amount: " + e.getMessage();
		}
	}

	// Transfer money between accounts
	@PostMapping("/transfer")
	public ResponseModel transfer(@RequestParam int fromAccountId, @RequestParam int toAccountId,
			@RequestParam double amount,@RequestBody SmsModel sms) {
		BankAccount account = new BankAccount();
		ResponseModel response = new ResponseModel();
		try {
			logger.info("Received request to transfer {} from account with ID {} to account with ID {}", amount,
					fromAccountId, toAccountId);
			if (!bankAccountService.isAccountExists(fromAccountId, logger)) {
				// logger.info("Source account with ID {} is not present in Database for
				// transfer", fromAccountId);
				response.setErrorCode(Constants.sourceaccount_Not_exist);
				response.setMessage("Source Account Doesnt Exist in Db.");
				response.setSucess(false);
				return response;
			}
			if (!bankAccountService.isAccountExists(toAccountId, logger)) {
				logger.info("Destination account with ID {} is not present in Database for transfer", toAccountId);
				response.setErrorCode(Constants.destination_account_Not_exist);
				response.setMessage("Destination Account Doesnt Exist in Db.");
				response.setSucess(false);
				return response;
			}
			
			ResponseModel transfer = bankAccountService.transfer(fromAccountId, toAccountId, amount, logger);
			logger.info("Amount {} transferred successfully from account with ID {} to account with ID {}", 
					amount,fromAccountId, toAccountId);
	        smsService.sendTransferSms(sms,amount, transfer);
			return transfer;
			
			

		} catch (IllegalArgumentException e) {

			logger.error(
					"Insufficient funds while transferring amount from account with ID {} to account with ID {}: {}",
					fromAccountId, toAccountId, e.getMessage(), e);
			return response;
		} catch (Exception e) {
			logger.error("Error occurred while transferring amount from account with ID {} to account with ID {}: {}",
					fromAccountId, toAccountId, e.getMessage(), e);
			return response;
		}
	}

	// Check account balance
	@GetMapping("/balance/{accountId}")
	public String checkBalance(@PathVariable int accountId,@RequestBody SmsModel sms) {
		BankAccount account=new BankAccount();
		try {
			logger.info("Received request to check balance for account with ID: {}", accountId);
			if (!bankAccountService.isAccountExists(accountId, logger)) {
				logger.info("Account with ID {} is not present in Database for balance check", accountId);
				return "You can't check balance because the account number " + accountId + " is not present.";
			}
			String balance = bankAccountService.checkBalance(accountId, logger);
			logger.info("Balance checked successfully for account with ID {}: {}", accountId, balance);
	         smsService.sendCheckBalanceSms(sms,accountId,account.getBalance());

			return balance;
		} catch (Exception e) {
			logger.error("Error occurred while checking balance for account with ID {}: {}", accountId, e.getMessage(),
					e);
			return "Error occurred while checking balance: " + e.getMessage();
		}
	}
}
