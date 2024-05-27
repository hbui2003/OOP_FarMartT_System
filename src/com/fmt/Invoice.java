package com.fmt;

import java.util.ArrayList;
import java.util.List;

public class Invoice implements Comparable<Invoice> {
	private int invoiceId;
	private String invoiceCode;
	private Store store;
	private Person customer;
	private Person salesPerson;
	private String invoiceDate;
	private List<Item> items;

	public Invoice(int invoiceId, String invoiceCode, Store store, Person customer, Person salesPerson,
			String invoiceDate, List<Item> items) {
		super();
		this.invoiceId = invoiceId;
		this.invoiceCode = invoiceCode;
		this.store = store;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceDate = invoiceDate;
		this.items = items;
	}

	public Invoice(String invoiceCode, Store store, Person customer, Person salesPerson, String invoiceDate) {
		this.invoiceCode = invoiceCode;
		this.store = store;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceDate = invoiceDate;
		this.items = new ArrayList<>();
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public Store getStore() {
		return store;
	}

	public Person getCustomer() {
		return customer;
	}

	public Person getSalesperson() {
		return salesPerson;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item i) {
		this.items.add(i);
	}

	public double getTotalTax() {
		double tax = 0.0;
		for (Item i : items) {
			tax += i.getTaxes();
		}
		return tax;
	}

	public double getTotalPrice() {
		double totalPrice = 0.0;
		for (Item i : items) {
			totalPrice += i.getTotalPrice();
		}
		return totalPrice;
	}

	public double getGrandTotal() {
		return getTotalPrice() + getTotalTax();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Invoice # " + this.invoiceCode + "\n");
		sb.append("Store   # " + this.store.getStoreCode() + "\n");
		sb.append("Date      " + this.invoiceDate + "\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Invoice that) {
		int num = (int) (that.getTotalPrice() - this.getTotalPrice());
		return num;
	}

}