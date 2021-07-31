package com.revature.service;

import java.util.List;

import com.revature.dao.RealtorRepository;
import com.revature.dto.PostRealtorDTO;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.RealtorNotFoundException;
import com.revature.model.Realtor;

public class RealtorService {
	
	private RealtorRepository realtorRepository;
	
	public static final String BAD_PARAMETER = "Realtor id must be an int. User provided ";
	public static final String REALTOR_ID = "Realtor with id of ";
	public static final String NOT_FOUND = " was not found.";
	
	public RealtorService() {
		this.realtorRepository = new RealtorRepository();
	}
	
	public RealtorService(RealtorRepository realtorRepository) {
		this.realtorRepository = realtorRepository;
	}
	
	public Realtor addRealtor(PostRealtorDTO realtorDTO) throws DatabaseException, AddRealtorException {
		if (realtorRepository.getRealtorByName(realtorDTO.getFirstName(), realtorDTO.getLastName()) != null) {
			throw new AddRealtorException("User tried to add a realtor that already exists with that name.");
		}
		
		if (realtorDTO.getFirstName().trim().equals("") || realtorDTO.getLastName().trim().equals("")) {
			throw new AddRealtorException("User tried to add a realtor with a blank name.");
		}
		
		return realtorRepository.addRealtor(realtorDTO);
	}
	
	public List<Realtor> getAllRealtors() throws RealtorNotFoundException, DatabaseException {
			List<Realtor> realtors = realtorRepository.getAllRealtors();
			
			if (realtors == null) {
				throw new RealtorNotFoundException("There are no realtors in the database.");
			}
			
			return realtorRepository.getAllRealtors();
	}
	
	public Realtor getRealtorById(String stringId) throws DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int realtorId = Integer.parseInt(stringId);
			
			Realtor realtor = realtorRepository.getRealtorById(realtorId);
			
			if (realtor == null) {
				throw new RealtorNotFoundException(REALTOR_ID + realtorId + NOT_FOUND);
			}
			
			return realtor;
		} catch (NumberFormatException e) {
			throw new BadParameterException(BAD_PARAMETER + stringId);
		}
	
	}
	
	public Realtor updateRealtor(String stringId, PostRealtorDTO realtorDTO) throws DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			
			Realtor realtor = realtorRepository.updateRealtor(id, realtorDTO);
			
			if (realtor == null) {
				throw new RealtorNotFoundException(REALTOR_ID + id + NOT_FOUND);
			}
			
			return realtor;
		} catch (NumberFormatException e) {
			throw new BadParameterException(BAD_PARAMETER + stringId);
		}
	
	}
	
	public void deleteRealtor(String stringId) throws DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			
			realtorRepository.deleteRealtor(id);
			
			if (stringId == null) {
				throw new RealtorNotFoundException(REALTOR_ID + id + NOT_FOUND);
			}

		} catch (NumberFormatException e) {
			throw new BadParameterException(BAD_PARAMETER + stringId);
		}
	
	}
	
}
