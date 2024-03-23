package model;

/**
 * Represents the Orders class that is identical with the table in the database
 */
public class Orders {
	protected int orderId;
	protected int clientId;
	protected int productId;
	protected int orderedQuantity;
	
	public Orders() {
		
	}

	public Orders(int orderId, int clientId, int productId, int orderedQuantity) {
		this.orderId = orderId;
		this.clientId = clientId;
		this.productId = productId;
		this.orderedQuantity = orderedQuantity;
	}

	public Orders(int clientId, int productId, int orderedQuantity) {
		this.clientId = clientId;
		this.productId = productId;
		this.orderedQuantity = orderedQuantity;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

}
