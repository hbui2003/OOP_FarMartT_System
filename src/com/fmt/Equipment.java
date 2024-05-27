package com.fmt;

/**
 * Represent an equipment
 * 
 * @author huybui
 *
 */
public class Equipment extends Item {
	private String model;

	public Equipment(String code, String name, String model) {
		super(code, name);
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	public double getTotalPrice() {
		return 0;
	}

	public double getTaxes() {
		return 0;
	}

	public double getGrandTotal() {
		return 0;
	}

	public String itemToString() {
		return null;
	}
}