package com.revature.service.realtor;

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

import com.revature.dao.RealtorRepository;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;
import com.revature.service.RealtorService;
import com.revature.util.ConnectionUtil;

public class DeleteRealtorServiceTest {
	
	private static RealtorRepository mockRealtorRepository;
	private static Connection mockConnection;

	private RealtorService realtorService;

	@BeforeClass
	public static void setUp() throws DatabaseException, AddRealtorException, RealtorNotFoundException {
		mockRealtorRepository = mock(RealtorRepository.class);
		mockConnection = mock(Connection.class);

		List<Realtor> realtors = new ArrayList<Realtor>(2);
		realtors.add(new Realtor(1, "Ivan", "Whisky"));
		realtors.add(new Realtor(2, "Cory", "Button"));
		when(mockRealtorRepository.getAllRealtors()).thenReturn(realtors);

		when(mockRealtorRepository.getRealtorById(2)).thenReturn(new Realtor(2, "Cory", "Button"));
	}

	@Before
	public void beforeTest() {
		realtorService = new RealtorService(mockRealtorRepository);
	}

//	@Test
//	public void deleteRealtor_happyPath() throws BadParameterException, DatabaseException, RealtorNotFoundException {
//
//		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
//			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
//			
//			List<Realtor> expected = new ArrayList<Realtor>();
//			expected.add(new Realtor(1, "Ivan", "Whisky"));
//			
//			List<Realtor> actual = realtorService.getAllRealtors();
//			
//			int id = actual.indexOf(actual.get(1)) + 1;
//			
//			String stringId = Integer.toString(id);
//			System.out.println(stringId);
//			
//			realtorService.deleteRealtor(stringId);
//			actual.remove(1);
//			
//			assertEquals(expected, actual);
//		}
//	}

	@Test
	public void deleteRealtor_realtorNotFound()
			throws BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			String stringId = "3";

			try {
				realtorService.deleteRealtor(stringId);
				fail("RealtorNotFoundException was not thrown.");
			} catch (RealtorNotFoundException e) {
				assertEquals("a realtor with an Id of " + stringId + " was not found.", e.getMessage());
			}
		}
	}
	
	@Test
	public void deleteRealtor_badParameter()
			throws BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			String stringId = "abc";

			try {
				realtorService.deleteRealtor(stringId);
				fail("BadParameterException was not thrown.");
			} catch (BadParameterException e) {
				assertEquals("a realtor's Id must be an integer. The user provided " + stringId, e.getMessage());
			}
		}
	}
}
