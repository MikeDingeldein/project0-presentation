package com.revature.dto;

import java.util.Objects;

public class AddOrUpdateAccountDTO {

	private int clientId;
	private int accountId;
	private String accountType;
	private double accountBalance;

	public AddOrUpdateAccountDTO() {

	}

	public AddOrUpdateAccountDTO(int clientId, String accountType, double accountBalance) { //int accountId,
		this.clientId = clientId;
//		this.accountId = accountId;
		this.accountType = accountType;
		this.accountBalance = accountBalance;
	}

	public AddOrUpdateAccountDTO(int accountId, int clientId, String accountType, double accountBalance) { //int accountId,
		this.clientId = clientId;
		this.accountId = accountId;
		this.accountType = accountType;
		this.accountBalance = accountBalance;
	}
		
	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountBalance, accountId, accountType, clientId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddOrUpdateAccountDTO other = (AddOrUpdateAccountDTO) obj;
		return Double.doubleToLongBits(accountBalance) == Double.doubleToLongBits(other.accountBalance)
				&& accountId == other.accountId && Objects.equals(accountType, other.accountType)
				&& clientId == other.clientId;
	}

	@Override
	public String toString() {
		return "AddOrUpdateAccountDTO [clientId=" + clientId + ", accountId=" + accountId + ", accountType="
				+ accountType + ", accountBalance=" + accountBalance + "]";
	}

}

