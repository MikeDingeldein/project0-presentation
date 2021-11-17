package com.revature.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.AddOrUpdateAccountDTO;
import com.revature.dto.AddOrUpdateClientDTO;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.AccountService;
import com.revature.util.JDBCUtility;

public class AccountDAO {

	private Logger logger = LoggerFactory.getLogger(AccountService.class); //for logging tests
	
	public void deleteAllAccountsByClientId(int clientId) throws SQLException {
		logger.info("deleteAllAccountsByClientId() invoked in DAO");
		
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "DELETE FROM accounts WHERE client_id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);

			pstmt.executeUpdate();
		}
	}
//		public void deleteAccountById(int aid) throws SQLException {
//			try (Connection con = JDBCUtility.getConnection()) {
//				String sql = "Delete FROM accounts WHERE account_id = ?";
//				
//				PreparedStatement pstmt = con.prepareStatement(sql);
//				pstmt.setInt(1, aid);
//				pstmt.executeUpdate();
//			}
//		}

	public List<Account> getAllAccountsByClientId(int clientId, int greaterThan, int lessThan) throws SQLException {
		List<Account> account = new ArrayList<>();
	//	logger.info("getAllAccountsByClientId() invoked in DAO");
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE client_id = ? AND account_balance >= ? and account_balance <= ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);
			pstmt.setInt(2, greaterThan);
			pstmt.setInt(3, lessThan);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int aid = rs.getInt("account_id");
				int id = rs.getInt("client_id");
				String accountType = rs.getString("account_type");
				double accountBalance = rs.getInt("account_balance");
//				String clientFirstName = rs.getString("clientFirstName");
//				String clientLastName = rs.getString("clientLastName");

				Account a = new Account(id, aid, accountType, accountBalance); //
//				Account a = new Account(id, aid, accountType, accountBalance, clientFirstName, clientLastName);
				account.add(a);
			}

		}

		return account;

	}

	public Account getAccountByClientIdAndAcountId(int clientId, int accountId) throws SQLException {
		
		logger.info("getAccountByClientIdAndAcountId() invoked in DAO");
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "SELECT * FROM accounts WHERE client_id = ? AND account_id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, clientId);
			pstmt.setInt(2, accountId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
//				int aid = rs.getInt("account_id");
//				int id = rs.getInt("client_id");
//				String accountType = rs.getString("account_type");
//				double accountBalance = rs.getInt("account_balance");
//				String clientFirstName = rs.getString("clientFirstName");
//				String clientLastName = rs.getString("clientLastName");
				return new Account (rs.getInt("account_id"), rs.getInt("client_id"), rs.getString("account_type"), rs.getInt("account_balance"));

				//Account a = new Account(id, aid, accountType, accountBalance); //
//				Account a = new Account(id, aid, accountType, accountBalance, clientFirstName, clientLastName);
				
			} 
			return null;
		}

//		return new Account(id, aid, accountType, accountBalance);

	}

	public Account updatedAccount(int id, int aid, AddOrUpdateAccountDTO account) throws SQLException {
		logger.info("updatedAccount() invoked in DAO");
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "UPDATE accounts SET account_type = ?, account_balance = ? WHERE client_id = ? and account_id = ?;";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, account.getAccountType());
			pstmt.setDouble(2, account.getAccountBalance());
			pstmt.setInt(3, id);
			pstmt.setInt(4, aid);

			int numberOfRecordsUpdated = pstmt.executeUpdate();

			if (numberOfRecordsUpdated != 1) {
				throw new SQLException("Unable to Update");

			}
		}
		return new Account(id, aid, account.getAccountType(), account.getAccountBalance());
	}

	public void deleteAcountByClientIdAndAcountId(int aid) throws SQLException {
		logger.info("deleteAcountByClientIdAndAcountId() invoked in DAO");
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "DELETE FROM accounts WHERE account_id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, aid);

			int numberOfRecordsDeleted = pstmt.executeUpdate();

			if (numberOfRecordsDeleted != 1) {
				throw new SQLException("Unable to Delete");
			}
		}

	}

//	public void deleteClientById(int id) throws SQLException {
//		try (Connection con = JDBCUtility.getConnection()) {
//			String sql = "DELETE FROM clients WHERE client_id = ?";
//
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, id);
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
