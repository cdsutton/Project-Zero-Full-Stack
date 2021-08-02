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
import com.revature.dto.PostRealtorDTO;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;
import com.revature.service.RealtorService;
import com.revature.util.ConnectionUtil;

public class AddRealtorServiceTest {
	
	private static RealtorRepository mockRealtorRepository;
	private static Connection mockConnection;
	
	private RealtorService realtorService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException, AddRealtorException, RealtorNotFoundException {
		mockRealtorRepository = mock(RealtorRepository.class);
		mockConnection = mock(Connection.class);
		
		when(mockRealtorRepository.addRealtor(new PostRealtorDTO("Ivan", "Whisky")))
				.thenReturn(new Realtor(1, "Ivan", "Whisky"));
	}
	
	@Before
	public void beforeTest() {
		realtorService = new RealtorService(mockRealtorRepository);
	}
	
	@Test
	public void addRealtor_happyPath() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			Realtor expected = new Realtor(1, "Ivan", "Whisky");

			Realtor actual = realtorService.addRealtor(new PostRealtorDTO("Ivan", "Whisky"));

			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void addRealtor_blankName() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.addRealtor(new PostRealtorDTO("", ""));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void addRealtor_blankSpaceName() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.addRealtor(new PostRealtorDTO("    ", "    "));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void addRealtor_blankFirstSpace() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.addRealtor(new PostRealtorDTO("    ", ""));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void addRealtor_blankLastSpace() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.addRealtor(new PostRealtorDTO("", "    "));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void addRealtor_nonBlankFirst() throws AddRealtorException, DatabaseException {
		
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("Ivan", ""));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void addRealtor_nonBlankLast() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.addRealtor(new PostRealtorDTO("", "Whisky"));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}

	@Test
	public void addRealtor_nonBlankFirstSpace() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.addRealtor(new PostRealtorDTO("Ivan", "    "));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}

	@Test
	public void addRealtor_nonBlankLastSpace() throws AddRealtorException, DatabaseException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.addRealtor(new PostRealtorDTO("    ", "Whisky"));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
}
