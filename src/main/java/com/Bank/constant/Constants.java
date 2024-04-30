package com.Bank.constant;

public interface Constants {

	int insufficientBal = 100;
	int isAccountExists=101;
	int sourceaccount_Not_exist=102;
	int destination_account_Not_exist=103;
	int success=200;
	
	
	interface KAFKA_TOPIC_KEY {
		String NEW_ORDER = "NEW_ORDER";
		String CANCEL_ORDER_REQ = "CANCEL_ORDER";
		String UPDATE_REQ = "UPDATE_REQ";
		String DELETE_REQ="DELETE_REQ";
	}
	

}
