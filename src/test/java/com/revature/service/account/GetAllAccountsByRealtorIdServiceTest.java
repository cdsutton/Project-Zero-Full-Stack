package com.revature.service.account;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.revature.dao.AccountRepository;
import com.revature.dto.PostAccountDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Account;
import com.revature.service.AccountService;
import com.revature.util.ConnectionUtil;

public class GetAllAccountsByRealtorIdServiceTest {
	
	private static AccountRepository mockAccountRepository;
	private static Connection mockConnection;
	
	private AccountService accountService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException {
		mockAccountRepository = mock(AccountRepository.class);
		mockConnection = mock(Connection.class);
		
		when(mockAccountRepository.addAccount(1, new PostAccountDTO("Checking", 1000)))
				.thenReturn(new Account(1, "Checking", 1000));
		
		List<Account> accounts = new ArrayList<Account>(2);
		accounts.add(new Account(1, "Checking", 1000));
		accounts.add(new Account(1, "Savings", 2000));
		when(mockAccountRepository.getAllAccountsByRealtorId(1)).thenReturn(accounts);
	}
	
	@Before
	public void beforeTest() {
		accountService = new AccountService(mockAccountRepository);
	}
	
	@Test
	public void getAllAccountsByRealtorId_happyPath() throws AccountNotFoundException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			List<Account> expected = new ArrayList<Account>(2);
			expected.add(new Account(1, "Checking", 1000));
			expected.add(new Account(1, "Savings", 2000));

			List<Account> actual = accountService.getAllAccountsByRealtorId("1");

			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void getAllAccountsByRealtorId_realtorNotFound()
			throws AccountNotFoundException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			String stringId = "2";

			try {
				accountService.getAllAccountsByRealtorId(stringId);
				fail("RealtorNotFoundException was not thrown.");
			} catch (RealtorNotFoundException e) {
				assertEquals("a realtor with an Id of " + stringId + " was not found.", e.getMessage());
			}
		}
	}
	
}
