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
	
	String realtorIdMustBeAnIntUserProvided = "Realtor id must be an int. User provided ";
	String realtorWithIdOf = "Realtor with id of ";
	String wasNotFound = " was not found.";
	
	public RealtorService() {
		super();
		this.realtorRepository = new RealtorRepository();
	}
	
	public RealtorService(RealtorRepository realtorRepository) {
		this.realtorRepository = realtorRepository;
	}
	
	public Realtor addRealtor(PostRealtorDTO realtorDTO) throws DatabaseException, AddRealtorException {
		if (realtorRepository.getRealtorByName(realtorDTO.getFirstName(), realtorDTO.getLastName()) != null) {
			throw new AddRealtorException("User tried to add a realtor that already exists with that name");
		}
		
		if (realtorDTO.getFirstName().trim().equals("") || realtorDTO.getLastName().trim().equals("")) {
			throw new AddRealtorException("User tried to add a realtor with a blank name");
		}
		
		return realtorRepository.addRealtor(realtorDTO);
	}
	
	public List<Realtor> getRealtors() throws RealtorNotFoundException, DatabaseException {
			List<Realtor> realtors = realtorRepository.getRealtors();
			
			if (realtors == null) {
				throw new RealtorNotFoundException("There are no realtors in the database");
			}
			
			return realtorRepository.getRealtors();
	}
	
	public Realtor getRealtorById(String stringId) throws DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			
			Realtor realtor = realtorRepository.getRealtorById(id);
			
			if (realtor == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id + wasNotFound);
			}
			
			return realtor;
		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdMustBeAnIntUserProvided + stringId);
		}
	
	}
	
	public Realtor updateRealtor(String stringId, PostRealtorDTO realtorDTO) throws DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			
			Realtor realtor = realtorRepository.updateRealtor(id, realtorDTO);
			
			if (realtor == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id + wasNotFound);
			}
			
			return realtor;
		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdMustBeAnIntUserProvided + stringId);
		}
	
	}
	
	public void deleteRealtor(String stringId) throws DatabaseException, BadParameterException, RealtorNotFoundException {
		try {
			int id = Integer.parseInt(stringId);
			
			realtorRepository.deleteRealtor(id);
			
			if (stringId == null) {
				throw new RealtorNotFoundException(realtorWithIdOf + id + wasNotFound);
			}

		} catch (NumberFormatException e) {
			throw new BadParameterException(realtorIdMustBeAnIntUserProvided + stringId);
		}
	
	}
	
}
