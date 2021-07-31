package com.revature.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.revature.util.ConnectionUtil;
import com.revature.dao.AccountRepository;
import com.revature.dto.PostAccountDTO;
import com.revature.exceptions.AddAccountException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.model.Account;

public class AccountServiceTest {
	
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
	
	@Test
	public void test_happyPath() throws BadParameterException, DatabaseException, AddAccountException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			Account actual = accountService.addAccount("1", new PostAccountDTO("Checking", 1000));
			
			Account expected = new Account(1, "Checking", 1000);
			
			assertEquals(expected, actual);
		}
		
	}
	
	@Test
	public void test_blankAccountType() throws DatabaseException, BadParameterException {
		PostAccountDTO blankAccountTypeDTO = new PostAccountDTO("", 0);
		try {
			accountService.addAccount("1", blankAccountTypeDTO);
			fail("AddAccountException was not thrown");
		} catch (AddAccountException e) {
			assertEquals("Account types cannot be blank", e.getMessage());
		}
	}
	
	@Test
	public void test_blankAccountTypeSpace() throws DatabaseException, BadParameterException {
		PostAccountDTO blankAccountTypeDTO = new PostAccountDTO("    ", 0);
		try {
			accountService.addAccount("1", blankAccountTypeDTO);
			fail("AddAccountException was not thrown");
		} catch (AddAccountException e) {
			assertEquals("Account types cannot be blank", e.getMessage());
		}
	}
	
}
