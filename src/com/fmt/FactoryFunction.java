package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FactoryFunction {

	// Returns a connection to the database
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return conn;
	}

	// Closes the given connection to the database.
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
