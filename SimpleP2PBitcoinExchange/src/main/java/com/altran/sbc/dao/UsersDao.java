package com.altran.sbc.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

import com.altran.sbc.model.BaseModel;
import com.altran.sbc.model.User;

public class UsersDao {
	private Connection con;
	public static final String SALT = "exchange-salt";

	public UsersDao() throws ClassNotFoundException, SQLException {
		Connection con = ConnectionProvider.getInstance();
		this.con = con;
	}

	public BaseModel register(User user) throws NoSuchAlgorithmException {
		try {
			String QUERY = "select * from users where username = '" + user.getUsername() + "'";
			PreparedStatement preparedStatement = con.prepareStatement(QUERY);
			ResultSet rs = preparedStatement.executeQuery();
			boolean item = rs.next();
			if (item) {
				return new BaseModel("USERNAME ALREADY REGISTERD", 21);
			} else {
				String saltedPassword = SALT + user.getPassword();
				String hashedPassword = generateHash(saltedPassword);
				PreparedStatement stmt = con.prepareStatement(
						"insert into users(name,username,password,btcbalance,usdbalance) values(?,?,?,?,?)");
				stmt.setString(1, user.getName());
				stmt.setString(2, user.getUsername());
				stmt.setString(3, hashedPassword);
				stmt.setInt(4, Integer.valueOf(0));
				stmt.setInt(5, Integer.valueOf(0));
				int i = stmt.executeUpdate();
				return new BaseModel("REGISTER SUCCESS", 22);
			}
		} catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
			return new BaseModel("CONNECTION FAILED", 23);
		}
	}

	public BaseModel login(User user) throws Exception {
		String saltedPassword = SALT + user.getPassword();
		String hashedPassword = generateHash(saltedPassword);

		String QUERY = "select * from users where username = '" + user.getUsername() + "'";
		PreparedStatement preparedStatement = con.prepareStatement(QUERY);
		ResultSet rs = preparedStatement.executeQuery();
		boolean item = rs.next();
		if (!item) {
			return new BaseModel("USERNAME NOT FOUND", 24);
		} else {
			String storedPasswordHash = rs.getString("password");
			if (hashedPassword.equals(storedPasswordHash)) {
				// updat token
				String token = generatetoken(user.getUsername());
				String sql = "update users set token='" + token + "' where username='" + user.getUsername() + "'";
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
				System.out.println("Token updated");
				BaseModel bm = new BaseModel("SUCCESS", 25, user);
				bm.setToken(token);
				return bm;
			} else {
				return new BaseModel("WRONG USRNAME/PASSWORD", 26);
			}

		}
	}

	private static String generateHash(String input) throws NoSuchAlgorithmException {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException();
		}

		return hash.toString();
	}

	private static String generatetoken(String username) {
		UUID uuid = UUID.randomUUID();
		return username + ":" + uuid.toString();
	}

}
