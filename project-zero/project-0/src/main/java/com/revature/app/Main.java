package com.revature.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.AccountController;
import com.revature.controller.BankController;
import com.revature.controller.ExceptionMappingController;

import io.javalin.Javalin;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Javalin app = Javalin.create();
		
		Logger logger =LoggerFactory.getLogger(Main.class);
		
		app.before(ctx -> {
			logger.info(ctx.method() + "request received by " + ctx.path() + " endpoint");
		
		});
		
		BankController controller = new BankController();
		AccountController accountController = new AccountController();
		
		controller.registerEndPoints(app);
		accountController.registerEndPoints(app);
		
		
		ExceptionMappingController exceptionController = new ExceptionMappingController();
		exceptionController.mapException(app);
		
		
		app.start();
		
		
	}

}
