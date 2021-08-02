package com.revature.controller;

import java.util.ArrayList;
import java.util.List;

import com.revature.controller.AccountController;
import com.revature.dto.PostAccountDTO;
import com.revature.model.Account;
import com.revature.service.AccountService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController implements Controller {
	
	private AccountService accountService;
	
	public static final String REALTOR_ID = "realtorid";
	public static final String ACCOUNT_ID = "accountid";
	public static final String ACCOUNT_ID_URL = "/realtors/:realtorid/accounts/:accountid";
	public static final String ACCOUNT_URL = "/realtors/:realtorid/accounts";
	
	public AccountController() {
		this.accountService = new AccountService();
	}

	private Handler addAccount = ctx -> {
		PostAccountDTO accountDTO = ctx.bodyAsClass(PostAccountDTO.class);
		String realtorId = ctx.pathParam(REALTOR_ID);

		Account insertedAccount = this.accountService.addAccount(realtorId, accountDTO);

		ctx.status(201);
		ctx.json(insertedAccount);
	};
	
	private Handler getAllAccountsByRealtorId = ctx -> {
		String realtorId = ctx.pathParam(REALTOR_ID);
		String aLT = ctx.queryParam("amountLessThan");
		String aGT = ctx.queryParam("amountGreaterThan");
		
		List<Account> accountList = new ArrayList<>();
		
		if (aLT == null && aGT == null) {
			accountList = this.accountService.getAllAccountsByRealtorId(realtorId);
		} else {
			accountList = this.accountService.getSelectAccountsByRealtorId(realtorId, aLT, aGT);
		}
		
		ctx.json(accountList);
		ctx.status(200);
	};
	
	private Handler getSingleAccountByRealtorId = ctx -> {
		String id1 = ctx.pathParam(REALTOR_ID);
		String id2 = ctx.pathParam(ACCOUNT_ID);
		
		Account account = accountService.getSingleAccountByRealtorId(id1, id2);
		
		ctx.json(account);
		ctx.status(200);
	};
	
	private Handler updateAccount = ctx -> {
		String id1 = ctx.pathParam(REALTOR_ID);
		String id2 = ctx.pathParam(ACCOUNT_ID);
		PostAccountDTO accountDTO = ctx.bodyAsClass(PostAccountDTO.class);
		
		Account account = this.accountService.updateAccount(id1, id2, accountDTO);
		
		ctx.json(account);
		ctx.status(200);
	};
	
	private Handler deleteAccount = ctx -> {
		String id1 = ctx.pathParam(REALTOR_ID);
		String id2 = ctx.pathParam(ACCOUNT_ID);
		
		this.accountService.deleteAccount(id1, id2);
		
		ctx.status(200);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post(ACCOUNT_URL, addAccount);
		app.get(ACCOUNT_URL, getAllAccountsByRealtorId);
		app.get(ACCOUNT_ID_URL, getSingleAccountByRealtorId);
		app.put(ACCOUNT_ID_URL, updateAccount);
		app.delete(ACCOUNT_ID_URL, deleteAccount);
	}
	
}
