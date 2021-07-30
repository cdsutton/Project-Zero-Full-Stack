package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.util.ConnectionUtil;
import com.revature.dto.PostAccountDTO;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotRealtorsAccountException;
import com.revature.model.Account;

public class AccountRepository {
	
	private Connection connection;
	
	String accountType = "account_type";
	String amount = "amount";
	String exceptionMessage = "Exception message: ";
	String somethingWentWrongWhenTryingToGetAConnection = "Something went wrong when trying to get a connection. ";
	String somethingWentWrongWithTheDatabase = "Something went wrong with the database. ";
	
	public AccountRepository() {
		super();
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Account addAccount(int realtorId, PostAccountDTO accountDTO) throws DatabaseException {
		
		Account newAccount = null;
		String sql = "INSERT INTO accounts (account_type, amount, realtor_id) VALUES (?, ?, ?)";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, accountDTO.getAccountType());
			pstmt.setDouble(2, accountDTO.getAmount());
			pstmt.setInt(3, realtorId);

			int recordsAdded = pstmt.executeUpdate();

			if (recordsAdded != 1) {
				throw new DatabaseException("Couldn't add an account to the database");
			}

			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					int id = rs.getInt(1);
					newAccount = new Account(id, accountDTO.getAccountType(), accountDTO.getAmount());
				} else {
					throw new DatabaseException("Account id was not generated, and therefore adding an account failed");
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(somethingWentWrongWithTheDatabase
			+ exceptionMessage + e.getMessage());
		}
		
		return newAccount;
		
	}

	public List<Account> getAllAccounts(int realtorId) throws DatabaseException {
		
		List<Account> allAccounts = new ArrayList<>();
		Account retrievedAccount = null;
		String sql = "SELECT * FROM accounts WHERE realtor_id = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, realtorId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					String retrievedAccountType = rs.getString(accountType);
					double retrievedAmount = rs.getDouble(amount);
					retrievedAccount = new Account(id, retrievedAccountType, retrievedAmount);
					allAccounts.add(retrievedAccount);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(somethingWentWrongWithTheDatabase
			+ exceptionMessage + e.getMessage());
		}
		
		return allAccounts;
		
	}

	public List<Account> getSelectAccounts(int realtorId, double lessThan, double greaterThan)
			throws DatabaseException {
		
		List<Account> selectedAccounts = new ArrayList<>();
		Account retrievedAccount = null;
		String sql = "SELECT * FROM accounts WHERE realtor_id = ? AND amount <= ? AND amount >= ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, realtorId);
			pstmt.setDouble(2, lessThan);
			pstmt.setDouble(3, greaterThan);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					String retrievedAccountType = rs.getString(accountType);
					double retrievedAmount = rs.getDouble(amount);
					retrievedAccount = new Account(id, retrievedAccountType, retrievedAmount);
					selectedAccounts.add(retrievedAccount);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(somethingWentWrongWithTheDatabase
			+ exceptionMessage + e.getMessage());
		}

		return selectedAccounts;

	}

	public Account getAccountById(int realtorId, int accountId) throws DatabaseException {
		
		Account specifiedAccount = null;
		String sql = "SELECT * FROM accounts WHERE realtor_id = ? AND id = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, realtorId);
			pstmt.setInt(2, accountId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int id = rs.getInt("id");
					String retrievedAccountType = rs.getString(accountType);
					double retrievedAmount = rs.getDouble(amount);
					specifiedAccount = new Account(id, retrievedAccountType, retrievedAmount);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(somethingWentWrongWhenTryingToGetAConnection
			+ exceptionMessage + e.getMessage());
		}
		
		return specifiedAccount;
		
	}

	public Account updateAccount(int realtorId, int accountId, PostAccountDTO accountDTO)
			throws DatabaseException, NotRealtorsAccountException {
		
		Account updatedAccount = null;
		String sql = "UPDATE accounts SET account_type = ?, amount = ? WHERE realtor_id = ? AND id = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, accountDTO.getAccountType());
			pstmt.setDouble(2, accountDTO.getAmount());
			pstmt.setInt(3, realtorId);
			pstmt.setInt(4, accountId);

			int recordsUpdated = pstmt.executeUpdate();

			if (recordsUpdated != 1) {
				throw new NotRealtorsAccountException("This account does not belong to that realtor");
			}

			updatedAccount = new Account(accountId, accountDTO.getAccountType(), accountDTO.getAmount());

		} catch (SQLException e) {
			throw new DatabaseException(somethingWentWrongWhenTryingToGetAConnection
			+ exceptionMessage + e.getMessage());
		}
		
		return updatedAccount;
		
	}

	public void deleteAccount(int realtorId, int accountId) throws DatabaseException, NotRealtorsAccountException {
		
		String sql = "DELETE FROM accounts WHERE realtor_id = ? AND id = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, realtorId);
			pstmt.setInt(2, accountId);

			int recordsDeleted = pstmt.executeUpdate();

			if (recordsDeleted != 1) {
				throw new NotRealtorsAccountException("This account does not belong to that realtor");
			}
		} catch (SQLException e) {
			throw new DatabaseException(somethingWentWrongWhenTryingToGetAConnection
			+ exceptionMessage + e.getMessage());
		}
	}
}
