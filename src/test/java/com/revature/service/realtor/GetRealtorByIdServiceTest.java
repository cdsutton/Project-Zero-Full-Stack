package com.revature.service.realtor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.revature.dao.RealtorRepository;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;
import com.revature.service.RealtorService;
import com.revature.util.ConnectionUtil;

public class GetRealtorByIdServiceTest {
	
	private static RealtorRepository mockRealtorRepository;
	private static Connection mockConnection;

	private RealtorService realtorService;

	@BeforeClass
	public static void setUp() throws DatabaseException, AddRealtorException, RealtorNotFoundException {
		mockRealtorRepository = mock(RealtorRepository.class);
		mockConnection = mock(Connection.class);

		when(mockRealtorRepository.getRealtorById(1)).thenReturn(new Realtor(1, "Ivan", "Whisky"));
	}

	@Before
	public void beforeTest() {
		realtorService = new RealtorService(mockRealtorRepository);
	}
	
	@Test
	public void getRealtorById_happyPath() throws BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			Realtor expected = new Realtor(1, "Ivan", "Whisky");

			Realtor actual = realtorService.getRealtorById("1");

			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void getRealtorById_realtorNotFound()
			throws BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			String stringId = "3";

			try {
				realtorService.getRealtorById(stringId);
				fail("RealtorNotFoundException was not thrown.");
			} catch (RealtorNotFoundException e) {
				assertEquals("a realtor with an Id of " + stringId + " was not found.", e.getMessage());
			}
		}
	}
	
	@Test
	public void getRealtorById_badParameter()
			throws BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			String stringId = "abc";

			try {
				realtorService.getRealtorById(stringId);
				fail("BadParameterException was not thrown.");
			} catch (BadParameterException e) {
				assertEquals("a realtor's Id must be an integer. The user provided " + stringId, e.getMessage());
			}
		}
	}
}
