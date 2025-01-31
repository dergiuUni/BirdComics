/**
 * OrderDetail class - represents payment details.
 * @author Nam Ha Minh
 * @copyright https://codeJava.net
 */
package com.birdcomics.GestioneOrdine;

public class OrderDetail {
	private String productName;
	private float subtotal;
	private float shipping;
	private float tax;
	private float total;

	public OrderDetail(String productName, String subtotal, 
			String shipping, String tax, String total) {
		this.productName = productName;
		this.subtotal = Float.parseFloat(subtotal);
		this.shipping = Float.parseFloat(shipping);
		this.tax = Float.parseFloat(tax);
		this.total = Float.parseFloat(total);
		
		System.out.println(subtotal + shipping + shipping + tax + total);
	}

	public String getProductName() {
		return productName;
	}

	public String getSubtotal() {
		return String.format("%.2f", subtotal).replace(",", ".");
	}

	public String getShipping() {
		return String.format("%.2f", shipping).replace(",", ".");
	}

	public String getTax() {
		return String.format("%.2f", tax).replace(",", ".");
	}
	
	public String getTotal() {
		System.out.println(String.format("%.2f", total).replace(",", "."));
		return String.format("%.2f", total).replace(",", ".");
	}
}
