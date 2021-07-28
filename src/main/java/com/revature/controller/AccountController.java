package com.revature.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.AccountController;
import com.revature.dto.PostAccountDTO;
import com.revature.model.Account;
import com.revature.service.AccountService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController implements Controller {
	
	private Logger logger = LoggerFactory.getLogger(AccountController.class);
	private AccountService accountService;

	public AccountController() {
		this.accountService = new AccountService();
	}

	private Handler addAccount = ctx -> {
		PostAccountDTO accountDTO = ctx.bodyAsClass(PostAccountDTO.class);
		String realtorId = ctx.pathParam("realtorid");

		Account insertedAccount = this.accountService.addAccount(realtorId, accountDTO);

		ctx.status(201);
		ctx.json(insertedAccount);
	};
	
	private Handler getAccounts = ctx -> {
		String id = ctx.pathParam("realtorid");
		String aLT = ctx.queryParam("amountLessThan");
		String aGT = ctx.queryParam("amountGreaterThan");
		
		List<Account> accountList = new ArrayList<>();
		
		if (aLT == null && aGT == null) {
			accountList = this.accountService.getAccounts(id);
		} else {
			accountList = this.accountService.getAccountsSpecial(id, aLT, aGT);
		}
		
		ctx.json(accountList);
		ctx.status(200);
	};
	
	private Handler getAccountById = ctx -> {
		String id1 = ctx.pathParam("realtorid");
		String id2 = ctx.pathParam("accountid");
		
		Account account = accountService.getAccountById(id1, id2);
		
		ctx.json(account);
		ctx.status(200);
	};
	
	private Handler updateAccount = ctx -> {
		String id1 = ctx.pathParam("realtorid");
		String id2 = ctx.pathParam("accountid");
		PostAccountDTO accountDTO = ctx.bodyAsClass(PostAccountDTO.class);
		
		Account account = this.accountService.updateAccount(id1, id2, accountDTO);
		
		ctx.json(account);
		ctx.status(200);
	};
	
	private Handler deleteAccount = ctx -> {
		String id1 = ctx.pathParam("realtorid");
		String id2 = ctx.pathParam("accountid");
		
		this.accountService.deleteAccount(id1, id2);
		
		ctx.status(200);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post("/realtors/:realtorid/accounts", addAccount);
		app.get("/realtors/:realtorid/accounts", getAccounts);
		app.get("/realtors/:realtorid/accounts/:accountid", getAccountById);
		app.put("/realtors/:realtorid/accounts/:accountid", updateAccount);
		app.delete("/realtors/:realtorid/accounts/:accountid", deleteAccount);
	}
	
}
