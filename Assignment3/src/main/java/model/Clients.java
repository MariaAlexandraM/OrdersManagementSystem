package model;

/**
 * Represents the Clients class that is identical with the table in the database
 */
public class Clients {
	protected int clientId;
	protected String clientName;
	protected String address;
	protected String email;

	public Clients() {
	}

	public Clients(int clientId, String clientName, String address, String email) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.address = address;
		this.email = email;
	}

	public Clients(String clientName, String address, String email) {
		this.clientName = clientName;
		this.address = address;
		this.email = email;
	}

	public Clients(int clientId) {
		this.clientId = clientId;
	}

    public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
