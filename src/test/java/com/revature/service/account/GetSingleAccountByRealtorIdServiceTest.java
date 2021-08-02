package com.revature.service.account;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.AccountRepository;
import com.revature.dto.PostAccountDTO;
import com.revature.exceptions.DatabaseException;
import com.revature.model.Account;
import com.revature.service.AccountService;

public class GetSingleAccountByRealtorIdServiceTest {

	private static AccountRepository mockAccountRepository;
	private static Connection mockConnection;
	
	private AccountService accountService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException {
		mockAccountRepository = mock(AccountRepository.class);
		mockConnection = mock(Connection.class);
		
		when(mockAccountRepository.addAccount(1, new PostAccountDTO("Checking", 1000)))
				.thenReturn(new Account(1, "Checking", 1000));
	}
	
	@Before
	public void beforeTest() {
		accountService = new AccountService(mockAccountRepository);
	}

	
	
}
