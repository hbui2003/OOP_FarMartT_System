package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * Represents an address
 * 
 * @author huybui
 *
 */
public class Address {
	private int addressId;
	private final String street;
	private final String city;
	private final String state;
	private final String zip;
	private final String country;
	
	public Address(int addressId, String street, String city, String state, String zip, String country) {
		super();
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	public Address(String street, String city, String state, String zip, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getCountry() {
		return country;
	}

	public String getStreet() {
		return this.street;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

	public String getZip() {
		return this.zip;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %s %s", this.street, this.city, this.state, this.zip, this.country);
	}

	public static Address getAddress(int addressId) {
		Address address = null;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		String query = "select a.street,a.city,a.zip,s.stateName,c.countryName\n" + "from Address a "
				+ "join State s on s.stateId = a.stateId\n" + "join Country c on c.countryId = a.countryId "
				+ "where a.addressId = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, addressId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String street = rs.getString("a.street");
				String city = rs.getString("a.city");
				String zip = rs.getString("a.zip");
				String state = rs.getString("s.stateName");
				String country = rs.getString("c.countryName");
				address = new Address(street, city, zip, state, country);
			} else {
				throw new IllegalStateException("no such Address with addressId = " + addressId);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return address;
	}

}
