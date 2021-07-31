package com.revature.controller;

import java.util.List;

import com.revature.dto.PostRealtorDTO;
import com.revature.model.Realtor;
import com.revature.service.RealtorService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class RealtorController implements Controller {
	
	private RealtorService realtorService;
	
	public static final String REALTOR_ID = "realtorid";
	public static final String REALTOR_URL = "/realtors";
	public static final String REALTOR_ID_URL = "/realtors/:realtorid";
	
	public RealtorController() {
		this.realtorService = new RealtorService();
	}
	
	private Handler addRealtor = ctx -> {
		PostRealtorDTO realtorDTO = ctx.bodyAsClass(PostRealtorDTO.class);
		
		Realtor realtor = this.realtorService.addRealtor(realtorDTO);
		
		ctx.json(realtor);
		ctx.status(201);
	};
	
	private Handler getAllRealtors = ctx -> {
		List<Realtor> realtor = this.realtorService.getAllRealtors();
		
		ctx.json(realtor);
		ctx.status(200);
	};
	
	private Handler getRealtorById = ctx -> {
		String realtorId = ctx.pathParam(REALTOR_ID);
		
		Realtor realtor = this.realtorService.getRealtorById(realtorId);
		
		ctx.json(realtor);
		ctx.status(200);
	};
	
	private Handler updateRealtor = ctx -> {
		String realtorId = ctx.pathParam(REALTOR_ID);
		PostRealtorDTO realtorDTO = ctx.bodyAsClass(PostRealtorDTO.class);
		
		Realtor realtor = this.realtorService.updateRealtor(realtorId, realtorDTO);
		
		ctx.json(realtor);
		ctx.status(200);
	};
	
	private Handler deleteRealtor = ctx -> {
		String realtorId = ctx.pathParam(REALTOR_ID);
		
		this.realtorService.deleteRealtor(realtorId);
		
		ctx.status(200);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post(REALTOR_URL, addRealtor);
		app.get(REALTOR_URL, getAllRealtors);
		app.get(REALTOR_ID_URL, getRealtorById);
		app.put(REALTOR_ID_URL, updateRealtor);
		app.delete(REALTOR_ID_URL, deleteRealtor);
	}
	
}