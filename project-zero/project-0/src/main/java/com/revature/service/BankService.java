package com.revature.service;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.doa.AccountDAO;
import com.revature.doa.BankDAO;
import com.revature.dto.AddOrUpdateAccountDTO;
import com.revature.dto.AddOrUpdateClientDTO;
import com.revature.exceptions.ClientNotFound;
import com.revature.model.Account;
import com.revature.model.Client;

public class BankService {

	private Logger logger = LoggerFactory.getLogger(BankService.class); //for logging tests

	private BankDAO bankDao;
	private AccountDAO accountDao;

	
	//real constructors
	public BankService() {
		this.bankDao = new BankDAO();
		this.accountDao = new AccountDAO();
	}

//	Unit testing constructors
	public BankService(BankDAO bankDAO) {
		this.bankDao = bankDAO;
	}
	
//	public BankService(BankDAO bankDAO, AccountDAO accountDAO) {
//		this.bankDao = bankDAO;
//		this.accountDao = accountDAO;
//	}

	// add new client service here
	public Client addNewClient(AddOrUpdateClientDTO dto) throws SQLException, InvalidParameterException {
		logger.info("addNewClient() invoked");
		
		if (dto.getFirstName().trim().equals("") || dto.getLastName().trim().equals("")) {
			throw new InvalidParameterException("First name or last name can not be blank");
		}
		dto.setFirstName(dto.getFirstName().trim());
		dto.setLastName(dto.getLastName().trim());

		Client insertedClient = this.bankDao.addClient(dto);
		return insertedClient;
	}

	// get client list
	public List<Client> getAllClients() throws SQLException {
		logger.info("getAllClients() invoked");

		List<Client> client = this.bankDao.getAllClients();
		return client;
	}

	// get client by Id
	public Client getClientById(String clientId) throws SQLException, InvalidParameterException, ClientNotFound {
		logger.info("getClientById() invoked");
		// Convert from String to int
		try {
			int id = Integer.parseInt(clientId);
			Client c = this.bankDao.getClientById(id);
			if (c == null) {
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}
			return c;
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value");
		}
	}

//
//	// edit client by Id
	// , String firstName, String lastName
	public Client editClientById(String clientId, String changedFirstName, String changedLastName)
			throws SQLException, InvalidParameterException, ClientNotFound {
		logger.info("editClientById() invoked");
		try {
			int id = Integer.parseInt(clientId);
			Client clientToEdit = this.bankDao.getClientById(id);
			if (clientToEdit == null) {
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}

			AddOrUpdateClientDTO dto = new AddOrUpdateClientDTO(changedFirstName, changedLastName);
			Client updatedClient = this.bankDao.updatedClient(id, dto);

			if (dto.getFirstName().trim().equals("") || dto.getLastName().trim().equals("")) {
				throw new InvalidParameterException("First name or last name can not be blank");
			}
			dto.setFirstName(dto.getFirstName().trim());
			dto.setLastName(dto.getLastName().trim());

			return updatedClient;
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value.");
		}
	}

	// delete client with Id
	public void deleteClientById(String clientId) throws SQLException, InvalidParameterException, ClientNotFound {
		logger.info("deleteClientById() invoked");
		try {
			int id = Integer.parseInt(clientId);
			Client c = this.bankDao.getClientById(id);
			if (c == null) {
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}
			// delete all accounts for the client
			this.accountDao.deleteAllAccountsByClientId(id);
			// delete the client
			this.bankDao.deleteClientById(id);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value.");
		}

	}

	// create new account for client with Id
	public Account createNewAccount(AddOrUpdateAccountDTO dto)
			throws SQLException, InvalidParameterException, ClientNotFound {
		try {
			int id = Integer.valueOf(dto.getClientId());
			Client c = this.bankDao.getClientById(id);
			if (c == null) {
				throw new ClientNotFound("Client with id of " + id + " was not found.");
			}
//			return c;
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value.");
		}
		logger.info("createNewAccount() invoked");
		if (dto.getAccountType().trim().equals("")) { // || dto.getAccountBalance() == null
			throw new InvalidParameterException("Account type can not be blank");
		}

		Set<String> validClassification = new HashSet<>();

		validClassification.add("Checking");
		validClassification.add("Savings");
		validClassification.add("Certificate of Deposit");
		validClassification.add("Overdraft Protection");
	
		if (!validClassification.contains(dto.getAccountType())) {
			throw new InvalidParameterException("You entered an invalid account type");
		}

		dto.setAccountType(dto.getAccountType().trim());
		dto.setAccountBalance(dto.getAccountBalance());

		Account insertedAccount = this.bankDao.createNewAccount(dto);
		return insertedAccount;
	}

	public List<Account> getAllAccountsByClientId(String clientId, int greaterThan, int lessThan) throws SQLException, InvalidParameterException, ClientNotFound {
		// Convert from String to int
		logger.info("getAllAccountsByClientId() invoked");
		try {
			int id = Integer.parseInt(clientId);
			Client c = this.bankDao.getClientById(id);
			if (c == null) {
				throw new ClientNotFound("Client with id of " + clientId + " was not found.");
			}
			List<Account> account = this.accountDao.getAllAccountsByClientId(id, greaterThan, lessThan);
		
			return account;
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Id provided is not an int convertable value");
		}
	}

}
