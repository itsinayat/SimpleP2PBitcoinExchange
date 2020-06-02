package com.altran.sbc.dao;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.altran.sbc.model.BaseModel;
import com.altran.sbc.model.OrdersRequest;

public class OrdersDao {
	UsersDao usersDao;

	public OrdersDao() throws ClassNotFoundException, SQLException {
		UsersDao usersDao = new UsersDao();
		this.usersDao = usersDao;
	}

	// one instance, reuse
	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	// IF BUY BTC deduct USD add BTC from Buyers account and deduct BTC and add USD
	// in seller account
	public void executeOrder(OrdersRequest or, Double btcPrice) throws SQLException {
		if (or.getCoin().equalsIgnoreCase("BTC")) {
			// ADD BTC to buyer account & substract USD from buyer account
			Double existingbalanceBTCBuyer = Double.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), "BTC"));
			Double updated_btc_balance_buyer = existingbalanceBTCBuyer + or.getAmount();

			// SUBSTRACT USD from buyer account
			Double totalUSDOrdervalue = btcPrice * or.getAmount();
			Double existingbalanceUSD = Double.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), "USD"));
			Double updated_usd_balanceBuyer = existingbalanceUSD - totalUSDOrdervalue;

			// DEDUCT BTC from seller
			Double existingbalanceBTCSeller = Double.valueOf(usersDao.findBalanceByUserName(or.getSeller(), "BTC"));
			Double updated_btc_balance_seller = existingbalanceBTCSeller - or.getAmount();

			// ADD USD to seller account
			Double existingbalanceUSDSeller = Double.valueOf(usersDao.findBalanceByUserName(or.getSeller(), "USD"));
			Double updated_usd_balanceSeller = existingbalanceUSDSeller + totalUSDOrdervalue;

			// updating BUYER
			String sqlUpdate = "UPDATE users SET btcBalance = ? , usdBalance = ? WHERE username = ?";
			Connection conn = ConnectionProvider.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
			pstmt.setDouble(1, updated_btc_balance_buyer);
			pstmt.setDouble(2, updated_usd_balanceBuyer);
			pstmt.setString(3, or.getBuyer());
			int rowAffected = pstmt.executeUpdate();
			if (rowAffected > 0) {
				System.out.println("success");
			}

			// updating SELLER
			String sqlUpdate1 = "UPDATE users SET btcBalance = ? , usdBalance = ? WHERE username = ?";
			Connection conn1 = ConnectionProvider.getInstance();
			PreparedStatement pstmt1 = conn1.prepareStatement(sqlUpdate1);
			pstmt1.setDouble(1, updated_btc_balance_seller);
			pstmt1.setDouble(2, updated_usd_balanceSeller);
			pstmt1.setString(3, or.getSeller());
			int rowAffected1 = pstmt1.executeUpdate();
			if (rowAffected1 > 0) {
				System.out.println("success");
			}
			//UPDATE ORDER
			String sqlUpdate2 = "UPDATE orders SET status = ? where id = ?";
			Connection conn2 = ConnectionProvider.getInstance();
			PreparedStatement pstmt2 = conn2.prepareStatement(sqlUpdate2);
			pstmt2.setString(1, "SUCCESS");
			pstmt2.setInt(2, Integer.valueOf(or.getOrderId()));
			int rowAffected2 = pstmt2.executeUpdate();
			if (rowAffected2 > 0) {
				System.out.println("success");
			}

		} else if (or.getCoin().equalsIgnoreCase("USD")) {
			// ADD USD to buyer account
			Double existingbalanceUSDBuyer = Double.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), "USD"));
			Double updated_usd_balance_buyer = existingbalanceUSDBuyer + or.getAmount();

			// SUBSTRACT BTC from buyer account
			Double totalBTCOrdervalue = (1 / btcPrice) * or.getAmount();
			Double existingbalanceBTC = Double.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), "BTC"));
			Double updated_btc_balance_buyer = existingbalanceBTC - totalBTCOrdervalue;

			// DEDUCT USD from seller
			Double existingbalanceUSDSeller = Double.valueOf(usersDao.findBalanceByUserName(or.getSeller(), "USD"));
			Double updated_usd_balance_seller = existingbalanceUSDSeller - or.getAmount();

			// ADD BTC to seller account
			Double existingbalanceBTCSeller = Double.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), "BTC"));
			Double updated_btc_balanceSeller = existingbalanceBTCSeller + totalBTCOrdervalue;

			// updating BUYER
			String sqlUpdate = "UPDATE users SET btcBalance = ? , usdBalance = ? WHERE username = ?";
			Connection conn = ConnectionProvider.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
			pstmt.setDouble(1, updated_btc_balance_buyer);
			pstmt.setDouble(2, updated_usd_balance_buyer);
			pstmt.setString(3, or.getBuyer());
			int rowAffected = pstmt.executeUpdate();
			if (rowAffected > 0) {
				System.out.println("success");
			}

			// updating SELLER
			String sqlUpdate1 = "UPDATE users SET btcBalance = ? , usdBalance = ? WHERE username = ?";
			Connection conn1 = ConnectionProvider.getInstance();
			PreparedStatement pstmt1 = conn1.prepareStatement(sqlUpdate1);
			pstmt1.setDouble(1, updated_btc_balanceSeller);
			pstmt1.setDouble(2, updated_usd_balance_seller);
			pstmt1.setString(3, or.getSeller());
			int rowAffected1 = pstmt1.executeUpdate();
			if (rowAffected1 > 0) {
				System.out.println("success");
			}
			
			//UPDATE ORDER
			String sqlUpdate2 = "UPDATE orders SET status = ? where id = ?";
			Connection conn2 = ConnectionProvider.getInstance();
			PreparedStatement pstmt2 = conn2.prepareStatement(sqlUpdate2);
			pstmt1.setString(1, "SUCCESS");
			pstmt1.setInt(2, Integer.valueOf(or.getOrderId()));
			int rowAffected2 = pstmt2.executeUpdate();
			if (rowAffected2 > 0) {
				System.out.println("success");
			}

		}

	}

	public BaseModel placeOrder(OrdersRequest or) throws SQLException {
		HttpRequest request = HttpRequest.newBuilder().GET()
				.uri(URI.create("https://blockchain.info/tobtc?currency=USD&value=1"))
				.setHeader("User-Agent", "Java 8 HttpClient Bot").build();
		HttpResponse<String> response = null;
		Double btcValue = null;
		try {
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			btcValue = Double.valueOf(response.body());
		} catch (Exception e) {
			return new BaseModel("HTTP connection ERROR", 345);
		}

		// updating SELLER
		String sqlUpdate1 = "INSERT INTO orders(type, amount, price, username, status,coin) VALUES(?, ?, ?, ?, ?,?);";
		Connection conn1 = ConnectionProvider.getInstance();
		PreparedStatement pstmt1 = conn1.prepareStatement(sqlUpdate1);
		pstmt1.setString(1, or.getType());
		pstmt1.setDouble(2, or.getAmount());
		pstmt1.setDouble(3, (1/btcValue));
		if (or.getType().equalsIgnoreCase("BUY")) {
			pstmt1.setString(4, or.getBuyer());
		} else if (or.getType().equalsIgnoreCase("SELL")) {
			pstmt1.setString(4, or.getSeller());
		}
		pstmt1.setString(5, "PENDING");
		pstmt1.setString(6, or.getCoin());
		int rowAffected1 = pstmt1.executeUpdate();
		if (rowAffected1 > 0) {
			System.out.println("success");
			return new BaseModel("SUCCESS", 345);
		}
		return new BaseModel("ERROR", 345);
	}

}
