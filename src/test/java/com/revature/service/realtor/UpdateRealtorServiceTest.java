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
import com.revature.dto.PostRealtorDTO;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;
import com.revature.service.RealtorService;
import com.revature.util.ConnectionUtil;

public class UpdateRealtorServiceTest {
	
	private static RealtorRepository mockRealtorRepository;
	private static Connection mockConnection;

	private RealtorService realtorService;

	@BeforeClass
	public static void setUp() throws DatabaseException, AddRealtorException, RealtorNotFoundException {
		mockRealtorRepository = mock(RealtorRepository.class);
		mockConnection = mock(Connection.class);

		when(mockRealtorRepository.updateRealtor(2, new PostRealtorDTO("Coby", "Glutton")))
				.thenReturn(new Realtor(2, "Coby", "Glutton"));
	}

	@Before
	public void beforeTest() {
		realtorService = new RealtorService(mockRealtorRepository);
	}
	
	@Test
	public void updateRealtor_happyPath()
			throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			Realtor expected = new Realtor(2, "Coby", "Glutton");

			Realtor actual = realtorService.updateRealtor("2", new PostRealtorDTO("Coby", "Glutton"));

			assertEquals(expected, actual);
		}
	}

	@Test
	public void updateRealtor_realtorNotFound()
			throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			String stringId = "3";

			try {
				realtorService.updateRealtor(stringId, new PostRealtorDTO("Coby", "Glutton"));
				fail("RealtorNotFoundException was not thrown.");
			} catch (RealtorNotFoundException e) {
				assertEquals("a realtor with an Id of " + stringId + " was not found.", e.getMessage());
			}
		}
	}

	@Test
	public void updateRealtor_badParameter()
			throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			String stringId = "abc";

			try {
				realtorService.updateRealtor(stringId, new PostRealtorDTO("Coby", "Glutton"));
				fail("BadParameterException was not thrown.");
			} catch (BadParameterException e) {
				assertEquals("a realtor's Id must be an integer. The user provided " + stringId, e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_blankName() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("", ""));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_blankSpaceName() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("    ", "    "));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_blankFirstSpace() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("    ", ""));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_blankLastSpace() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("", "    "));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_nonBlankFirst() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("Ivan", ""));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_nonBlankLast() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("", "Whisky"));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_nonBlankFirstSpace() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("Ivan", "    "));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
	
	@Test
	public void updateRealtor_nonBlankLastSpace() throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {

		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

			try {
				realtorService.updateRealtor("2", new PostRealtorDTO("    ", "Whisky"));
				fail("AddRealtorException was not thrown.");
			} catch (AddRealtorException e) {
				assertEquals("the user tried to add a realtor with a blank name.", e.getMessage());
			}
		}
	}
}
