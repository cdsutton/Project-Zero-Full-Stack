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
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;
import com.revature.service.RealtorService;
import com.revature.util.ConnectionUtil;

public class GetAllRealtorsServiceTest {
	
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
	}

	@Before
	public void beforeTest() {
		realtorService = new RealtorService(mockRealtorRepository);
	}
	
	@Test
	public void getAllRealtors_happyPath() throws DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			List<Realtor> expected = new ArrayList<Realtor>(2);
			expected.add(new Realtor(1, "Ivan", "Whisky"));
			expected.add(new Realtor(2, "Cory", "Button"));

			List<Realtor> actual = realtorService.getAllRealtors();

			assertEquals(expected, actual);
		}
	}

//	@Test
//	public void getAllRealtors_noRealtors() throws BadParameterException, DatabaseException, RealtorNotFoundException {
//
//		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
//			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
//			
//			List<Realtor> expected = new ArrayList<Realtor>();
//			when(mockRealtorRepository.getAllRealtors()).thenReturn(expected);
//			
//			try {
//				List<Realtor> actual = realtorService.getAllRealtors();
//				assertEquals(expected, actual);
//				fail("RealtorNotFoundException was not thrown.");
//			} catch (RealtorNotFoundException e) {
//				assertEquals("there are no realtors in the database.", e.getMessage());
//			}
//		}
//	}
}
