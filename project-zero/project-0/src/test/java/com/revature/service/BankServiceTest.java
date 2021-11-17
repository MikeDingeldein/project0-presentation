package com.revature.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.revature.doa.AccountDAO;
import com.revature.doa.BankDAO;
import com.revature.dto.AddOrUpdateAccountDTO;
import com.revature.dto.AddOrUpdateClientDTO;
import com.revature.exceptions.AccountNotFound;
import com.revature.exceptions.ClientNotFound;
import com.revature.model.Account;
import com.revature.model.Client;

public class BankServiceTest {

	// test the back service
	private BankService sut;

	// Create New Client

	@Test // positive test
	public void testGetAllClientsPositive() throws SQLException {
		// BankService requires a Dao to function
		// mock a bank object
		BankDAO mockBankDao = mock(BankDAO.class); // fake object
		// not "new" mock object

		Client client1 = new Client(10, "Bob", "Jones");
		Client client2 = new Client(11, "Jason", "Grey");
		Client client3 = new Client(100, "Jessica", "Smith");

		List<Client> clientsFromDao = new ArrayList<>();
		clientsFromDao.add(client1);
		clientsFromDao.add(client2);
		clientsFromDao.add(client3);

		when(mockBankDao.getAllClients()).thenReturn(clientsFromDao);

		BankService bankService = new BankService(mockBankDao);

		List<Client> testClients = bankService.getAllClients();

		System.out.println(testClients);

		List<Client> expectedClients = new ArrayList<>();
		expectedClients.add(new Client(10, "Bob", "Jones"));
		expectedClients.add(new Client(11, "Jason", "Grey"));
		expectedClients.add(new Client(100, "Jessica", "Smith"));

		Assertions.assertEquals(expectedClients, testClients);

	}

	@Test // negative test
	public void getAllClientsSQLExceptionOccurs() throws SQLException {
		BankDAO mockBankDao = mock(BankDAO.class); // fake object

		when(mockBankDao.getAllClients()).thenThrow(SQLException.class);

		BankService bankService = new BankService(mockBankDao);

		Assertions.assertThrows(SQLException.class, () -> {
			bankService.getAllClients(); // get a SQLException from bankService getAllCLients
		});

	}

	// Get Client
	@Test // positive
	public void testGetClientByIdPositive() throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		when(mockBankDao.getClientById(eq(5))).thenReturn(new Client(5, "Jake", "Trebek"));

		BankService bankService = new BankService(mockBankDao); // Remember to add argument. Like third time already...

		// Act

		Client actual = bankService.getClientById("5");
		System.out.println(actual);

		// Assert

		Assertions.assertEquals(new Client(5, "Jake", "Trebek"), actual);
	}

	@Test // Negative test
	public void testGetClientByIdNotFoundNegative() throws InvalidParameterException, SQLException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		// Act and Assert

		Assertions.assertThrows(ClientNotFound.class, () -> {
			bankService.getClientById("1");
		});

	}

	@Test // Negative test
	// Id is Alphabetical
	public void testGetClientByIdAlphabeticalIdNegative()
			throws InvalidParameterException, SQLException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		// Act and Assert

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bankService.getClientById("abc");
		});

	}

	@Test // Negative test
	// Id is a decimal
	public void testGetClientByIdDecimalIdNegative() throws InvalidParameterException, SQLException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		// Act and Assert

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bankService.getClientById("0.01");
		});

	}

	@Test // Positive test
	// edit client by Id
	public void testEditClientByIdPositive() throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		when(mockBankDao.getClientById(eq(5))).thenReturn(new Client(5, "Jake", "Trebek"));

		AddOrUpdateClientDTO dto = new AddOrUpdateClientDTO("Blake", "Schmitt");
		when(mockBankDao.updatedClient(eq(5), eq(dto))).thenReturn(new Client(5, "Blake", "Schmitt"));

		BankService bankService = new BankService(mockBankDao);

		// Act

		Client actual = bankService.editClientById("5", "Blake", "Schmitt");

		// Assert

		Client expected = new Client(5, "Blake", "Schmitt");

		Assertions.assertEquals(expected, actual);

	}

	// Negative test
	// ClientNotFound
	@Test
	public void testEditClientByIdButIddoesNotExist() {

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		Assertions.assertThrows(ClientNotFound.class, () -> {

			bankService.editClientById("50", "Blake", "Schmitt");

		});
	}

	// Negative test
	// ClientNotFound
	@Test
	public void testEditClientByIdButIdProvideedIsNotAnInt() {

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		Assertions.assertThrows(InvalidParameterException.class, () -> {

			bankService.editClientById("abc", "Blake", "Schmitt");

		});

	}

	// Add Client

	@Test // Positive
	public void testAddClientAllInformationCorrectInDTO() throws SQLException {

		// Arrange
		BankDAO clientDao = mock(BankDAO.class);

		AddOrUpdateClientDTO dtoIntoDao = new AddOrUpdateClientDTO("Jake", "Trebek");

		when(clientDao.addClient(eq(dtoIntoDao))).thenReturn(new Client(100, "Jake", "Trebek"));

		BankService bankService = new BankService(clientDao);

		// Act

		AddOrUpdateClientDTO dto = new AddOrUpdateClientDTO("Jake", "Trebek");
		Client insertedClient = bankService.addNewClient(dto);

		// Assert

		Client expected = new Client(100, "Jake", "Trebek");

		Assertions.assertEquals(expected, insertedClient);
	}

	// Negative
	@Test // First Name Blank
	public void testAddClientAllInformationCorrectExceptFirstName() throws SQLException {

		// Arrange
		BankDAO clientDao = mock(BankDAO.class);

		BankService bankService = new BankService(clientDao);

		// Act

		AddOrUpdateClientDTO dto = new AddOrUpdateClientDTO("     ", "Trebek");

		// Assert

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bankService.addNewClient(dto);
		});

	}

	// Negative
	@Test // Last Name Blank
	public void testAddClientAllInformationCorrectExceptLastName() throws SQLException {

		// Arrange
		BankDAO clientDao = mock(BankDAO.class);

		BankService bankService = new BankService(clientDao);

		// Act

		AddOrUpdateClientDTO dto = new AddOrUpdateClientDTO("Mike", "     ");

		// Assert

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bankService.addNewClient(dto);
		});

	}

	// Negative
	@Test // Both Name Blank
	public void testAddClientBothNamesAreBlank() throws SQLException {

		// Arrange
		BankDAO clientDao = mock(BankDAO.class);

		BankService bankService = new BankService(clientDao);

		// Act

		AddOrUpdateClientDTO dto = new AddOrUpdateClientDTO("   ", "     ");

		// Assert

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bankService.addNewClient(dto);
		});

	}

	// Delete by Id
	@Test // Client ID not found
	public void testDeleteClientByIdButIdProvideedDoesNotExist() {

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		Assertions.assertThrows(ClientNotFound.class, () -> {

			bankService.deleteClientById("50");

		});
	}

	@Test // Client ID not an Int
	public void testDeleteClientByIdButIdProvidedIsNotAnInt() {

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		Assertions.assertThrows(InvalidParameterException.class, () -> {

			bankService.deleteClientById("abc");

		});
	}

	// Add Account
	// review this test
	@Test // Positive
	public void testAddAccountAllInformationCorrectInDTO()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange
		BankDAO accountDao = mock(BankDAO.class);

		AddOrUpdateAccountDTO dtoIntoDao = new AddOrUpdateAccountDTO(100, "Checking", 100.00); // like 4th time
		when(accountDao.getClientById(eq(100))).thenReturn(new Client(100, "Jake", "Trebek"));
		when(accountDao.createNewAccount(eq(dtoIntoDao))).thenReturn(new Account(100, 100, "Checking", 100.00));

		BankService bankService = new BankService(accountDao);

		// Act

		AddOrUpdateAccountDTO dto = new AddOrUpdateAccountDTO(100, "Checking", 100.00);
		Account insertedAccount = bankService.createNewAccount(dto);

		// Assert

		Account expected = new Account(100, 100, "Checking", 100.00);

		Assertions.assertEquals(expected, insertedAccount);
	}

	// Negative
	@Test // Account Type Blank
	public void testAddAccountAllInformationCorrectExceptAccountTypeIsBlank() throws SQLException {

		// Arrange
		BankDAO AccountDao = mock(BankDAO.class);

		BankService bankService = new BankService(AccountDao);

		// Act
		when(AccountDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));
		AddOrUpdateAccountDTO dto = new AddOrUpdateAccountDTO(1, "     ", 100.00);

		// Assert

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bankService.createNewAccount(dto);
		});

	}

	// Negative
	@Test // Account Type Invalid
	public void testAddAccountAllInformationCorrectExceptAccountTypeIsInvalid() throws SQLException {

		// Arrange
		BankDAO AccountDao = mock(BankDAO.class);

		BankService bankService = new BankService(AccountDao);

		// Act
		when(AccountDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));
		AddOrUpdateAccountDTO dto = new AddOrUpdateAccountDTO(1, "Money", 100.00);

		// Assert

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bankService.createNewAccount(dto);
		});

	}

	// Get All Accounts by Client Id
	@Test // positive
	public void testGetAllAccountsByClientIdPositive() throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);

		Account mockDto = new Account(5, 1, "Checking", 1654.00);
		Account mockDto1 = new Account(6, 1, "Savings", 611.00);
		ArrayList<Account> mockList = new ArrayList<>();
		mockList.add(mockDto);
		mockList.add(mockDto1);
		when(mockBankDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));
		when(mockAccountDao.getAllAccountsByClientId(eq(1), eq(0), eq(100000000))).thenReturn(mockList);

		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		List<Account> actual = accountService.getAllAccountsByClientId("1", "0", "100000000");
		System.out.println(actual);

		// Assert
		ArrayList<Account> expected = new ArrayList<>();
		expected.add(new Account(5, 1, "Checking", 1654.00));
		expected.add(new Account(6, 1, "Savings", 611.00));
		Assertions.assertEquals(expected, actual);
	}

	@Test // negative
	public void testGetAllAccountsByClientIdButClientIdDoesNotExist()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);

		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		// Assert
		Assertions.assertThrows(ClientNotFound.class, () -> {
			accountService.getAllAccountsByClientId("500", "0", "100000000");
		});
	}

	// Get Account by client id account id
	@Test // positive
	public void testGetAccountsByClientIdAndAccountIdPositive()
			throws SQLException, InvalidParameterException, ClientNotFound, AccountNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);

		AccountDAO mockAccountDao = mock(AccountDAO.class);
		Account mockDto = new Account(5, 1, "Checking", 1654.00);
		Account mockDto1 = new Account(6, 1, "Savings", 611.00);
		ArrayList<Account> mockList = new ArrayList<>();
		mockList.add(mockDto);
		mockList.add(mockDto1);
		when(mockBankDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));

		when(mockAccountDao.getAccountByClientIdAndAcountId(eq(1), eq(5))).thenReturn(mockDto);

		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		Account actual = accountService.getAccountByClientIdAndAcountId("1", "5");
		System.out.println(actual);

		// Assert

		Assertions.assertEquals(new Account(5, 1, "Checking", 1654.00), actual);
	}

	@Test // negative
	public void testGetAllAccountsByClientIdAndAccountIdButClientIdDoesNotExist()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);

		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		// Assert
		Assertions.assertThrows(ClientNotFound.class, () -> {
			accountService.getAccountByClientIdAndAcountId("500", "6");
		});
	}

	@Test // negative
	public void testGetAllAccountsByClientIdAndAccountIdButAccountIdDoesNotExist()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);
		Account mockDto = new Account(5, 1, "Checking", 1654.00);
		Account mockDto1 = new Account(6, 1, "Savings", 611.00);
		ArrayList<Account> mockList = new ArrayList<>();
		mockList.add(mockDto);
		mockList.add(mockDto1);
		when(mockBankDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));
		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		// Assert
		Assertions.assertThrows(AccountNotFound.class, () -> {
			accountService.getAccountByClientIdAndAcountId("1", "500");
		});
	}

	@Test // negative
	public void testGetAllAccountsByClientIdAndAccountIdButClientIdIsNotAnInt()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);

		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		// Assert
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			accountService.getAccountByClientIdAndAcountId("abc", "5");
		});
	}

	@Test // negative
	public void testGetAllAccountsByClientIdAndAccountIdButAccountIdIsNotAnInt()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);
		Account mockDto = new Account(5, 1, "Checking", 1654.00);
		Account mockDto1 = new Account(6, 1, "Savings", 611.00);
		ArrayList<Account> mockList = new ArrayList<>();
		mockList.add(mockDto);
		mockList.add(mockDto1);
		when(mockBankDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));
		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		// Assert
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			accountService.getAccountByClientIdAndAcountId("1", "abc");
		});
	}

	@Test // Positive test
	// edit account by Id
	public void testEditAccountByIdPositive()
			throws SQLException, InvalidParameterException, ClientNotFound, AccountNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);
		AccountDAO mockAccountDao = mock(AccountDAO.class);

		when(mockAccountDao.getAccountByClientIdAndAcountId(eq(5), eq(5)))
				.thenReturn(new Account(5, 5, "Checking", 425.24));

		AddOrUpdateAccountDTO dto = new AddOrUpdateAccountDTO(5, "Checking", 500.96);
		when(mockBankDao.getClientById(eq(5))).thenReturn(new Client(5, "Jake", "Trebek"));
		when(mockAccountDao.updatedAccount(eq(5), eq(5), eq(dto))).thenReturn(new Account(5, 5, "Checking", 500.96));

		AccountService accountService = new AccountService(mockAccountDao, mockBankDao);

		// Act

		Account editActual = accountService.editAccountByClientIdAndAcountId("5", "5", "Checking", 500.96);

		// Assert

		Account expected = new Account(5, 5, "Checking", 500.96);

		Assertions.assertEquals(expected, editActual);

	}

	@Test // negative
	public void testEditAccountByIdButAccountIdDoesNotExist()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);
		when(mockBankDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));
		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		// Assert
		Assertions.assertThrows(AccountNotFound.class, () -> {
			accountService.editAccountByClientIdAndAcountId("1", "500", "Checking", 500.50);
		});
	}

	@Test // negative
	public void testEditAccountByIdButAccountTypeInvalid()
			throws SQLException, InvalidParameterException, ClientNotFound {

		// Arrange

		BankDAO mockBankDao = mock(BankDAO.class);

		BankService bankService = new BankService(mockBankDao);
		AccountDAO mockAccountDao = mock(AccountDAO.class);
		when(mockAccountDao.getAccountByClientIdAndAcountId(eq(1), eq(1)))
				.thenReturn(new Account(1, 1, "Checking", 425.24));
	//	AddOrUpdateAccountDTO dto = new AddOrUpdateAccountDTO(1, "Checking", 500.96);
		when(mockBankDao.getClientById(eq(1))).thenReturn(new Client(1, "Jake", "Trebek"));
	//	when(mockAccountDao.updatedAccount(eq(1), eq(1), eq(dto))).thenReturn(new Account(1, 1, "Checking", 500.96));
		AccountService accountService = new AccountService(mockAccountDao, mockBankDao); // Remember to add argument.

		// Act

		// Assert
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			accountService.editAccountByClientIdAndAcountId("1", "1", "Cash", 500.50);
		});
	}

	// Delete by Account by Account Id
	@Test // Client ID not found
	public void testDeleteAccountByIdButClientIdProvideedDoesNotExist() {

		BankDAO mockBankDao = mock(BankDAO.class);
		AccountDAO mockAccountDao = mock(AccountDAO.class);

		AccountService accountService = new AccountService(mockAccountDao, mockBankDao);

		Assertions.assertThrows(ClientNotFound.class, () -> {

			accountService.deleteAcountByClientIdAndAcountId("50", "50");

		});
	}
}
