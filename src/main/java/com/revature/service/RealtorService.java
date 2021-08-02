package com.revature.service;

import java.util.ArrayList;
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
	
	public static final String BAD_PARAMETER = "a realtor's Id must be an integer. The user provided ";
	public static final String REALTOR_ID = "a realtor with an Id of ";
	public static final String NOT_FOUND = " was not found.";
	
	public RealtorService() {
		this.realtorRepository = new RealtorRepository();
	}
	
	public RealtorService(RealtorRepository realtorRepository) {
		this.realtorRepository = realtorRepository;
	}
	
	public Realtor addRealtor(PostRealtorDTO realtorDTO) throws AddRealtorException, DatabaseException {
//		if (realtorRepository.getRealtorByName(realtorDTO.getFirstName(), realtorDTO.getLastName()) != null) {
//			throw new AddRealtorException("the user tried to add a realtor that already exists with that name.");
//		}
		
		if (realtorDTO.getFirstName().trim().equals("") || realtorDTO.getLastName().trim().equals("")) {
			throw new AddRealtorException("the user tried to add a realtor with a blank name.");
		} else {
			return realtorRepository.addRealtor(realtorDTO);
		}
	}
	
	public List<Realtor> getAllRealtors() throws DatabaseException, RealtorNotFoundException {
	
		List<Realtor> realtors = realtorRepository.getAllRealtors();
			
		if (realtors == new ArrayList<Realtor>()) {
			throw new RealtorNotFoundException("there are no realtors in the database.");
		} else {
			return realtorRepository.getAllRealtors();
		}
	}
	
	public Realtor getRealtorById(String stringId) throws BadParameterException, DatabaseException, RealtorNotFoundException {
		
		try {
			int intId = Integer.parseInt(stringId);
			
			if (realtorRepository.getRealtorById(intId) == null) {
				throw new RealtorNotFoundException(REALTOR_ID + intId + NOT_FOUND);
			} else {
				return realtorRepository.getRealtorById(intId);
			}

		} catch (NumberFormatException e) {
			throw new BadParameterException(BAD_PARAMETER + stringId);
		}
	}
	
	public Realtor updateRealtor(String stringId, PostRealtorDTO realtorDTO) throws AddRealtorException, BadParameterException, DatabaseException, RealtorNotFoundException {
		
		try {
			int intId = Integer.parseInt(stringId);
			
			if (realtorRepository.getRealtorById(intId) == null) {
				throw new RealtorNotFoundException(REALTOR_ID + intId + NOT_FOUND);
			} else if (realtorDTO.getFirstName().trim().equals("") || realtorDTO.getLastName().trim().equals("")) {
				throw new AddRealtorException("the user tried to add a realtor with a blank name.");
			} else {
				return realtorRepository.updateRealtor(intId, realtorDTO);
			}
			
		} catch (NumberFormatException e) {
			throw new BadParameterException(BAD_PARAMETER + stringId);
		}
	}
	
	public void deleteRealtor(String stringId) throws BadParameterException, DatabaseException, RealtorNotFoundException {
		
		try {
			int intId = Integer.parseInt(stringId);
			
			if (realtorRepository.getRealtorById(intId) == null) {
				throw new RealtorNotFoundException(REALTOR_ID + intId + NOT_FOUND);
			} else {
				realtorRepository.deleteRealtor(intId);
			}

		} catch (NumberFormatException e) {
			throw new BadParameterException(BAD_PARAMETER + stringId);
		}
	}
}
