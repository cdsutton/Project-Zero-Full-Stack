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
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotRealtorsAccountException;
import com.revature.model.Account;

public class AccountRepository {
	
	private Connection connection;
	
	public AccountRepository() {
		super();
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public Account addAccount(int realtorId, PostAccountDTO accountDTO) throws DatabaseException {
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO accounts (account_type, amount, realtor_id) VALUES (?, ?, ?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, accountDTO.getAccountType());
			pstmt.setDouble(2, accountDTO.getAmount());
			pstmt.setInt(3, realtorId);
			
			int recordsAdded = pstmt.executeUpdate();
			
			if (recordsAdded != 1) {
				throw new DatabaseException("Couldn't add an account to the database");
			}
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				Account newAccount = new Account(id, accountDTO.getAccountType(), accountDTO.getAmount());
				return newAccount;
			} else {
				throw new DatabaseException("Account id was not generated, and therefore adding an account failed");
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with the database. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
	public List<Account> getAccounts(int realtorId) throws DatabaseException {
		Account account = null;
		List<Account> accounts = new ArrayList<>();
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE realtor_id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setInt(1, realtorId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String retrievedAccountType = rs.getString("account_type");
				double retrievedAmount = rs.getDouble("amount");
				account = new Account(id, retrievedAccountType, retrievedAmount);
				
				accounts.add(account);
			}
			
			return accounts;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with the database. "
					+ "Exception message: " + e.getMessage());
		}
		
	}
	
	public List<Account> getAccountsSpecial(int realtorId, double lessThan, double greaterThan) throws DatabaseException {
		Account account = null;
		List<Account> accounts = new ArrayList<>();
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE realtor_id = ? AND amount <= ? AND amount >= ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setInt(1, realtorId);
			pstmt.setDouble(2, lessThan);
			pstmt.setDouble(3, greaterThan);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String retrievedAccountType = rs.getString("account_type");
				double retrievedAmount = rs.getDouble("amount");
				account = new Account(id, retrievedAccountType, retrievedAmount);
				
				accounts.add(account);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong with the database. "
					+ "Exception message: " + e.getMessage());
		}
		
		return accounts;
		
	}
	
	public Account getAccountById(int realtorId, int accountId) throws DatabaseException {
		Account account = null;
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE realtor_id = ? AND id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setInt(1, realtorId);
			pstmt.setInt(2, accountId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int id = rs.getInt("id");
				String retrievedAccountType = rs.getString("account_type");
				double retrievedAmount = rs.getDouble("amount");
				account = new Account(id, retrievedAccountType, retrievedAmount);
			}
			
			return account;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
	public Account updateAccount(int realtorId, int accountId, PostAccountDTO accountDTO) throws BadParameterException, DatabaseException, NotRealtorsAccountException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE accounts SET account_type = ?, amount = ? WHERE realtor_id = ? AND id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, accountDTO.getAccountType());
			pstmt.setDouble(2, accountDTO.getAmount());
			pstmt.setInt(3, realtorId);
			pstmt.setInt(4, accountId);
			
			int recordsUpdated = pstmt.executeUpdate();
			
			if (recordsUpdated != 1) {
				throw new NotRealtorsAccountException("This account does not belong to that realtor");
			}
			
				Account newAccount = new Account(accountId, accountDTO.getAccountType(), accountDTO.getAmount());
				return newAccount;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
	public void deleteAccount(int realtorId, int accountId) throws BadParameterException, DatabaseException, NotRealtorsAccountException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM accounts WHERE realtor_id = ? AND id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, realtorId);
			pstmt.setInt(2, accountId);
			
			int recordsDeleted = pstmt.executeUpdate();
			
			if (recordsDeleted != 1) {
				throw new NotRealtorsAccountException("This account does not belong to that realtor");
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
}
