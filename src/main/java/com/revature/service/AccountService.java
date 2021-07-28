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
				throw new BadParameterException("Realtor id must be an int. User provided " + stringId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}

	}
	
	public List<Account> getAccounts(String stringId) throws AccountNotFoundException, DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			
			List<Account> accounts = accountRepository.getAccounts(id);	
			
			if (stringId == null) {
				throw new RealtorNotFoundException("Realtor with id of " + id + " was not found");
			}
			
			if (accounts == null) {
				throw new AccountNotFoundException("There are no accounts in the database");
			}
			
			return accountRepository.getAccounts(id);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Realtor id must be an int. User provided " + stringId);
		}
	}
	
	public List<Account> getAccountsSpecial(String stringId, String aLT, String aGT) throws AccountNotFoundException, DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			double lessThan = Double.parseDouble(aLT);
			double greaterThan = Double.parseDouble(aGT);
			
			List<Account> accounts = accountRepository.getAccountsSpecial(id, lessThan, greaterThan);	
			
			if (stringId == null) {
				throw new RealtorNotFoundException("Realtor with id of " + id + " was not found");
			}
			
			if (accounts == null) {
				throw new AccountNotFoundException("There are no accounts in the database");
			}
			
			return accounts;
			
		} catch (NumberFormatException e) {
			throw new BadParameterException("Realtor id must be an int. User provided " + stringId);
		}
	}
	
	public Account getAccountById(String stringId1, String stringId2) throws DatabaseException, BadParameterException, AccountNotFoundException, RealtorNotFoundException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			Account account = accountRepository.getAccountById(id1, id2);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException("Realtor with id of " + id1 + " was not found");
			}
			
			if (account == null) {
				throw new AccountNotFoundException("Account with id of " + id2 + " was not found");
			}
			
			return account;
		} catch (NumberFormatException e) {
			throw new BadParameterException("Realtor id and Account id must be an int. User provided " + stringId1 + " and " + stringId2);
		}
	
	}
	
	public Account updateAccount(String stringId1, String stringId2, PostAccountDTO accountDTO) throws DatabaseException, BadParameterException, RealtorNotFoundException, AccountNotFoundException, NotRealtorsAccountException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			Account account = accountRepository.updateAccount(id1, id2, accountDTO);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException("Realtor with id of " + id1 + " was not found");
			}
			
			if (stringId2 == null) {
				throw new AccountNotFoundException("Account with id of " + id2 + " was not found");
			}
			
			return account;
		} catch (NumberFormatException e) {
			throw new BadParameterException("Realtor id and Account id must be an int. User provided " + stringId1 + " and " + stringId2);
		}
	
	}
	
	public void deleteAccount(String stringId1, String stringId2) throws DatabaseException, BadParameterException, RealtorNotFoundException, AccountNotFoundException, NotRealtorsAccountException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			accountRepository.deleteAccount(id1, id2);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException("Realtor with id of " + id1 + " was not found");
			}
			
			if (stringId2 == null) {
				throw new AccountNotFoundException("Account with id of " + id2 + " was not found");
			}

		} catch (NumberFormatException e) {
			throw new BadParameterException("Realtor id and Account id must be an int. User provided " + stringId1 + " and " + stringId2);
		}
	
	}
	
}
