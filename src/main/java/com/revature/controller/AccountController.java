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
	
	String constantRealtorId = "realtorid";
	String constantAccountId = "accountid";
	String accountIdUrl = "/realtors/:realtorid/accounts/:accountid";
	String accountUrl = "/realtors/:realtorid/accounts";
	
	public AccountController() {
		this.accountService = new AccountService();
	}

	private Handler addAccount = ctx -> {
		PostAccountDTO accountDTO = ctx.bodyAsClass(PostAccountDTO.class);
		String realtorId = ctx.pathParam(constantRealtorId);

		Account insertedAccount = this.accountService.addAccount(realtorId, accountDTO);

		ctx.status(201);
		ctx.json(insertedAccount);
	};
	
	private Handler getAccounts = ctx -> {
		String realtorId = ctx.pathParam(constantRealtorId);
		String aLT = ctx.queryParam("amountLessThan");
		String aGT = ctx.queryParam("amountGreaterThan");
		
		List<Account> accountList = new ArrayList<>();
		
		if (aLT == null && aGT == null) {
			accountList = this.accountService.getAccounts(realtorId);
		} else {
			accountList = this.accountService.getAccountsSpecial(realtorId, aLT, aGT);
		}
		
		ctx.json(accountList);
		ctx.status(200);
	};
	
	private Handler getAccountById = ctx -> {
		String id1 = ctx.pathParam(constantRealtorId);
		String id2 = ctx.pathParam(constantAccountId);
		
		Account account = accountService.getAccountById(id1, id2);
		
		ctx.json(account);
		ctx.status(200);
	};
	
	private Handler updateAccount = ctx -> {
		String id1 = ctx.pathParam(constantRealtorId);
		String id2 = ctx.pathParam(constantAccountId);
		PostAccountDTO accountDTO = ctx.bodyAsClass(PostAccountDTO.class);
		
		Account account = this.accountService.updateAccount(id1, id2, accountDTO);
		
		ctx.json(account);
		ctx.status(200);
	};
	
	private Handler deleteAccount = ctx -> {
		String id1 = ctx.pathParam(constantRealtorId);
		String id2 = ctx.pathParam(constantAccountId);
		
		this.accountService.deleteAccount(id1, id2);
		
		ctx.status(200);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post(accountUrl, addAccount);
		app.get(accountUrl, getAccounts);
		app.get(accountIdUrl, getAccountById);
		app.put(accountIdUrl, updateAccount);
		app.delete(accountIdUrl, deleteAccount);
	}
	
}
