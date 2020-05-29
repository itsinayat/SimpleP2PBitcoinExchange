package com.altran.sbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {
	private static Connection conenction = null;

	private ConnectionProvider() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/bitcoin";
		Properties props = new Properties();
		props.setProperty("user", "postgres");
		props.setProperty("password", "root");
		Connection con = DriverManager.getConnection(url, props);
		conenction = con;
	}

	public static Connection getInstance() throws SQLException {
		if (conenction == null || conenction.isClosed()) {
			try {
				new ConnectionProvider();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conenction;

	}
}
