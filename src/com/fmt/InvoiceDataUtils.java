package com.fmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.core.appender.db.jdbc.FactoryMethodConnectionSource;

public class InvoiceDataUtils {
	// Retrieves the ID of the statId with the specified name from the database
	public static int getStateId(String stateName) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int stateId = -1;
		String query = "SELECT stateId FROM State WHERE StateName = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, stateName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				stateId = rs.getInt("stateId");
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return stateId;
	}

	// Retrieves the ID of the country with the specified name from the database
	public static int getCountryId(String countryName) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		int countryId = -1;
		String query = "SELECT countryId FROM Country WHERE countryName = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, countryName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				countryId = rs.getInt("countryId");
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return countryId;
	}

	// Add the address into database
	public static int addAddress(String street, String city, String state, String zip, String country) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = null;
		int addressId = -1;
		int stateId = getStateId(state);
		int countryId = getCountryId(country);
		if (stateId == -1) {
			try {
				query = "INSERT INTO State(stateName) VALUES (?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, state);
				ps.executeUpdate();
				ResultSet keys = ps.getGeneratedKeys();
				if (keys.next()) {
					stateId = keys.getInt(1);
				}
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		if (countryId == -1) {
			try {
				query = "INSERT INTO Country(countryName) VALUES (?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, country);
				ps.executeUpdate();
				ResultSet keys = ps.getGeneratedKeys();
				if (keys.next()) {
					countryId = keys.getInt(1);
				}
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		try {
			query = "INSERT INTO Address(street,city,zip,stateId,countryId) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setInt(4, stateId);
			ps.setInt(5, countryId);
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				addressId = keys.getInt(1);
			}
			keys.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return addressId;
	}

	public static int getPersonId(String personCode) {
		Connection conn = null;
		PreparedStatement ps = null;
		int personId = -1;
		String query = "SELECT personId FROM Person WHERE personCode = ?";
		ResultSet rs = null;
		try {
			conn = FactoryFunction.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				personId = rs.getInt("personId");
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return personId;
	}

	// Retrieves the ID of the stordId with the specified name from the database
	public static int getStoreId(String storeCode) {
		Connection conn = null;
		PreparedStatement ps = null;
		String query = "SELECT storeId FROM Store WHERE storeCode = ?";
		int storeId = -1;
		try {
			conn = FactoryFunction.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				storeId = rs.getInt("storeId");
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return storeId;
	}

	// Retrieves the ID of the itemId with the specified name from the database
	public static int getItemId(String itemCode) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = "SELECT itemId FROM Item WHERE itemCode = ?";
		int itemId = -1;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return itemId;
	}

	// Retrieves the ID of the invoiceId with the specified name from the database
	public static int getInvoiceId(String invoiceCode) {
		Connection conn = FactoryFunction.getConnection();
		PreparedStatement ps = null;
		String query = "SELECT invoiceId FROM Invoice WHERE invoiceCode = ?";
		int invoiceId = -1;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				invoiceId = rs.getInt("invoiceId");
			}
			rs.close();
			ps.close();
			FactoryFunction.closeConnection(conn);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return invoiceId;
	}
}
