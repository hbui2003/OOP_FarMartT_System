package com.fmt;

/**
 * Represent a product
 * 
 * @author huybui
 *
 */
public class Product extends Item {
	private String unit;
	private double unitPrice;
	private double numQuant;

	public Product(String code, String name, String unit, double unitPrice) {
		super(code, name);
		this.unit = unit;
		this.unitPrice = unitPrice;
	}

	public Product(String code, String name, String unit, double unitPrice, double numQuant) {
		super(code, name);
		this.unit = unit;
		this.unitPrice = unitPrice;
		this.numQuant = numQuant;
	}
	
	public String getUnit() {
		return unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}
	
	public double getNumQuant() {
		return numQuant;
	}
	@Override
	public double getTotalPrice() {
		return Math.round((unitPrice * numQuant)* 100.00)/100.00 ;
	}
	@Override
	public double getTaxes() {
		return Math.round(getTotalPrice() * 0.0715 * 100.00) / 100.00;
	}
	@Override
	public double getGrandTotal() {
		return getTaxes() + getTotalPrice();
	}
	@Override
	public String itemToString() { 
		return (String.format("\n%s\t\t(Product)      %s\n\t%.2f @ $%.2f/%s \n\t\t\t\t\t\t\t     $ %.2f", 
				this.getCode(),
				this.getName(),
				this.getNumQuant(),
				this.getUnitPrice(),
				this.getUnit(),
				this.getTotalPrice()));
	}
}

