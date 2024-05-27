package com.fmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 * 
	 * @throws SQLException
	 */
	public static void clearDatabase() {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = null;
		try {
			query = "delete from InvoiceItem;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from Item;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from Invoice;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from Store;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from Email;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from Person;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from Address;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from Country;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			query = "delete from State;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate(query);
			ps.close();

			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */

	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = null;
		int personId = InvoiceDataUtils.getPersonId(personCode);
		int addressId = InvoiceDataUtils.addAddress(street, city, state, zip, country);
		if (addressId == -1) {
			System.out.println("Address does not exist.");
			return;
		}
		if (personId == -1) {
			try {
				query = "INSERT INTO Person(personCode,firstName,lastName,addressId) VALUES (?,?,?,?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, personCode);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setInt(4, addressId);
				ps.executeUpdate();
				ps.close();
				FactoryFunction.closeConnection(conn);
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int personId = InvoiceDataUtils.getPersonId(personCode);
		if (personId == -1) {
			System.out.println("Person with code " + personCode + " does not exist.");
			return;
		}
		try {
			String query = "INSERT INTO Email(address, personId) VALUES (?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, email);
			ps.setInt(2, personId);
			ps.executeUpdate();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip, String country) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int storeId = InvoiceDataUtils.getStoreId(storeCode);
		int addressId = InvoiceDataUtils.addAddress(street, city, state, zip, country);
		int managerId = InvoiceDataUtils.getPersonId(managerCode);
		if (managerId == -1) {
			System.out.println("Manager with code " + managerCode + " does not exist.");
			return;
		}
		if (addressId == -1) {
			System.out.println("Address does not exist.");
			return;
		}
		if (storeId == -1) {
			try {
				String query = "INSERT INTO Store(storeCode,managerId,addressId) VALUES (?,?,?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, storeCode);
				ps.setInt(2, managerId);
				ps.setInt(3, addressId);
				ps.executeUpdate();
				ps.close();
				FactoryFunction.closeConnection(conn);

			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Adds a product record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>unit</code> and <code>pricePerUnit</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addProduct(String code, String name, String unit, double pricePerUnit) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = "INSERT INTO Item (itemCode,itemName,type,unit,unitPrice) VALUES (?,?,?,?,?)";
		int itemId = InvoiceDataUtils.getItemId(code);
		if (itemId == -1) {
			try {
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, "P");
				ps.setString(4, unit);
				ps.setDouble(5, pricePerUnit);
				ps.executeUpdate();
				ps.close();
				FactoryFunction.closeConnection(conn);

			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Adds an equipment record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>modelNumber</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addEquipment(String code, String name, String modelNumber) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = "INSERT INTO Item (itemCode,itemName,type,model) VALUES (?,?,?,?)";
		int itemId = InvoiceDataUtils.getItemId(code);
		if (itemId == -1) {
			try {
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, "E");
				ps.setString(4, modelNumber);
				ps.executeUpdate();
				ps.close();
				FactoryFunction.closeConnection(conn);
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Adds a service record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>costPerHour</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addService(String code, String name, double costPerHour) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = "INSERT INTO Item (itemCode,itemName,type,hourlyRate) VALUES (?,?,?,?)";
		int itemId = InvoiceDataUtils.getItemId(code);
		if (itemId == -1) {
			try {
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, "S");
				ps.setDouble(4, costPerHour);
				ps.executeUpdate();
				ps.close();
				FactoryFunction.closeConnection(conn);

			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Adds an invoice record to the database with the given data.
	 *
	 * @param invoiceCode
	 * @param storeCode
	 * @param customerCode
	 * @param salesPersonCode
	 * @param invoiceDate
	 */
	public static void addInvoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode,
			String invoiceDate) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int storeId = InvoiceDataUtils.getStoreId(storeCode);
		int customerId = InvoiceDataUtils.getPersonId(customerCode);
		int salespersonId = InvoiceDataUtils.getPersonId(salesPersonCode);
		int invoiceId = InvoiceDataUtils.getInvoiceId(salesPersonCode);
		if (customerId == -1) {
			System.out.println("Customer with code " + customerCode + " does not exist.");
			return;
		}
		if (salespersonId == -1) {
			System.out.println("SalesPerson with code " + salesPersonCode + " does not exist.");
			return;
		}
		if (storeId == -1) {
			System.out.println("Store with code " + storeCode + " does not exist.");
			return;
		}
		if (invoiceId == -1) {
			String query = "INSERT INTO Invoice(invoiceCode,storeId,customerId,salespersonId,invoiceDate) VALUES (?,?,?,?,?)";
			try {
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, invoiceCode);
				ps.setInt(2, storeId);
				ps.setInt(3, customerId);
				ps.setInt(4, salespersonId);
				ps.setString(5, invoiceDate);
				ps.executeUpdate();
				ps.close();
				FactoryFunction.closeConnection(conn);
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified quantity.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param quantity
	 */

	public static void addProductToInvoice(String invoiceCode, String itemCode, int quantity) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int invoiceId = InvoiceDataUtils.getInvoiceId(invoiceCode);
		int itemId = InvoiceDataUtils.getItemId(itemCode);
		if (invoiceId == -1) {
			System.out.println("Invoice with code " + invoiceCode + " does not exist.");
			return;
		}
		if (itemId == -1) {
			System.out.println("Item with code " + itemCode + " does not exist.");
			return;
		}
		try {
			String query = "INSERT INTO InvoiceItem (invoiceId, itemId, numQuant,type) VALUES (?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setInt(3, quantity);
			ps.setString(4, null);
			ps.executeUpdate();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular equipment <i>purchase</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) at the given <code>purchasePrice</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param purchasePrice
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double purchasePrice) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int invoiceId = InvoiceDataUtils.getInvoiceId(invoiceCode);
		int itemId = InvoiceDataUtils.getItemId(itemCode);
		if (invoiceId == -1) {
			System.out.println("Invoice with code " + invoiceCode + " does not exist.");
			return;
		}
		if (itemId == -1) {
			System.out.println("Item with code " + itemCode + " does not exist.");
			return;
		}
		String query = "INSERT INTO InvoiceItem (invoiceId, itemId, purchasePrice,type) VALUES(?,?,?,?)";
		try {
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, purchasePrice);
			ps.setString(4, "P");
			ps.executeUpdate();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular equipment <i>lease</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) with the given 30-day <code>periodFee</code> and
	 * <code>beginDate/endDate</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param amount
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double periodFee, String beginDate,
			String endDate) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int invoiceId = InvoiceDataUtils.getInvoiceId(invoiceCode);
		int itemId = InvoiceDataUtils.getItemId(itemCode);
		if (invoiceId == -1) {
			System.out.println("Invoice with code " + invoiceCode + " does not exist.");
			return;
		}
		if (itemId == -1) {
			System.out.println("Item with code " + itemCode + " does not exist.");
			return;
		}
		String query = "INSERT INTO InvoiceItem (invoiceId, itemId, monthFee, startDate, endDate,type) "
				+ "VALUES (?, ?, ?, ?,?,?);";
		try {
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, periodFee);
			ps.setString(4, beginDate);
			ps.setString(5, endDate);
			ps.setString(6, "L");
			ps.executeUpdate();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified number of hours.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param billedHours
	 */
	public static void addServiceToInvoice(String invoiceCode, String itemCode, double billedHours) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int invoiceId = InvoiceDataUtils.getInvoiceId(invoiceCode);
		int itemId = InvoiceDataUtils.getItemId(itemCode);
		if (invoiceId == -1) {
			System.out.println("Invoice with code " + invoiceCode + " does not exist.");
			return;
		}
		if (itemId == -1) {
			System.out.println("Item with code " + itemCode + " does not exist.");
			return;
		}
		String query = "INSERT INTO InvoiceItem (invoiceId, itemId, numOfHours,type) VALUES (?,?,?,?)";
		try {
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, billedHours);
			ps.setString(4, null);
			ps.executeUpdate();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}