package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a store
 * 
 * @author huybui
 *
 */
public class Store implements Comparable<Store> {
	private String storeCode;
	private Person manager;
	private Address address;
	private List<Invoice> invoices;

	public Store(String storeCode, Person managerCode, Address address) {
		super();
		this.storeCode = storeCode;
		this.manager = managerCode;
		this.address = address;
		this.invoices = new ArrayList<Invoice>();
	}

	public String getStoreCode() {
		return storeCode;
	}

	public Person getManager() {
		return manager;
	}

	public Address getAddress() {
		return address;
	}

	public List<Invoice> getInvoiceStore() {
		return invoices;
	}

	public void addItem(Invoice i) {
		this.invoices.add(i);
	}

	public double getGrandTotal() {
		double grandTotal = 0.0;
		for (int i = 0; i < invoices.size(); i++) {
			grandTotal += this.invoices.get(i).getTotalTax() + this.invoices.get(i).getTotalPrice();
		}
		return grandTotal;
	}

	@Override
	public int compareTo(Store that) {
		int comparator = that.getManager().getLastName().compareToIgnoreCase(this.getManager().getLastName());
		if (comparator == 0) {
			comparator = that.getManager().getFirstName().compareToIgnoreCase(this.getManager().getLastName());
		}
		if (comparator == 0) {
			comparator = (int) (that.getGrandTotal() - this.getGrandTotal());
		}
		return comparator;
	}

	public static Store getDetailedStore(int storeId) {

		Store store = null;

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		String query = "select s.storeId,s.storeCode,s.managerId,s.addressId from Store s "
				+ "join Address a on s.addressId = a.addressId"
				+ "join Person p on p.personId = s.managerId where s.storeId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, storeId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String storeCode = rs.getString("s.storeCode");
				int managerId = rs.getInt("s.managerId");
				int addressId = rs.getInt("s.addressId");
				Person manager = Person.getDetailPerson(managerId);
				Address address = Address.getAddress(addressId);
				store = new Store(storeCode, manager, address);
			} else {
				throw new IllegalStateException("No such Store in database with storeId = " + storeId);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return store;
	}

}
