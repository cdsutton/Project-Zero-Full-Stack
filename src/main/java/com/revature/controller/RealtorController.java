package com.revature.controller;

import java.util.List;

import com.revature.dto.PostRealtorDTO;
import com.revature.model.Realtor;
import com.revature.service.RealtorService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class RealtorController implements Controller {
	
private RealtorService realtorService;
	
	public RealtorController() {
		this.realtorService = new RealtorService();
	}
	
	private Handler addRealtor = ctx -> {
		PostRealtorDTO realtorDTO = ctx.bodyAsClass(PostRealtorDTO.class);
		
		Realtor realtor = this.realtorService.addRealtor(realtorDTO);
		
		ctx.json(realtor);
		ctx.status(201);
	};
	
	private Handler getRealtors = ctx -> {
		List<Realtor> realtor = this.realtorService.getRealtors();
		
		ctx.json(realtor);
		ctx.status(200);
	};
	
	private Handler getRealtorById = ctx -> {
		String id = ctx.pathParam("realtorid");
		
		Realtor realtor = this.realtorService.getRealtorById(id);
		
		ctx.json(realtor);
		ctx.status(200);
	};
	
	private Handler updateRealtor = ctx -> {
		String id = ctx.pathParam("realtorid");
		PostRealtorDTO realtorDTO = ctx.bodyAsClass(PostRealtorDTO.class);
		
		Realtor realtor = this.realtorService.updateRealtor(id, realtorDTO);
		
		ctx.json(realtor);
		ctx.status(200);
	};
	
	private Handler deleteRealtor = ctx -> {
		String id = ctx.pathParam("realtorid");
		
		this.realtorService.deleteRealtor(id);
		
		ctx.status(200);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post("/realtors", addRealtor);
		app.get("/realtors", getRealtors);
		app.get("/realtors/:realtorid", getRealtorById);
		app.put("/realtors/:realtorid", updateRealtor);
		app.delete("/realtors/:realtorid", deleteRealtor);
	}
	
}
