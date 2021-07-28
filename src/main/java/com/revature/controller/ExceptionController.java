package com.revature.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.ExceptionController;
import com.revature.dto.MessageDTO;
import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.AddAccountException;
import com.revature.exceptions.AddRealtorException;
import com.revature.exceptions.BadParameterException;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.NotRealtorsAccountException;
import com.revature.exceptions.RealtorNotFoundException;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

public class ExceptionController implements Controller {
	
private Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	private ExceptionHandler<AccountNotFoundException> accountNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("A user tried to retrieve an account, but it was not found. Exception message is \n" + e.getMessage());
		ctx.status(404);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<AddAccountException> addAccountExceptionHandler = (e, ctx) -> {
		logger.warn("Could not add account. Exception message is \n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<AddRealtorException> addRealtorExceptionHandler = (e, ctx) -> {
		logger.warn("Could not add realtor. Exception message is \n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<BadParameterException> badParameterExceptionHandler = (e, ctx) -> {
		logger.warn("A user provided a bad parameter. Exception message is: \n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<DatabaseException> databaseExceptionHandler = (e, ctx) -> {
		logger.error("Could not connect to database. Exception message is \n" + e.getMessage());
		ctx.status(500);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<NotRealtorsAccountException> notRealtorsAccountExceptionHandler = (e, ctx) -> {
		logger.warn("Could not access an account that belongs to another client. Exception message is \n" + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<RealtorNotFoundException> realtorNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("A user tried to retrieve a realtor, but it was not found. Exception message is \n" + e.getMessage());
		ctx.status(404);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.exception(AccountNotFoundException.class, accountNotFoundExceptionHandler);
		app.exception(AddAccountException.class, addAccountExceptionHandler);
		app.exception(AddRealtorException.class, addRealtorExceptionHandler);
		app.exception(BadParameterException.class, badParameterExceptionHandler);
		app.exception(DatabaseException.class, databaseExceptionHandler);
		app.exception(NotRealtorsAccountException.class, notRealtorsAccountExceptionHandler);
		app.exception(RealtorNotFoundException.class, realtorNotFoundExceptionHandler);
	}
	
}
