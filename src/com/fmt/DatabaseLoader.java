package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a collection of utility methods that loading the data from
 * SQLWorkBench to Java classes.
 *
 */
public class DatabaseLoader {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);

	// Load the address data from database
	public static HashMap<Integer, Address> getAddress() {
		HashMap<Integer, Address> address = new HashMap<>();
		LOGGER.info("Start loading all address...");
		Address a = null;
		Connection conn = FactoryFunction.getConnection();
		String query = "select a.addressId, a.street,a.city,a.zip,s.stateName,c.countryName from Address a join State s on s.stateId = a.stateId join Country c on c.countryId = a.countryId ";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int addressId = rs.getInt("a.addressId");
				String street = rs.getString("a.street");
				String city = rs.getString("a.city");
				String zip = rs.getString("a.zip");
				String state = rs.getString("s.stateName");
				String country = rs.getString("c.countryName");
				a = new Address(street, city, zip, state, country);
				address.put(addressId, a);
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		LOGGER.info("Done loading data all address...");
		return address;
	}

	// Load the list of email data from specific person
	public static List<String> getEmail(int personId) {
		List<String> emails = new ArrayList<>();
		Connection conn = FactoryFunction.getConnection();

		String query = "select address from Email where personId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			while (rs.next()) {
				String email = rs.getString("address");
				emails.add(email);
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			LOGGER.error("SQLException: ");
			throw new RuntimeException(e);
		}
		return emails;
	}

	// Load the person data from database
	public static HashMap<Integer, Person> loadPerson(HashMap<Integer, Address> address) {
		HashMap<Integer, Person> persons = new HashMap<>();
		LOGGER.info("Start loading data all people...");
		Connection conn = FactoryFunction.getConnection();

		String query = "select addressId,personId, personCode,firstName,lastName from Person;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int personId = rs.getInt("personId");
				int addressId = rs.getInt("addressId");
				String personCode = rs.getString("personCode");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Address addr = address.get(addressId);
				Person p = new Person(personCode, lastName, firstName, addr, getEmail(personId));
				persons.put(personId, p);
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			LOGGER.error("SQLException: ");
			throw new RuntimeException(e);
		}
		LOGGER.info("Done loading data all people...");

		return persons;
	}

	// Load the store data from database
	public static HashMap<Integer, Store> loadStore(HashMap<Integer, Address> addr, HashMap<Integer, Person> persons) {
		HashMap<Integer, Store> stores = new HashMap<>();
		LOGGER.info("Start loading all stores...");
		Connection conn = FactoryFunction.getConnection();

		String query = "select storeCode,storeId, managerId,addressId from Store;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int storeId = rs.getInt("storeId");
				String storeCode = rs.getString("storeCode");
				int managerId = rs.getInt("managerId");
				int addressId = rs.getInt("addressId");
				Person manager = persons.get(managerId);
				Address address = addr.get(addressId);
				Store store = new Store(storeCode, manager, address);
				stores.put(storeId, store);
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			LOGGER.error("SQLException: ");
			throw new RuntimeException(e);
		}
		LOGGER.info("Done loading all stores...");
		return stores;
	}

	// Load the address data from database
	public static HashMap<Integer, Item> loadItem() {
		HashMap<Integer, Item> items = new HashMap<>();
		LOGGER.info("Start loading all items...");
		Connection conn = FactoryFunction.getConnection();

		String query = "select itemId,itemCode,itemName,model,hourlyRate,unit,unitPrice,type from Item";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("itemId");
				String itemCode = rs.getString("itemCode");
				String itemName = rs.getString("itemName");
				String model = rs.getString("model");
				double hourlyRate = rs.getDouble("hourlyRate");
				String unit = rs.getString("unit");
				double unitPrice = rs.getDouble("unitPrice");
				String type = rs.getString("type");
				Item item = null;
				if (type.equals("E")) {
					item = new Equipment(itemCode, itemName, model);

				} else if (type.equals("P")) {
					item = new Product(itemCode, itemName, unit, unitPrice);

				} else if (type.equals("S")) {
					item = new Service(itemCode, itemName, hourlyRate);
				}
				items.put(itemId, item);
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		LOGGER.info("Done loading all items...");
		return items;
	}

	// Load the Invoice data from database
	public static HashMap<Integer, Invoice> loadInvoice(HashMap<Integer, Store> stores,
			HashMap<Integer, Person> persons) {

		HashMap<Integer, Invoice> invoices = new HashMap<>();
		LOGGER.info("Start loading all invoices...");
		Connection conn = FactoryFunction.getConnection();

		String query = "select invoiceId, invoiceCode,storeId,customerId,salespersonId,invoiceDate from Invoice";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("invoiceCode");
				int storeId = rs.getInt("storeId");
				Store store = stores.get(storeId);
				int customerId = rs.getInt("customerId");
				Person customer = persons.get(customerId);
				int salespersonId = rs.getInt("salespersonId");
				Person salesPerson = persons.get(salespersonId);
				String invoiceDate = rs.getString("invoiceDate");
				Invoice invoice = new Invoice(invoiceCode, store, customer, salesPerson, invoiceDate);
				invoices.put(invoiceId, invoice);
				store.addItem(invoice);
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		LOGGER.info("Done loading all invoices...");
		return invoices;
	}

	// loading a invoiceItem data from database
	public static List<Item> loadInvoiceItem(HashMap<Integer, Invoice> invoices) {
		List<Item> invoiceItems = new ArrayList<>();
		HashMap<Integer, Item> items = DatabaseLoader.loadItem();
		Connection conn = FactoryFunction.getConnection();

		String query = "select invoiceItemId,itemId,invoiceId,purchasePrice,monthFee,startDate,endDate,numQuant,numOfHours,type from InvoiceItem";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int invoiceItemId = rs.getInt("invoiceItemId");
				int itemId = rs.getInt("itemId");
				int invoiceId = rs.getInt("invoiceId");
				double purchasePrice = rs.getDouble("purchasePrice");
				double monthFee = rs.getDouble("monthFee");
				String startDate = rs.getString("startDate");
				String endDate = rs.getString("endDate");
				double numQuant = rs.getDouble("numQuant");
				double numOfHours = rs.getDouble("numOfHours");
				String type = rs.getString("type");
				Item i = items.get(itemId);
				Item item = null;

				if (i instanceof Service) {
					item = new Service(i.getCode(), i.getName(), ((Service) i).getHourlyRate(), numOfHours);

				} else if (i instanceof Product) {
					item = new Product(i.getCode(), i.getName(), ((Product) i).getUnit(), ((Product) i).getUnitPrice(),
							numQuant);

				} else if (i instanceof Equipment) {
					if (type.equals("P")) {
						item = new Purchase(i.getCode(), i.getName(), ((Equipment) i).getModel(), purchasePrice);

					} else if (type.equals("L")) {
						item = new Lease(i.getCode(), i.getName(), ((Equipment) i).getModel(), monthFee, startDate,
								endDate);
					}
				}
				invoiceItems.add(item);
				invoices.get(invoiceId).addItem(item);
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return invoiceItems;
	}
}
