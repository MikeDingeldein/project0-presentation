package com.revature.model;

import java.util.Objects;

public class Client {

	private int clientId;
	private String clientFirstName;
	private String clientLastName;
	
	public Client() {
		
	}
	
	public Client(int clientId, String clientFirstName, String clientLastName) {
		super();
		this.clientId = clientId;
		this.clientFirstName = clientFirstName;
		this.clientLastName = clientLastName;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientFirstName() {
		return clientFirstName;
	}

	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}

	public String getClientLastName() {
		return clientLastName;
	}

	public void setClientLastName(String clientLastName) {
		this.clientLastName = clientLastName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientFirstName, clientLastName, clientId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(clientFirstName, other.clientFirstName)
				&& Objects.equals(clientLastName, other.clientLastName) && clientId == other.clientId;
	}

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", clientFirstName=" + clientFirstName + ", clientLastName=" + clientLastName + "]";
	}
	
	
}
