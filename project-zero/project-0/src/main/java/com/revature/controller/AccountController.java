package com.revature.controller;

import java.util.List;

import com.revature.dto.AddOrUpdateAccountDTO;
import com.revature.dto.AddOrUpdateClientDTO;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController {

	private AccountService accountService;
	private Logger logger = LoggerFactory.getLogger(AccountService.class); //for logging tests
	public AccountController() {
		this.accountService = new AccountService();

	}

	private Handler getAllAccountsByClientId = (ctx) -> {
		String clientId = ctx.pathParam("id");
		String greaterThan = ctx.queryParam("greaterThan"); //Use quaryParam for for parameters being past in.
		String lessThan = ctx.queryParam("lessThan");
		List<Account> account = this.accountService.getAllAccountsByClientId(clientId, greaterThan, lessThan);
		ctx.json(account);

	};

	private Handler getAccountByClientIdAndAcountId = (ctx) -> {
		String clientId = ctx.pathParam("id");
		String accountId = ctx.pathParam("accountId");
		Account account = this.accountService.getAccountByClientIdAndAcountId(clientId, accountId);
		ctx.json(account);

	};

	private Handler editAccountByClientIdAndAcountId = (ctx) -> {
		logger.info("getAccountByClientIdAndAcountId() invoked in Handler");
		String clientId = ctx.pathParam("id");
		String accountId = ctx.pathParam("accountId");
		AddOrUpdateAccountDTO dto = ctx.bodyAsClass(AddOrUpdateAccountDTO.class);
		Account account = this.accountService.editAccountByClientIdAndAcountId(clientId, accountId,
				dto.getAccountType(), dto.getAccountBalance());
		ctx.json(account);

	};
	
	private Handler deleteAcountByClientIdAndAcountId = (ctx) -> {// very unsure
		String id = ctx.pathParam("id");
		String aid = ctx.pathParam("accountId");
		this.accountService.deleteAcountByClientIdAndAcountId(id, aid);
	};
//	
//	private Handler editClientById = (ctx) -> {
//		String id = ctx.pathParam("id");
//		AddOrUpdateClientDTO dto = ctx.bodyAsClass(AddOrUpdateClientDTO.class);
//		Client c = this.bankService.editClientById(id, dto.getFirstName(), dto.getLastName());
//		ctx.json(c);
//	};



	public void registerEndPoints(Javalin app) {
		app.get("/clients/{id}/accounts", getAllAccountsByClientId);
//		app.get("/clients/{id}/accounts", getAllAccountsByClientId);
		app.get("/clients/{id}/accounts/{accountId}", getAccountByClientIdAndAcountId);
		app.put("/clients/{id}/accounts/{accountId}", editAccountByClientIdAndAcountId);
		app.delete("/clients/{id}/accounts/{accountId}", deleteAcountByClientIdAndAcountId);
	}
}