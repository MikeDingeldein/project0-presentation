package com.revature.controller;

import java.util.List;

import com.revature.dto.AddOrUpdateAccountDTO;

//import java.util.logging.Handler;

import com.revature.dto.AddOrUpdateClientDTO;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.AccountService;
import com.revature.service.BankService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class BankController {

	private BankService bankService; // Create a HAS-A relationship
	private AccountService accountService;
	

	public BankController() {
		this.bankService = new BankService();
		
		
	}

	public Handler addNewClient = (ctx) -> {
		AddOrUpdateClientDTO dto = ctx.bodyAsClass(AddOrUpdateClientDTO.class);

		Client s = this.bankService.addNewClient(dto);
		ctx.status(201);
		ctx.json(s);
		
	};

	private Handler getAllClients = (ctx) -> {
		List<Client> clients = this.bankService.getAllClients();

		ctx.json(clients);
	};

	private Handler getClientById = (ctx) -> {
		String id = ctx.pathParam("id");
		Client c = this.bankService.getClientById(id);
		ctx.json(c);
	};

	private Handler editClientById = (ctx) -> {
		String id = ctx.pathParam("id");
		AddOrUpdateClientDTO dto = ctx.bodyAsClass(AddOrUpdateClientDTO.class);
		Client c = this.bankService.editClientById(id, dto.getFirstName(), dto.getLastName());
		ctx.json(c);
	};

	private Handler deleteClientById = (ctx) -> {
		String id = ctx.pathParam("id");
		this.bankService.deleteClientById(id);
	};

	
	
	
	//	
	private Handler createNewAccount = (ctx) -> {
		AddOrUpdateAccountDTO dto = ctx.bodyAsClass(AddOrUpdateAccountDTO.class);

		Account a = this.bankService.createNewAccount(dto);

		ctx.json(a);
		ctx.status(201);
	};		
	
//	private Handler getAllAccountsByClientId = (ctx) -> {
//		String id = ctx.pathParam("id");
//		String amountGreaterThan = ctx.queryParam("amountGreaterThan");
//		String amountLessThan = ctx.queryParam("amountLessThan");
//	
//	
//	//String aid = ctx.pathParam("aid");
//	List<Account> a = this.accountService.getAllAccountsByClientId(id, ctx);
//	ctx.json(a);
//};
//	
//	private Handler getAccountByClientIdAndBalance = (ctx) -> {// very unsure
//		String id = ctx.pathParam("id");
//		Account a = this.bankService.getAccountById(id);
//		ctx.json(a);
//	};
//	
//	private Handler getAccountByClientIdAndAcountId = (ctx) -> {// very unsure
//		String id = ctx.pathParam("id");
//		String aid = ctx.pathParam("aid");
//		Account a = this.bankService.getAccountByClientIdAndAcountId(id, aid);
//		ctx.json(a);
//	};
//	
//	private Handler editAccountByClientIdAndAcountId = (ctx) -> {
//	};
//	
//	private Handler deleteAcountByClientIdAndAcountId = (ctx) -> {// very unsure
//		String id = ctx.pathParam("id");
//		String aid = ctx.pathParam("aid");
//		this.bankService.deleteAcountByClientIdAndAcountId(id, aid);
//	};
//	
	
	public void registerEndPoints(Javalin app) {
		app.post("/clients", addNewClient);
		app.get("/clients", getAllClients);
		app.get("/clients/{id}", getClientById);
		app.put("/clients/{id}", editClientById);
		app.delete("/clients/{id}", deleteClientById);
		app.post("/clients/{id}/accounts", createNewAccount);
//		app.get("/clients/{id}/accounts", getAllAccountsByClientId); //moved to Account Controller
//		app.get("/clients/{id}/accounts", getAccountByClientIdAndBalance); not needed
//		app.get("/clients/{id}/accounts/{accountId}", getAccountByClientIdAndAcountId); //moved to Account Controller
//		app.put("/clients/{id}/accounts/{accountId}", editAccountByClientIdAndAcountId); //moved to AccountController
//		app.delete("/clients/{id}/accounts/{accountId}", deleteAcountByClientIdAndAcountId);

	}
}
