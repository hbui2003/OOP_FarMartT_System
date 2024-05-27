package com.fmt;

/**
 * 
 * Represents an item
 * 
 * @author huybui
 *
 */
public abstract class Item {
	private String code;
	private String name;

	public Item(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public abstract double getTaxes();

	public abstract double getTotalPrice();

	public abstract double getGrandTotal();

	public abstract String itemToString();
}
