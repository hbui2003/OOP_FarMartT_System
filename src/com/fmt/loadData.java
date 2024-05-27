package com.fmt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * Loading a given file
 * 
 * @author huybui
 *
 */
public class loadData {

	public static HashMap<String, Person> loadPerson() {

		HashMap<String, Person> result = new HashMap<>();

		String line = null;

		try (Scanner s = new Scanner(new File("data/Persons.csv"))) {

			int numRecords = Integer.parseInt(s.nextLine());
			for (int i = 0; i < numRecords; i++) {
				line = s.nextLine();
				Person p = null;
				List<String> emails = new ArrayList<>();
				String tokens[] = line.split(",");
				String personCode = tokens[0];
				String lastName = tokens[1];
				String firstName = tokens[2];
				String street = tokens[3];
				String city = tokens[4];
				String state = tokens[5];
				String zip = tokens[6];
				String county = tokens[7];

				Address addresss = new Address(street, city, state, zip, county);
				for (int j = 8; j < tokens.length; j++) {
					String email = tokens[j];
					emails.add(email);
				}
				p = new Person(personCode, lastName, firstName, addresss, emails);
				result.put(personCode, p);
			}
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}

	public static HashMap<String, Store> loadStore(HashMap<String, Person> persons) {

		HashMap<String, Store> result = new HashMap<>();

		String line = null;

		try (Scanner s = new Scanner(new File("data/Stores.csv"))) {

			int numRecords = Integer.parseInt(s.nextLine());
			for (int i = 0; i < numRecords; i++) {
				line = s.nextLine();
				Store e = null;
				String tokens[] = line.split(",");
				String storeCode = tokens[0];
				Person managerCode = persons.get(tokens[1]);
				String street = tokens[2];
				String city = tokens[3];
				String state = tokens[4];
				String zip = tokens[5];
				String county = tokens[6];

				Address addresss = new Address(street, city, state, zip, county);
				e = new Store(storeCode, managerCode, addresss);
				result.put(storeCode, e);
			}
			s.close();
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}

	public static HashMap<String, Item> loadItem() {

		HashMap<String, Item> result = new HashMap<>();
		double unitPrice = 0.0;
		double hourlyRate = 0.0;
		String model = null;
		String unit = null;
		String line = null;

		try (Scanner s = new Scanner(new File("data/Items.csv"))) {

			int numRecords = Integer.parseInt(s.nextLine());
			for (int i = 0; i < numRecords; i++) {
				line = s.nextLine();
				Item e = null;
				String tokens[] = line.split(",");
				String itemCode = tokens[0];
				String type = tokens[1];
				String name = tokens[2];

				if (type.equals("E")) {
					model = tokens[3];
					e = new Equipment(itemCode, name, model);

				} else if (type.equals("P")) {
					unit = tokens[3];
					unitPrice = Double.parseDouble(tokens[4]);
					e = new Product(itemCode, name, unit, unitPrice);

				} else if (type.equals("S")) {
					hourlyRate = Double.parseDouble(tokens[3]);
					e = new Service(itemCode, name, hourlyRate);
				}
				result.put(itemCode, e);
			}
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}

	public static HashMap<String, Invoice> loadInvoicedata(HashMap<String, Store> stores,
			HashMap<String, Person> persons) {
		HashMap<String, Invoice> result = new HashMap<String, Invoice>();

		String line = null;

		try (Scanner s = new Scanner(new File("data/Invoices.csv"))) {

			int numRecords = Integer.parseInt(s.nextLine());
			for (int i = 0; i < numRecords; i++) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				String invoiceCode = tokens[0];
				Store store = stores.get(tokens[1]);
				Person customer = persons.get(tokens[2]);
				Person salesPerson = persons.get(tokens[3]);
				String invoiceDate = tokens[4];
				Invoice invoice = new Invoice(invoiceCode, store, customer, salesPerson, invoiceDate);
				result.put(invoiceCode, invoice);
				store.addItem(invoice);
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<Item> loadInvoiceItem(HashMap<String, Invoice> invoices) {

		HashMap<String, Item> items = loadData.loadItem();
		List<Item> listInvItem = new ArrayList<>();
		String line = null;

		try (Scanner s = new Scanner(new File("data/InvoiceItems.csv"))) {
			int numRecords = Integer.parseInt(s.nextLine());
			for (int z = 0; z < numRecords; z++) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				String invoiceCode = tokens[0];
				Item i = items.get(tokens[1]);
				Item item = null;

				if (i instanceof Service) {
					double numofHours = Double.parseDouble(tokens[2]);
					item = new Service(i.getCode(), i.getName(), ((Service) i).getHourlyRate(), numofHours);

				} else if (i instanceof Product) {
					double numQuant = Double.parseDouble(tokens[2]);
					item = new Product(i.getCode(), i.getName(), ((Product) i).getUnit(), ((Product) i).getUnitPrice(),
							numQuant);

				} else if (i instanceof Equipment) {
					if (tokens[2].equals("P")) {
						double purchasePrice = Double.parseDouble(tokens[3]);
						item = new Purchase(i.getCode(), i.getName(), ((Equipment) i).getModel(), purchasePrice);

					} else if (tokens[2].equals("L")) {
						double monthFee = Double.parseDouble(tokens[3]);
						String startDate = tokens[4];
						String endDate = tokens[5];
						item = new Lease(i.getCode(), i.getName(), ((Equipment) i).getModel(), monthFee, startDate,
								endDate);
					}

				}
				listInvItem.add(item);
				invoices.get(invoiceCode).addItem(item);
			}

			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInvItem;
	}
}
