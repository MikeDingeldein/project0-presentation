package com.revature.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.dto.AddOrUpdateAccountDTO;
import com.revature.dto.AddOrUpdateClientDTO;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.util.JDBCUtility;

public class BankDOA {

	public Client addClient(AddOrUpdateClientDTO client) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
		String sql = "INSERT INTO clients (client_first_name, client_last_name) " + " VALUES (?,?)";//Add SQL line here
			
		PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, client.getFirstName());
			pstmt.setString(2, client.getLastName());
			
			int numberOfRecordsInserted = pstmt.executeUpdate();
			
			if (numberOfRecordsInserted != 1) {
				throw new SQLException("Adding New Client");
					
			}
			
			ResultSet rs = pstmt.getGeneratedKeys();
			
			rs.next();
			
			int automaticallyGeneratedID = rs.getInt(1);
			
			return new Client(automaticallyGeneratedID, client.getFirstName(), client.getLastName());
			
			
		}
				
	}

//Add additional methods here	

	public List<Client> getAllClients() throws SQLException {
		List<Client> listOfClients = new ArrayList<>();
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "SELECT * FROM clients";
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("client_id");
				String firstName = rs.getString("client_first_name");
				String lastName = rs.getString("client_last_name");

				Client c = new Client(id, firstName, lastName);
				listOfClients.add(c);

			}
		}
		return listOfClients;

	}

	public Client getClientById(int id) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "SELECT * FROM clients WHERE client_id = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new Client(rs.getInt("client_id"), rs.getString("client_first_name"),
						rs.getString("client_last_name"));

			} else {
				return null;
			}
		}
	}
//
	public Client updatedClient(int id, AddOrUpdateClientDTO client) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "UPDATE clients SET client_first_name = ?, client_last_name = ? WHERE client_id = ?;";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, client.getFirstName());
			pstmt.setString(2, client.getLastName());
			pstmt.setInt(3, id);

			int numberOfRecordsUpdated = pstmt.executeUpdate();

			if (numberOfRecordsUpdated != 1) {
				throw new SQLException("Unable to Update");

			}
		}
		return new Client(id, client.getFirstName(), client.getLastName());
	}

	public void deleteClientById(int id) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "DELETE FROM clients WHERE client_id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);

			int numberOfRecordsDeleted = pstmt.executeUpdate();

			if (numberOfRecordsDeleted != 1) {
				throw new SQLException("Unable to Delete");
			}
		}

	}

	public Account createNewAccount(AddOrUpdateAccountDTO account) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "INSERT INTO accounts (client_id, account_type, account_balance) " + " VALUES (?,?,?)";
			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, account.getClientId());
			pstmt.setString(2, account.getAccountType());
			pstmt.setDouble(3, account.getAccountBalance());

			int numberOfRecordsInserted = pstmt.executeUpdate();

			if (numberOfRecordsInserted != 1) {
				throw new SQLException("Adding New Account");
			}
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			int automaticallyGeneratedID = rs.getInt(1);
			return new Account(automaticallyGeneratedID, account.getClientId(), account.getAccountType(), account.getAccountBalance());
		}

	}

//	public Account getAccountByClientId(int id) throws SQLException {
//		try (Connection con = JDBCUtility.getConnection()) {
//			String sql = "SELECT * FROM account WHERE client_id = ?";
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				return new Account( rs.getInt("account_id"), rs.getInt("client_id"),
//						rs.getString("account_type"), rs.getDouble("account_balance"));
//
//			} else {
//				return null;
//			}
//
//		}
//
//	}
//
//	public Account getAccountByClientIdAndBalance(int id) throws SQLException {
//		try (Connection con = JDBCUtility.getConnection()) {
//			String sql = "SELECT * FROM client c inner join account a ON c.client_id = a.client_id WHERE a.client_id = ? AND a.account_balance BETWEEN 400 AND 2000";
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				return new Account(rs.getInt("client_id"), rs.getString("client_first_name"),
//						rs.getString("client_last_name"), rs.getString("account_id"), rs.getString("client_id"),
//						rs.getString("account_type"), rs.getString("account_balance"));
//
//			} else {
//				return null;
//			}
//
//		}
//
//	public Account getAccountByClientIdAndAcountId(int id, int aid) SQLException {
//		try (Connection con = JDBCUtility.getConnection()) {
//			String sql = "SELECT * FROM client c inner join account a ON c.client_id = a.client_id WHERE a.client_id = ? AND a.account_id = ?";
//	or try String sql = "SELECT * FROM accounts WHERE client_id = ? AND account_id = ?";
//			PreparedStatement pstmt = con.prepareStatement(sql);

	//			pstmt.setInt(1, id);
//			pstmt.setInt(2, aid);
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				return new Account(rs.getInt("client_id"), rs.getString("client_first_name"),
//						rs.getString("client_last_name"), rs.getString("account_id"), rs.getString("client_id"),
//						rs.getString("account_type"), rs.getString("account_balance"));
//
//			} else {
//				return null;
//			}
//
//		}
//
//	}
//
//	public Account editAccountByClientIdAndAcountId(int id, int aid) SQLException {
//		try (Connection con = JDBCUtility.getConnection()) {
//			String sql = "UPDATE account SET account_type = ?," + " account_balance = ?," + "WHERE "
//					+ "client_id = ? AND account_id = ?";
//
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, account.getAccountType());
//			pstmt.setString(2, account.getAccountBalance());
//			pstmt.setInt(3, id);
//			pstmt.setInt(4, accountId);
//			
//			int numberOfRecordsUpdated = pstmt.executeUpdate();
//
//			if (numberOfRecordsUpdated != 1) {
//				throw new SQLException("Unable to Update");
//
//			}
//		}
//		return new account(accountId, id, account.getAccountType(), account.getAccountBalance());
//	}
//
//	public void deleteAcountByClientIdAndAcountId(int id, int aid) throws SQLException {
//		try (Connection con = JDBCUtility.getConnection()) {
//			String sql = "DELETE FROM clients WHERE client_id = ? AND account_id = ?";
//
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			pstmt.setInt(2, aid);
//
//			int numberOfRecordsDeleted = pstmt.executeUpdate();
//
//			if (numberOfRecordsDeleted != 1) {
//				throw new SQLException("Unable to Delete");
//			}
//		}
//
//	}

}
