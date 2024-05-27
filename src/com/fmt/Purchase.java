package com.fmt;

public class Purchase extends Equipment {
	private double purchasePrice;

	public Purchase(String code, String name, String model, double purchasePrice) {
		super(code, name, model);
		this.purchasePrice = purchasePrice;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public double getTotalPrice() {
		return this.getPurchasePrice();
	}
	@Override
	public double getTaxes() {
		return 0;
	}
	@Override
	public String itemToString() { 
		return (String.format("\n%s\t\t(Purchase) %s-%s \n\t\t\t\t\t\t\t     $ %.2f", 
				this.getCode(),
				this.getName(),
				this.getModel(),
				this.getPurchasePrice()));
	}

}
