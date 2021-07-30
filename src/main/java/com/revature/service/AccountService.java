package com.revature.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.revature.util.ConnectionUtil;
import com.revature.dao.AccountRepository;
import com.revature.dto.PostAccountDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.AddAccountException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotRealtorsAccountException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Account;

public class AccountService {
	
	private AccountRepository accountRepository;
	
	String accountWithIdOf = "Account with Id of ";
	String and = " and ";
	String realtorIdMustBeAnIntUserProvided = "Realtor Id must be an int. User provided ";
	String realtorIdAndAccountIdMustBeAnIntUserProvided = "Realtor Id and Account Id must be an int. User provided ";
	String realtorWithIdOf = "Realtor with Id of ";
	String wasNotFound = " was not found.";
	
	public AccountService() {
		this.accountRepository = new AccountRepository();
	}
	
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public Account addAccount(String stringId, PostAccountDTO accountDTO) throws BadParameterException, DatabaseException, AddAccountException {
		try {
			Connection connection = ConnectionUtil.getConnection();
			this.accountRepository.setConnection(connection);
			connection.setAutoCommit(false);
			
			if (accountDTO.getAccountType().trim().equals("")) {
				throw new AddAccountException("Account types cannot be blank");
			}
			
			try {
				int realtorId = Integer.parseInt(stringId);
				
				Account account = accountRepository.addAccount(realtorId, accountDTO);
				
				connection.commit();
				return account;
			} catch (NumberFormatException e) {
				throw new BadParameterException(realtorIdMustBeAnIntUserProvided + stringId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}

	}
	
	public List<Account> getAllAccounts(String stringId) throws AccountNotFoundException, DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			
			List<Account> accounts = accountRepository.getAllAccounts(id);	
			
			if (stringId == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id + wasNotFound);
			}
			
			if (accounts == null) {
				throw new AccountNotFoundException("There are no accounts in the database");
			}
			
			return accountRepository.getAllAccounts(id);
		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdMustBeAnIntUserProvided + stringId);
		}
	}
	
	public List<Account> getSelectAccounts(String stringId, String aLT, String aGT) throws AccountNotFoundException, DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			double lessThan = Double.parseDouble(aLT);
			double greaterThan = Double.parseDouble(aGT);
			
			List<Account> accounts = accountRepository.getSelectAccounts(id, lessThan, greaterThan);	
			
			if (stringId == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id + wasNotFound);
			}
			
			if (accounts == null) {
				throw new AccountNotFoundException("There are no accounts in the database");
			}
			
			return accounts;
			
		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdMustBeAnIntUserProvided + stringId);
		}
	}
	
	public Account getAccountById(String stringId1, String stringId2) throws DatabaseException, BadParameterException, AccountNotFoundException, RealtorNotFoundException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			Account account = accountRepository.getAccountById(id1, id2);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id1 + wasNotFound);
			}
			
			if (account == null) {
				throw new AccountNotFoundException(accountWithIdOf + id2 + wasNotFound);
			}
			
			return account;
		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdAndAccountIdMustBeAnIntUserProvided + stringId1 + and + stringId2);
		}
	
	}
	
	public Account updateAccount(String stringId1, String stringId2, PostAccountDTO accountDTO) throws DatabaseException, BadParameterException, RealtorNotFoundException, AccountNotFoundException, NotRealtorsAccountException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			Account account = accountRepository.updateAccount(id1, id2, accountDTO);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id1 + wasNotFound);
			}
			
			if (stringId2 == null) {
				throw new AccountNotFoundException(accountWithIdOf + id2 + wasNotFound);
			}
			
			return account;
		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdAndAccountIdMustBeAnIntUserProvided + stringId1 + and + stringId2);
		}
	
	}
	
	public void deleteAccount(String stringId1, String stringId2) throws DatabaseException, BadParameterException, RealtorNotFoundException, AccountNotFoundException, NotRealtorsAccountException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			accountRepository.deleteAccount(id1, id2);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id1 + wasNotFound);
			}
			
			if (stringId2 == null) {
				throw new AccountNotFoundException(accountWithIdOf + id2 + wasNotFound);
			}

		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdAndAccountIdMustBeAnIntUserProvided + stringId1 + and + stringId2);
		}
	
	}
	
}
