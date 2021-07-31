package com.revature.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
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

import com.revature.util.ConnectionUtil;
import com.revature.dao.RealtorRepository;
import com.revature.dto.PostRealtorDTO;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;

public class RealtorServiceTest {
	
	private static RealtorRepository mockRealtorRepository;
	private static Connection mockConnection;
	
	private RealtorService realtorService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException, AddRealtorException {
		mockRealtorRepository = mock(RealtorRepository.class);
		mockConnection = mock(Connection.class);
		
		when(mockRealtorRepository.addRealtor(new PostRealtorDTO("Ivan", "Whisky")))
		.thenReturn(new Realtor(1, "Ivan", "Whisky"));
	}
	
	@Before
	public void beforeTest() {
		realtorService = new RealtorService(mockRealtorRepository);
	}
	
	/* Testing addRealtor Service */
	
	@Test
	public void addRealtor_happyPath() throws BadParameterException, DatabaseException, AddRealtorException {

		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			Realtor actual = realtorService.addRealtor(new PostRealtorDTO("Ivan", "Whisky"));
			
			Realtor expected = new Realtor(1, "Ivan", "Whisky");
			
			assertEquals(expected, actual);
		}
		
	}
	
	@Test
	public void test_blankName() throws BadParameterException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("", ""));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	@Test
	public void test_blankSpaceName() throws BadParameterException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("    ", "    "));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	@Test
	public void test_blankLastSpace() throws BadParameterException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("", "    "));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	@Test
	public void test_blankFirstSpace() throws BadParameterException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("    ", ""));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	@Test
	public void test_nonBlankFirst() throws BadParameterException, DatabaseException {

		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("Ivan", ""));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	@Test
	public void test_nonBlankFirstSpace() throws BadParameterException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("Ivan", "    "));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	@Test
	public void test_nonBlankLast() throws BadParameterException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("", "Whisky"));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	@Test
	public void test_nonBlankLastSpace() throws BadParameterException, DatabaseException {
		
		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
			
			try {
				realtorService.addRealtor(new PostRealtorDTO("    ", "Whisky"));
				fail("AddRealtorException was not thrown");
			} catch (AddRealtorException e) {
				assertEquals("User tried to add a realtor with a blank name.", e.getMessage());
			}
			
		}
		
	}
	
	/* Testing getRealtors Service */
	
//	@Test
//	public void getAllRealtors_happyPath() throws BadParameterException, DatabaseException, AddRealtorException, RealtorNotFoundException {
//
//		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
//			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
//			
//			List<Realtor> expected = new ArrayList<Realtor>();
//			expected.add(new Realtor(1, "Ivan", "Whisky"));
//			
//			List<Realtor> actual = realtorService.getAllRealtors();
//			
//			assertEquals(expected, actual);
//		}
//		
//	}
	
//	@Test(expected = RealtorNotFoundException.class)
//	public void getAllRealtors_null() throws BadParameterException, DatabaseException, AddRealtorException, RealtorNotFoundException {
//
//		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
//			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
//			
//			
////			assertThrows(RealtorNotFoundException.class, runnable);
//			try {
//				realtorService.getAllRealtors();
//				fail("RealtorNotFoundException was not thrown.");
//			} catch (RealtorNotFoundException e) {
//				//assertThrows(RealtorNotFoundException, "There are no realtors in the database.", e.getMessage());
//			}
//		}
//	}
	
	/* Testing getRealtorById Service */
	
//	@Test
//	public void getRealtorById_validIdOf1() throws BadParameterException, DatabaseException, RealtorNotFoundException {
//
//		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
//			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
//			
//			Realtor actual = realtorService.getRealtorById("1");
//			
//			Realtor expected = new Realtor(1, "Ivan", "Whisky");
//			
//			assertEquals(expected, actual);
//		}
//		
//	}
//	
//	/* Testing updateRealtor Service */
//	
//	@Test
//	public void updateRealtor_happyPath() throws BadParameterException, DatabaseException, AddRealtorException {
//
////		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
////			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
////			
////			Realtor actual = realtorService.addRealtor(new PostRealtorDTO("Joe", "Shimamura"));
////			
////			Realtor expected = new Realtor(1, "Joe", "Shimamura");
////			
////			assertEquals(expected, actual);
////		}
//		
//	}
//	
//	/* Testing deleteRealtor Service */
//	
//	@Test
//	public void deleteRealtor_happyPath() throws BadParameterException, DatabaseException, AddRealtorException {
//
////		try(MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
////			mockedConnectionUtil.when(ConnectionUtil::getConnection).thenReturn(mockConnection);
////			
////			Realtor actual = realtorService.addRealtor(new PostRealtorDTO("Joe", "Shimamura"));
////			
////			Realtor expected = new Realtor(1, "Joe", "Shimamura");
////			
////			assertEquals(expected, actual);
////		}
//		
//	}
	
}