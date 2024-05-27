package com.fmt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Lease extends Equipment {

	private double monthFee;
	private LocalDate startDate;
	private LocalDate endDate;

	public Lease(String code, String name, String model, double monthFee, String startDate, String endDate) {
		super(code, name, model);
		this.monthFee = monthFee;
		this.startDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
		this.endDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public double getMonthFee() {
		return monthFee;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public int getDaysBetween() {
		return (int) ChronoUnit.DAYS.between(getStartDate(), getEndDate()) + 1;
	}

	@Override
	public double getTotalPrice() {
		return Math.round((monthFee * this.getDaysBetween()) / 30.0 * 100.00) / 100.00;
	}

	@Override
	public double getTaxes() {
		double taxes = 0.0;
		if (this.getTotalPrice() < 10000) {
			taxes = 0;
		} else if (this.getTotalPrice() >= 10000 && this.getTotalPrice() < 100000) {
			taxes = 500;
		} else if (this.getTotalPrice() >= 100000) {
			taxes = 1500;
		}
		return taxes;
	}

	public String itemToString() {
		return (String.format(
				"\n%s   (Lease)   %s-%s\n   %d days (%s->%s) @ $%.2f / 30 days  \n\t\t\t\t\t\t\t     $ %.2f",
				this.getCode(), this.getName(), this.getModel(), this.getDaysBetween(), this.getStartDate(),
				this.getEndDate(), this.getMonthFee(), this.getTotalPrice()));
	}
}
