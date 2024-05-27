package com.fmt;

/**
 * Represents a service
 * 
 * @author huybui
 *
 */
public class Service extends Item {

	private double hourlyRate;
	private double numOfHours;

	public Service(String code, String name, double hourlyRate) {
		super(code, name);
		this.hourlyRate = hourlyRate;
	}

	public Service(String code, String name, double hourlyRate, double numOfHours) {
		super(code, name);
		this.hourlyRate = hourlyRate;
		this.numOfHours = numOfHours;
	}
	
	public double getHourlyRate() {
		return this.hourlyRate;
	}
	public double getNumOfHours() {
		return numOfHours;
	}
	@Override
	public double getTotalPrice() {
		return Math.round((this.numOfHours * this.hourlyRate) * 100.00) / 100.00;
	}
	@Override
	public double getTaxes() {
		return Math.round((getTotalPrice() * 0.0345)* 100.00)/100.00; 
	}
	@Override
	public double getGrandTotal() {
		return getTaxes() + getTotalPrice();
	}
	@Override
	public String itemToString() { 
		return (String.format("\n%s\t\t(Service)      %s\n\t%.2f @ $%.2f /hr \n\t\t\t\t\t\t\t     $ %.2f", 
				this.getCode(),
				this.getName(),
				this.getNumOfHours(),
				this.getHourlyRate(),
				this.getTotalPrice()));
	}



}
