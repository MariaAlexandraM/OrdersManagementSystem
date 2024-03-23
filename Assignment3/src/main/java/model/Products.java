package model;

/**
 * Represents the Products class that is identical with the table in the database
 */
public class Products {
	protected int productId;
	protected String productName;
	protected int quantity;
	protected double price;

	public Products() {
	}

	public Products(int productId) {
		this.productId = productId;
	}

	public Products(String productName) {
		this.productName = productName;
	}

	public Products(int productId, String productName, int quantity, double price) {
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
	}

	public Products(String productName, int quantity, double price) {
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
