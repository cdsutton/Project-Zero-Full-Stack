package com.revature.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.revature.model.Realtor;

public class AccountService {
	
	private AccountRepository accountRepository;
	
	public static final String ACCOUNT = "an account with an Id of ";
	public static final String AND = " and ";
	public static final String REALTOR_INTEGER = "a realtor's Id must be an integer. The user provided ";
	public static final String REALTOR_ACCOUNT_INTEGER = "a realtor's Id and account Id must be an integer. The user provided ";
	public static final String REALTOR = "a realtor with an Id of ";
	public static final String NOT_FOUND = " was not found.";
	
	public AccountService() {
		this.accountRepository = new AccountRepository();
	}
	
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public Account addAccount(String stringId, PostAccountDTO accountDTO) throws AddAccountException, BadParameterException, DatabaseException {
		
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
				throw new BadParameterException(REALTOR_INTEGER + stringId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}

	}
	
	public List<Account> getAllAccountsByRealtorId(String stringId) throws AccountNotFoundException, BadParameterException, DatabaseException, RealtorNotFoundException {
		
		try {
			int intId = Integer.parseInt(stringId);
			
			List<Account> accounts = accountRepository.getAllAccountsByRealtorId(intId);
			
			if (accountRepository.getAllAccountsByRealtorId(intId) == null) {
				throw new RealtorNotFoundException(REALTOR + intId + NOT_FOUND);
			} else if (accounts == new ArrayList<Account>()) {
				throw new AccountNotFoundException("There are no accounts in the database");
			} else {
				return accountRepository.getAllAccountsByRealtorId(intId);
			}
			
		} catch (NumberFormatException e) {
			throw new BadParameterException(REALTOR_INTEGER + stringId);
		}
	}
	
	public List<Account> getSelectAccountsByRealtorId(String stringId, String aLT, String aGT) throws AccountNotFoundException, DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			double lessThan = Double.parseDouble(aLT);
			double greaterThan = Double.parseDouble(aGT);
			
			List<Account> accounts = accountRepository.getSelectAccountsByRealtorId(id, lessThan, greaterThan);	
			
			if (stringId == null) {
				throw new RealtorNotFoundException(REALTOR + id + NOT_FOUND);
			}
			
			if (accounts == null) {
				throw new AccountNotFoundException("There are no accounts in the database");
			}
			
			return accounts;
			
		} catch (NumberFormatException e) {
			throw new BadParameterException(REALTOR_INTEGER + stringId);
		}
	}
	
	public Account getSingleAccountByRealtorId(String stringId1, String stringId2) throws DatabaseException, BadParameterException, AccountNotFoundException, RealtorNotFoundException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			Account account = accountRepository.getSingleAccountByRealtorId(id1, id2);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException(REALTOR + id1 + NOT_FOUND);
			}
			
			if (account == null) {
				throw new AccountNotFoundException(ACCOUNT + id2 + NOT_FOUND);
			}
			
			return account;
		} catch (NumberFormatException e) {
			throw new BadParameterException(REALTOR_ACCOUNT_INTEGER + stringId1 + AND + stringId2);
		}
	
	}
	
	public Account updateAccount(String stringId1, String stringId2, PostAccountDTO accountDTO) throws AccountNotFoundException, BadParameterException, DatabaseException, NotRealtorsAccountException, RealtorNotFoundException {
		try {
			int intId1 = Integer.parseInt(stringId1);
			int intId2 = Integer.parseInt(stringId2);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException(REALTOR + intId1 + NOT_FOUND);
			} else if (stringId2 == null) {
				throw new AccountNotFoundException(ACCOUNT + intId2 + NOT_FOUND);
			} else {
				return accountRepository.updateAccount(intId1, intId2, accountDTO);
			}
			
		} catch (NumberFormatException e) {
			throw new BadParameterException(REALTOR_ACCOUNT_INTEGER + stringId1 + AND + stringId2);
		}
	
	}
	
	public void deleteAccount(String stringId1, String stringId2) throws DatabaseException, BadParameterException, RealtorNotFoundException, AccountNotFoundException, NotRealtorsAccountException {
		try {
			int id1 = Integer.parseInt(stringId1);
			int id2 = Integer.parseInt(stringId2);
			
			accountRepository.deleteAccount(id1, id2);
			
			if (stringId1 == null) {
				throw new RealtorNotFoundException(REALTOR + id1 + NOT_FOUND);
			}
			
			if (stringId2 == null) {
				throw new AccountNotFoundException(ACCOUNT + id2 + NOT_FOUND);
			}

		} catch (NumberFormatException e) {
			throw new BadParameterException(REALTOR_ACCOUNT_INTEGER + stringId1 + AND + stringId2);
		}
	
	}
	
}
