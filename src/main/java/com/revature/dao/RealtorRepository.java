package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.util.ConnectionUtil;
import com.revature.dto.PostRealtorDTO;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;

public class RealtorRepository {
	
	public static final String EXCEPTION_MESSAGE = "Exception message: ";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String DATABASE_EXCEPTION = "something went wrong when trying to get a connection. ";
	
	public Realtor addRealtor(PostRealtorDTO realtorDTO) throws DatabaseException {
		
		Realtor newRealtor = null;
		String sql = "INSERT INTO realtors (first_name, last_name) VALUES (?, ?)";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, realtorDTO.getFirstName());
			pstmt.setString(2, realtorDTO.getLastName());
			
			int recordsAdded = pstmt.executeUpdate();
			
			if (recordsAdded != 1) {
				throw new DatabaseException("The user could not add a realtor to the database.");
			}
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					int id = rs.getInt(1);
					newRealtor = new Realtor(id, realtorDTO.getFirstName(), realtorDTO.getLastName());
					// newRealtor.setAccounts(new ArrayList<>());
				} else {
					throw new DatabaseException("a realtor's Id was not generated, and therefore adding a realtor to the database failed.");
				}
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(DATABASE_EXCEPTION + EXCEPTION_MESSAGE + e.getMessage());
		}
		
		return newRealtor;
		
	}
	
	public List<Realtor> getAllRealtors() throws DatabaseException {
		
		List<Realtor> allRealtors = new ArrayList<>();
		Realtor retrievedRealtor = null;
		String sql = "SELECT * FROM realtors";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					int id = rs.getInt("id");
					String retrievedFirstName = rs.getString(FIRST_NAME);
					String retrievedLastName = rs.getString(LAST_NAME);
					retrievedRealtor = new Realtor(id, retrievedFirstName, retrievedLastName);
					allRealtors.add(retrievedRealtor);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(DATABASE_EXCEPTION + EXCEPTION_MESSAGE + e.getMessage());
		}
		
		return allRealtors;
		
	}
	
	public Realtor getRealtorById(int realtorId) throws DatabaseException {
		
		Realtor retrievedRealtor = null;
		String sql = "SELECT * FROM realtors r WHERE r.id = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, realtorId);
			
			try (ResultSet rs = pstmt.executeQuery()) {	
				if (rs.next()) {
					int id = rs.getInt("id");
					String retrievedFirstName = rs.getString(FIRST_NAME);
					String retrievedLastName = rs.getString(LAST_NAME);
					retrievedRealtor = new Realtor(id, retrievedFirstName, retrievedLastName);
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(DATABASE_EXCEPTION + EXCEPTION_MESSAGE + e.getMessage());
		}
		
		return retrievedRealtor;
		
	}
	
//	public Realtor getRealtorByName(String firstName, String lastName) throws DatabaseException {
//		
//		Realtor retrievedRealtor = null;
//		String sql = "SELECT * FROM realtors r WHERE r.first_name = ? AND r.last_name = ?";
//		
//		try (Connection connection = ConnectionUtil.getConnection();
//		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
//			pstmt.setString(1, firstName);
//			pstmt.setString(2, lastName);
//			
//			try (ResultSet rs = pstmt.executeQuery()) {
//				if (rs.next()) {
//					int id = rs.getInt("id");
//					String retrievedFirstName = rs.getString(FIRST_NAME);
//					String retrievedLastName = rs.getString(LAST_NAME);
//					retrievedRealtor = new Realtor(id, retrievedFirstName, retrievedLastName);
//				}
//			}
//		} catch (SQLException e) {
//			throw new DatabaseException(DATABASE_EXCEPTION + EXCEPTION_MESSAGE + e.getMessage());
//		}
//		
//		return retrievedRealtor;
//		
//	}
	
	public Realtor updateRealtor(int realtorId, PostRealtorDTO realtorDTO) throws DatabaseException, RealtorNotFoundException {
		
		Realtor updatedRealtor = null;
		String sql = "UPDATE realtors r SET first_name = ?, last_name = ? WHERE r.id = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, realtorDTO.getFirstName());
			pstmt.setString(2, realtorDTO.getLastName());
			pstmt.setInt(3, realtorId);
			
			int recordsUpdated = pstmt.executeUpdate();
			
			if (recordsUpdated != 1) {
				throw new RealtorNotFoundException("Couldn't find that realtor in the database");
			}
			
			updatedRealtor = new Realtor(realtorId, realtorDTO.getFirstName(), realtorDTO.getLastName());
			
		} catch (SQLException e) {
			throw new DatabaseException(DATABASE_EXCEPTION + EXCEPTION_MESSAGE + e.getMessage());
		}
		
		return updatedRealtor;
		
	}
	
	public void deleteRealtor(int realtorId) throws DatabaseException, RealtorNotFoundException {
		
		String sql = "DELETE FROM realtors WHERE id = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, realtorId);
			
			int recordsDeleted = pstmt.executeUpdate();
			
			if (recordsDeleted != 1) {
				throw new RealtorNotFoundException("Couldn't find that realtor in the database");
			}
		} catch (SQLException e) {
			throw new DatabaseException(DATABASE_EXCEPTION + EXCEPTION_MESSAGE + e.getMessage());
		}
	}
}
