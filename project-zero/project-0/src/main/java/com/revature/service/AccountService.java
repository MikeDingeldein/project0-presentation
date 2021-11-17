package com.revature.service;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.doa.AccountDAO;
import com.revature.doa.BankDAO;
import com.revature.dto.AddOrUpdateAccountDTO;
import com.revature.dto.AddOrUpdateClientDTO;
import com.revature.exceptions.AccountNotFound;
import com.revature.exceptions.ClientNotFound;
import com.revature.model.Account;
import com.revature.model.Client;

import io.javalin.http.Context;

public class AccountService {

	private Logger logger = LoggerFactory.getLogger(AccountService.class); // for logging tests

	private AccountDAO accountDao;
	private BankDAO bankDao;

	public AccountService() {
		this.accountDao = new AccountDAO();
		this.bankDao = new BankDAO();

	}

	// Add for Mockito here
	public AccountService(AccountDAO accountDao, BankDAO bankDao) {
		this.accountDao = accountDao;
		this.bankDao = bankDao;
	}

	public List<Account> getAllAccountsByClientId(String clientId, String greaterThan, String lessThan)
			throws InvalidParameterException, ClientNotFound, SQLException {

		List<Account> accounts = new ArrayList<>();

		int id = Integer.parseInt(clientId);

//		logger.info("getAllAccountsByClientId() invoked");

		try {

			Client c = this.bankDao.getClientById(id);
			if (c == null) {
	//		logger.info("ClientNotFound() invoked");
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value");
		}

		if (greaterThan != null && lessThan != null) {
			int gt = Integer.parseInt(greaterThan);
			int lt = Integer.parseInt(lessThan);

			accounts = this.accountDao.getAllAccountsByClientId(id, gt, lt);

		} else if (lessThan != null) {

			int lT = Integer.parseInt(lessThan);

			accounts = this.accountDao.getAllAccountsByClientId(id, 0, lT);

		} else if (greaterThan != null) {
			int gT = Integer.parseInt(greaterThan);

			accounts = this.accountDao.getAllAccountsByClientId(id, gT, 400000000);
		} else {
			accounts = this.accountDao.getAllAccountsByClientId(id, 0, 400000000);
		}

		return accounts;
	}

	// get account by Id and client id
	public Account getAccountByClientIdAndAcountId(String clientId, String accountId)
			throws SQLException, InvalidParameterException, ClientNotFound, AccountNotFound {
		// Convert from String to int

//		List<Account> a1 = new ArrayList<>();

		logger.info("getAccountByClientIdAndAcountId() invoked");

		try {
			int id = Integer.parseInt(clientId);
			Client c = this.bankDao.getClientById(id);
			int aid = Integer.parseInt(accountId);
			Account a = this.accountDao.getAccountByClientIdAndAcountId(id, aid);
			if (c == null) {
				logger.info("ClientNotFound() invoked");
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}
			if (a == null) {
				throw new AccountNotFound("Account with id of " + accountId + " was not found.");
			}
			return a;
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value");
		}
	}

	public Account editAccountByClientIdAndAcountId(String clientId, String accountId, String changedAccountType,
			double changedAccountBalacne)
			throws SQLException, InvalidParameterException, ClientNotFound, AccountNotFound {
		// Convert from String to int

		logger.info("editAccountByClientIdAndAcountId() invoked");

		try {
			int id = Integer.parseInt(clientId);
			Client c = this.bankDao.getClientById(id);
			int aid = Integer.parseInt(accountId);
			Account a = this.accountDao.getAccountByClientIdAndAcountId(id, aid);
			if (c == null) {
				logger.info("ClientNotFound() invoked");
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}

			if (a == null) {
				throw new AccountNotFound("Account with id of " + accountId + " was not found.");
			}
			//

			AddOrUpdateAccountDTO dto = new AddOrUpdateAccountDTO(id, changedAccountType, changedAccountBalacne);
			Account updatedAccount = this.accountDao.updatedAccount(id, aid, dto);

			if (dto.getAccountType().trim().equals("") || dto.getAccountBalance() < 0) {
				throw new InvalidParameterException("Account Type or Account Balance can not be blank");
			}

			Set<String> validClassification = new HashSet<>();

			validClassification.add("Checking");
			validClassification.add("Savings");
			validClassification.add("Certificate of Deposit");
			validClassification.add("Overdraft Protection");

			if (!validClassification.contains(dto.getAccountType())) {
				throw new InvalidParameterException("You entered an invalid classification");
			}

			dto.setAccountType(dto.getAccountType().trim());
			dto.setAccountBalance(dto.getAccountBalance());

			return updatedAccount;
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value");
		}
	}

	// delete account with Id
	public void deleteAcountByClientIdAndAcountId(String clientId, String accountId)
			throws SQLException, InvalidParameterException, ClientNotFound, AccountNotFound {

		logger.info("deleteAcountByClientIdAndAcountId() invoked");

		try {
//			int id = Integer.parseInt(accountId);
//			Account a = this.accountDao.getAccountByClientIdAndAcountId(id);
//			if (a == null) {
//				throw new SQLException("Account with id of " + accountId + " was not found.");
//			}
			int id = Integer.parseInt(clientId);
			Client c = this.bankDao.getClientById(id);
			int aid = Integer.parseInt(accountId);
			Account a = this.accountDao.getAccountByClientIdAndAcountId(id, aid);
			if (c == null) {
				logger.info("ClientNotFound() invoked");
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}
			if (a == null) {
				throw new AccountNotFound("Account with id of " + accountId + " was not found.");
			}
			// delete all accounts for the client
			// this.accountDao.deleteAllAccountsByClientId(id);
			// delete the client
			this.accountDao.deleteAcountByClientIdAndAcountId(aid);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value.");
		}

	}

//	// delete client with Id
//	public void deleteClientById(String clientId) throws SQLException, InvalidParameterException, ClientNotFound {
//		try {
//			int id = Integer.parseInt(clientId);
//			Client c = this.bankDao.getClientById(id);
//			if (c == null) {
//				throw new SQLException("Client with id of " + clientId + " was not found.");
//			}
//			// delete all accounts for the client
//			this.accountDao.deleteAllAccountsByClientId(id);
//			// delete the client
//			this.bankDao.deleteClientById(id);
//		} catch (NumberFormatException e) {
//			throw new InvalidParameterException("Id provided is not an int convertable value.");
//		}
//
//	}

//	// edit client by Id
//	// , String firstName, String lastName
//	public Client editClientById(String clientId, String changedFirstName, String changedLastName)
//			throws SQLException, InvalidParameterException, ClientNotFound {
//		try {
//			int id = Integer.parseInt(clientId);
//			Client clientToEdit = this.bankDao.getClientById(id);
//			if (clientToEdit == null) {
//				throw new SQLException("Client with id of " + clientId + " was not found.");
//			}
//
//			AddOrUpdateClientDTO dto = new AddOrUpdateClientDTO(changedFirstName, changedLastName);
//			Client updatedClient = this.bankDao.updatedClient(id, dto);
//
//			if (dto.getFirstName().trim().equals("") || dto.getLastName().trim().equals("")) {
//				throw new InvalidParameterException("First name or last name can not be blank");
//			}
//			dto.setFirstName(dto.getFirstName().trim());
//			dto.setLastName(dto.getLastName().trim());
//
//			return updatedClient;
//		} catch (NumberFormatException e) {
//			throw new InvalidParameterException("Id provided is not an int convertable value.");
//		}
//	}
}
