package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a person
 * 
 * @author huybui
 *
 */
public class Person {
	private int personId;
	private String personCode;
	private String firstName;
	private String lastName;
	private Address address;
	private List<String> emails;
	
	public Person(String personCode, String lastName, String firstName, Address address, List<String> emails) {
		super();
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emails = emails;
	}
	public Person(int personId, String personCode, String firstName, String lastName, Address address,
			List<String> emails) {
		super();
		this.personId = personId;
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emails = emails;
	}

	public Person(String personCode, String firstName, String lastName, Address address) {
		super();
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public List<String> getEmails() {
		return emails;
	}

	public String getPersonCode() {
		return this.personCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void addEmail(String email) {
		this.emails.add(email);
	}

	public Address getAddress() {
		return this.address;
	}

	public String getName() {
		return this.lastName + ", " + this.firstName;
	}

	@Override
	public String toString() {
		return String.format("%s, %-10s(%s : %s)\n \t %s \n \t %s %s %s %s\n", lastName, firstName, personCode, emails,
				address.getStreet(), address.getCity(), address.getState(), address.getZip(), address.getCountry());
	}

	public static Person getDetailPerson(int personId) {
		Person p = null;

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		String query = "select a.addressId,personCode,firstName,lastName from Person join Address a where personId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String personCode = rs.getString("personCode");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Address address = Address.getAddress(rs.getInt("a.addressId"));
				p = new Person(personCode, lastName, firstName, address);
			} else {
				throw new IllegalStateException("no such person with personId = " + personId);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		query = "select p.firstName, p.lastName, e.address\n"
				+ "from Person p\n"
				+ "join Email e on p.personId = e.personId \n"
				+ "where e.personId = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			while (rs.next()) {
				p.addEmail(rs.getString("e.address"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return p;
	}
}
