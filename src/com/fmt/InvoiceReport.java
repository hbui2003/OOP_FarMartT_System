package com.fmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

public class InvoiceReport {

	// Generates a summary report of all invoices
	public static void summaryTotal(List<Invoice> invoices) {

		StringBuilder sb = new StringBuilder();
		sb.append("+----------------------------------------------------------------------------------------+\n");
		sb.append("| Summary Report - By Total                                                              |\n");
		sb.append("+----------------------------------------------------------------------------------------+\n");
		sb.append(String.format("%-12s  %-12s %-22s %-10s %8s %12s\n", "Invoice", "Store", "Customer", "Num Items",
				"Taxes", "Total"));

		double total = 0.0;
		double taxes = 0.0;
		int numItems = 0;

		for (Invoice invoiceCode : invoices) {
			numItems = numItems + invoiceCode.getItems().size();
			taxes = taxes + invoiceCode.getTotalTax();
			total = total + invoiceCode.getGrandTotal();
			sb.append(String.format("%-12s  %-12s %-25s %-10d $ %-8.2f $ %-10.2f\n", invoiceCode.getInvoiceCode(),
					invoiceCode.getStore().getStoreCode(), invoiceCode.getCustomer().getName(),
					invoiceCode.getItems().size(), invoiceCode.getTotalTax(), invoiceCode.getGrandTotal()));
		}

		sb.append("+----------------------------------------------------------------------------------------+\n");
		sb.append(String.format("\t\t\t\t\t\t    %-11d $ %-8.2f $ %-10.2f\n\n", numItems, taxes, total));
		System.out.print(sb);
	}

	// Generates a summary report of all stores
	public static void summaryStoreTotal(List<Store> stores) {

		double total = 0.0;
		int sales = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("+----------------------------------------------------------------+\n");
		sb.append("| Store Sales Summary Report                                     |\n");
		sb.append("+----------------------------------------------------------------+\n");
		sb.append(String.format("%-12s  %-16s %-15s %-15s \n", "Store", "Manger", "# Sales", "Grand Total"));

		for (Store store : stores) {
			sales = sales + store.getInvoiceStore().size();
			total = total + store.getGrandTotal();
			sb.append(String.format("%-13s %-19s %-12d %-12.2f\n", store.getStoreCode(), store.getManager().getName(),
					store.getInvoiceStore().size(), store.getGrandTotal()));
		}
		sb.append("+----------------------------------------------------------------+\n");
		sb.append(String.format("\t\t\t\t  %-10d $ %-12.2f \n\n", sales, total));
		System.out.print(sb);
	}

	// Generates a report of all items of each invoice
	public static void itemEachInvoice(HashMap<Integer, Invoice> invoices) {
		StringBuilder sb = new StringBuilder();
		for (int invoiceId : invoices.keySet()) {
			sb.append(invoices.get(invoiceId).toString());
			sb.append("Customer: \n");
			sb.append(invoices.get(invoiceId).getCustomer().toString());
			sb.append("\n");
			sb.append("Sales Person: \n");
			sb.append(invoices.get(invoiceId).getSalesperson().toString());
			sb.append("Item                                                               Total\n");
			sb.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=-\n");

			for (Item i : invoices.get(invoiceId).getItems()) {
				sb.append(i.itemToString());
			}
			sb.append("\n");
			sb.append("                                                             -=-=-=-=-=-\n");
			sb.append(String.format("                                                    Subtotal $ %.2f \n",
					invoices.get(invoiceId).getTotalPrice()));
			sb.append(String.format("                                                        Tax  $ %.2f \n",
					invoices.get(invoiceId).getTotalTax()));
			sb.append(String.format("                                                 Grand Total $ %.2f\n",
					invoices.get(invoiceId).getGrandTotal()));
		}
		System.out.print(sb);
	}

	/**
	 * This method takes in a linked list of Invoices and a type of sorting
	 * criteria, sorts the invoices based on that criteria and prints a formatted
	 * sales report.
	 *
	 */
	public static void sortedSaleReport(MyLinkedList<Invoice> invoices, String type) {

		StringBuilder sb = new StringBuilder();
		sb.append("+----------------------------------------------------------------------------------------+\n");
		sb.append("| Sales By " + type + "                         				                 |\n");
		sb.append("+----------------------------------------------------------------------------------------+\n");
		sb.append(String.format("%-12s  %-12s %-22s %-12s %12s\n", "Invoice", "Store", "Customer", "Salesperson",
				"Total"));

		for (Invoice invoiceCode : invoices) {
			sb.append(String.format("%-12s  %-12s %-22s %-18s  $ %-10.2f\n", invoiceCode.getInvoiceCode(),
					invoiceCode.getStore().getStoreCode(), invoiceCode.getCustomer().getName(),
					invoiceCode.getSalesperson().getName(), invoiceCode.getGrandTotal()));
		}
		System.out.print(sb);
	}

	public static void main(String[] args) {
		HashMap<Integer, Address> addrs = DatabaseLoader.getAddress();
		HashMap<Integer, Person> persons = DatabaseLoader.loadPerson(addrs);
		HashMap<Integer, Store> stores = DatabaseLoader.loadStore(addrs, persons);
		HashMap<Integer, Invoice> invoices = DatabaseLoader.loadInvoice(stores, persons);
		DatabaseLoader.loadInvoiceItem(invoices);

		// Define a comparator for sorting invoices by customer name
		Comparator<Invoice> nameCompare = new Comparator<Invoice>() {
			public int compare(Invoice i1, Invoice i2) {
				int result = i1.getCustomer().getLastName().compareTo(i2.getCustomer().getLastName());
				if (result == 0) {
					result = i1.getCustomer().getFirstName().compareTo(i2.getCustomer().getFirstName());
				}
				return result;
			}
		};

		// Create a linked list of invoices sorted by customer name
		MyLinkedList<Invoice> sortByName = new MyLinkedList<Invoice>(nameCompare);
		for (Integer i : invoices.keySet()) {
			sortByName.addElement(invoices.get(i));
		}

		// Define a comparator for sorting invoices by invoice total
		Comparator<Invoice> invoiceCompare = new Comparator<Invoice>() {
			public int compare(Invoice i1, Invoice i2) {
				if (i1.getGrandTotal() > i2.getGrandTotal()) {
					return -1;
				} else if (i1.getGrandTotal() < i2.getGrandTotal()) {
					return 1;
				} else {
					return 0;
				}
			}
		};

		// Create a linked list of invoices sorted by invoice total
		MyLinkedList<Invoice> sortByInvoice = new MyLinkedList<Invoice>(invoiceCompare);
		for (Integer i : invoices.keySet()) {
			sortByInvoice.addElement(invoices.get(i));
		}

		// Define a comparator for sorting invoices by store and salesperson name
		Comparator<Invoice> storeCompare = new Comparator<Invoice>() {
			public int compare(Invoice i1, Invoice i2) {
				int result = i1.getStore().getStoreCode().compareTo(i2.getStore().getStoreCode());
				if (result == 0) {
					result = i1.getSalesperson().getLastName().compareTo(i2.getSalesperson().getLastName());
					if (result == 0) {
						result = i1.getSalesperson().getFirstName().compareTo(i2.getSalesperson().getFirstName());
					}
				}
				return result;
			}
		};

		// Create a linked list of invoices sorted by store and salesperson name
		MyLinkedList<Invoice> sortByStore = new MyLinkedList<Invoice>(storeCompare);
		for (Integer i : invoices.keySet()) {
			sortByStore.addElement(invoices.get(i));
		}

		InvoiceReport.sortedSaleReport(sortByName, "Customer");
		InvoiceReport.sortedSaleReport(sortByInvoice, "Total");
		InvoiceReport.sortedSaleReport(sortByStore, "Store");
	}
}