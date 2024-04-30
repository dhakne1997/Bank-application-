package com.Bank.daoimpl;

import java.sql.*;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.Bank.config.HikariConfigs;
import com.Bank.dao.BankAccountDAO;
import com.Bank.model.BankAccount;

@Repository
public class BankAccountDAOImpl implements BankAccountDAO {

	@Autowired
	
	HikariConfigs configs;
	
	
	@Override
	public String createAccount(BankAccount account,Logger logger) {
		try {
            if (isAccountExists(account.getAccountId(),logger)) {
                logger.info("Account already exists for account ID: " + account.getAccountId());
                return "Account already exists!";
            }
            String query = "INSERT INTO bank_accounts (account_id, account_holder_name, balance) VALUES (?, ?, ?)";
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try {
                connection = configs.dataSource().getConnection();
                preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, account.getAccountId());
                preparedStatement.setString(2, account.getAccountHolderName());
                preparedStatement.setDouble(3, account.getBalance());
                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setAccountId(generatedKeys.getInt(1));
                        logger.info("Account created Data:"+"\n"+account);
                    }
                }
                logger.info("Account created successfully for account ID: " + account.getAccountId());
                return "Account created successfully!";
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    logger.info("Error closing resources: " + e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            logger.info("Failed to create account: " + e.getMessage(), e);
            System.err.println("Failed to create account: " + e.getMessage());
            return "Failed to create account: " + e.getMessage();
        }
    }


		
	// Method to check if an account already exists
	public boolean isAccountExists(int accountId, Logger logger) {
		String query = "SELECT COUNT(*) FROM bank_accounts WHERE account_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
            logger.info("Checking if account exists with ID: " + accountId);
			connection = configs.dataSource().getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, accountId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
		} catch (SQLException e) {
			logger.error("Error checking if account exists: " + e.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Error closing resources: " + e.getMessage());
			}
		}
		return false;
	}

	
	@Override
	public BankAccount getAccountById(int accountId,Logger logger) {
		String query = "SELECT * FROM bank_accounts WHERE account_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = configs.dataSource().getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, accountId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				BankAccount account = new BankAccount();
				account.setAccountId(resultSet.getInt("account_id"));
				account.setAccountHolderName(resultSet.getString("account_holder_name"));
				account.setBalance(resultSet.getDouble("balance"));
				logger.info("Account Getting  Data:"+"\n"+account);
				return account;
			}
		} catch (SQLException e) {
			logger.error("Error retrieving account with ID " + accountId + ": " + e.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Error closing resources: " + e.getMessage());
			}
		}
		return null;
	}

	@Override
	public void updateAccount(BankAccount account,Logger logger) {
		String query = "UPDATE bank_accounts SET account_holder_name = ?, balance = ? WHERE account_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = configs.dataSource().getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, account.getAccountHolderName());
			preparedStatement.setDouble(2, account.getBalance());
			preparedStatement.setInt(3, account.getAccountId());
			preparedStatement.executeUpdate();
			logger.info("Account Updating  Data:"+"\n"+account);
		} catch (SQLException e) {
			logger.error("Error updating account: " + e.getMessage());
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Error closing resources: " + e.getMessage());
			}
		}
	}

	@Override
	public void deleteAccount(int accountId,Logger logger) {
		String query = "DELETE FROM bank_accounts WHERE account_id = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = configs.dataSource().getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, accountId);
			preparedStatement.executeUpdate();
			logger.info("Account Deleted  Data Query:"+"\n"+preparedStatement);
		} catch (SQLException e) {
			logger.error("Error deleting account with ID " + accountId + ": " + e.getMessage());
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Error closing resources: " + e.getMessage());
			}
		}
	}
    
}
