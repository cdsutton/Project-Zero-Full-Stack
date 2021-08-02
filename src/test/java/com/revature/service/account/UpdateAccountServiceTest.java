package com.revature.service.account;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.revature.dao.AccountRepository;
import com.revature.dto.PostAccountDTO;
import com.revature.dto.PostRealtorDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotRealtorsAccountException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Account;
import com.revature.model.Realtor;
import com.revature.service.AccountService;
import com.revature.util.ConnectionUtil;

public class UpdateAccountServiceTest {
	
	private static AccountRepository mockAccountRepository;
	private static Connection mockConnection;
	
	private AccountService accountService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException, NotRealtorsAccountException {
		mockAccountRepository = mock(AccountRepository.class);
		mockConnection = mock(Connection.class);
		
		when(mockAccountRepository.updateAccount(1, 1, new PostAccountDTO("Checking", 2000)))
			.thenReturn(new Account(1, "Checking", 2000));
	}
	
	@Before
	public void beforeTest() {
		accountService = new AccountService(mockAccountRepository);
	}
	
	@Test
	public void updateAccount_happyPath()
			throws AccountNotFoundException, AddRealtorException, BadParameterException, DatabaseException, NotRealtorsAccountException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			Account expected = new Account(1, "Checking", 2000);

			Account actual = accountService.updateAccount("1", "1", new PostAccountDTO("Checking", 2000));

			assertEquals(expected, actual);
		}
	}
	
}
