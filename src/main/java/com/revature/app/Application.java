package com.revature.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.AccountController;
import com.revature.controller.Controller;
import com.revature.controller.ExceptionController;
import com.revature.controller.RealtorController;
import com.revature.controller.StaticFileController;

import io.javalin.Javalin;

public class Application {

	private static Javalin app;
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		app = Javalin.create();

		app.before(ctx -> {
			String uri = ctx.req.getRequestURI();
			String httpMethod = ctx.req.getMethod();
			LOGGER.info("{} request to endpoint {} received.", httpMethod, uri);
		});

		mapControllers(new AccountController(), new ExceptionController(), new RealtorController(),
				new StaticFileController());

		app.start(7009);

	}

	public static void mapControllers(Controller... controllers) {
		for (Controller c : controllers) {
			c.mapEndpoints(app);
		}
	}

}
