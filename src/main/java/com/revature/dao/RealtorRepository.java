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
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;

public class RealtorRepository {
	
	public Realtor addRealtor(PostRealtorDTO realtorDTO) throws DatabaseException, AddRealtorException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO realtors (first_name, last_name) VALUES (?, ?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, realtorDTO.getFirstName());
			pstmt.setString(2, realtorDTO.getLastName());
			
			int recordsAdded = pstmt.executeUpdate();
			
			if (recordsAdded != 1) {
				throw new DatabaseException("Couldn't add a realtor to the database");
			}
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				Realtor newRealtor = new Realtor(id, realtorDTO.getFirstName(), realtorDTO.getLastName());
				newRealtor.setAccounts(new ArrayList<>());
				return newRealtor;
			} else {
				throw new DatabaseException("Realtor id was not generated, and therefore adding a realtor failed");
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}

	public Realtor getRealtorByName(String firstName, String lastName) throws DatabaseException {
		Realtor realtor = null;
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM realtors r WHERE r.first_name = ? AND r.last_name = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int id = rs.getInt("id");
				String retrievedFirstName = rs.getString("first_name");
				String retrievedLastName = rs.getString("last_name");
				realtor = new Realtor(id, retrievedFirstName, retrievedLastName);
			}
			
			return realtor;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
	public List<Realtor> getRealtors() throws DatabaseException {
		Realtor realtor = null;
		List<Realtor> realtors = new ArrayList<>();
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM realtors";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String retrievedFirstName = rs.getString("first_name");
				String retrievedLastName = rs.getString("last_name");
				realtor = new Realtor(id, retrievedFirstName, retrievedLastName);
				
				realtors.add(realtor);
			}
			
			return realtors;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
	public Realtor getRealtorById(int realtorId) throws DatabaseException {
		Realtor realtor = null;
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM realtors r WHERE r.id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setInt(1, realtorId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int id = rs.getInt("id");
				String retrievedFirstName = rs.getString("first_name");
				String retrievedLastName = rs.getString("last_name");
				realtor = new Realtor(id, retrievedFirstName, retrievedLastName);
			}
			
			return realtor;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
	public Realtor updateRealtor(int realtorId, PostRealtorDTO realtorDTO) throws BadParameterException, DatabaseException, RealtorNotFoundException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE realtors r SET first_name = ?, last_name = ? WHERE r.id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, realtorDTO.getFirstName());
			pstmt.setString(2, realtorDTO.getLastName());
			pstmt.setInt(3, realtorId);
			
			int recordsUpdated = pstmt.executeUpdate();
			
			if (recordsUpdated != 1) {
				throw new RealtorNotFoundException("Couldn't find that realtor in the database");
			}
			
				Realtor newRealtor = new Realtor(realtorId, realtorDTO.getFirstName(), realtorDTO.getLastName());
				return newRealtor;
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
	public void deleteRealtor(int realtorId) throws BadParameterException, DatabaseException, RealtorNotFoundException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM realtors WHERE id = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, realtorId);
			
			int recordsDeleted = pstmt.executeUpdate();
			
			if (recordsDeleted != 1) {
				throw new RealtorNotFoundException("Couldn't find that realtor in the database");
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Something went wrong when trying to get a connection. "
					+ "Exception message: " + e.getMessage());
		}
	}
	
}
