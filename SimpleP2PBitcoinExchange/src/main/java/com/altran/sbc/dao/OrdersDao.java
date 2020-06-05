package com.altran.sbc.dao;

import java.io.ObjectInputFilter.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.altran.sbc.model.BaseModel;
import com.altran.sbc.model.CurrencyType;
import com.altran.sbc.model.OrdersRequest;
import com.altran.sbc.model.OrdersResponse;

/**
 * @author GUR56851
 *
 */
public class OrdersDao {
	UsersDao usersDao;

	public OrdersDao() throws ClassNotFoundException, SQLException {
		UsersDao usersDao = new UsersDao();
		this.usersDao = usersDao;
	}

	// one instance, reuse

	// IF BUY BTC deduct USD add BTC from Buyers account and deduct BTC and add USD
	// in seller account
	/**
	 * @param or
	 * @param btcPrice
	 * @throws SQLException
	 */
	public Boolean executeOrder(OrdersRequest or, Double btcPrice) throws SQLException {
		if (orderAlreadyFulfilled(or)) {
			System.out.println("ORDER ALREADY EXECUTED");
			return false;
		}

		if (or.getCoin().equalsIgnoreCase(CurrencyType.BTC.getCurrencyname())) {
			// ADD BTC to buyer account & substract USD from buyer account
			Double existingbalanceBTCBuyer = Double
					.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), CurrencyType.BTC.getCurrencyname()));
			Double updatedBTCBalanceBuyer = existingbalanceBTCBuyer + or.getAmount();

			// SUBSTRACT USD from buyer account
			Double totalUSDOrdervalue = btcPrice * or.getAmount();
			Double existingbalanceUSD = Double
					.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), CurrencyType.USD.getCurrencyname()));
			Double updatedUSDBalanceBuyer = existingbalanceUSD - totalUSDOrdervalue;

			// DEDUCT BTC from seller
			Double existingbalanceBTCSeller = Double
					.valueOf(usersDao.findBalanceByUserName(or.getSeller(), CurrencyType.BTC.getCurrencyname()));
			Double updatedBTCBalanceSeller = existingbalanceBTCSeller - or.getAmount();

			// ADD USD to seller account
			Double existingbalanceUSDSeller = Double
					.valueOf(usersDao.findBalanceByUserName(or.getSeller(), CurrencyType.USD.getCurrencyname()));
			Double updatedUSDBalanceSeller = existingbalanceUSDSeller + totalUSDOrdervalue;
			updateBuyer(updatedBTCBalanceBuyer, updatedUSDBalanceBuyer, or);
			updateSeller(updatedBTCBalanceSeller, updatedUSDBalanceSeller, or);
			updateOrder(or);

		} else if (or.getCoin().equalsIgnoreCase(CurrencyType.USD.getCurrencyname())) {
			// ADD USD to buyer account
			Double existingbalanceUSDBuyer = Double
					.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), CurrencyType.USD.getCurrencyname()));
			Double updatedUSDBalanceBuyer = existingbalanceUSDBuyer + or.getAmount();

			// SUBSTRACT BTC from buyer account
			Double totalBTCOrdervalue = (1 / btcPrice) * or.getAmount();
			Double existingbalanceBTC = Double
					.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), CurrencyType.BTC.getCurrencyname()));
			Double updatedBTCBalanceBuyer = existingbalanceBTC - totalBTCOrdervalue;

			// DEDUCT USD from seller
			Double existingbalanceUSDSeller = Double
					.valueOf(usersDao.findBalanceByUserName(or.getSeller(), CurrencyType.USD.getCurrencyname()));
			Double updatedUSDBalanceSeller = existingbalanceUSDSeller - or.getAmount();

			// ADD BTC to seller account
			Double existingbalanceBTCSeller = Double
					.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), CurrencyType.BTC.getCurrencyname()));
			Double updatedBTCBalanceSeller = existingbalanceBTCSeller + totalBTCOrdervalue;

			updateBuyer(updatedBTCBalanceBuyer, updatedUSDBalanceBuyer, or);

			updateSeller(updatedBTCBalanceSeller, updatedUSDBalanceSeller, or);

			updateOrder(or);

		}
		return true;

	}

	private boolean orderAlreadyFulfilled(OrdersRequest or) {
		try {
			String QUERY = "select * from orders where id=" + or.getOrderId();
			Connection conn = ConnectionProvider.getInstance();
			PreparedStatement preparedStatement = conn.prepareStatement(QUERY);
			ResultSet rs = preparedStatement.executeQuery();
			String status = null;
			if (rs.next()) {
				status = rs.getString("status");
			}
			if (status.equals("SUCCESS")) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * @param updatedBTCBalanceBuyer
	 * @param updatedUSDBalanceBuyer
	 * @param or
	 */
	public void updateBuyer(Double updatedBTCBalanceBuyer, Double updatedUSDBalanceBuyer, OrdersRequest or) {
		try {
			// updating BUYER
			String sqlUpdate = "UPDATE users SET btcBalance = ? , usdBalance = ? WHERE username = ?";
			Connection conn = ConnectionProvider.getInstance();
			PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
			pstmt.setDouble(1, updatedBTCBalanceBuyer);
			pstmt.setDouble(2, updatedUSDBalanceBuyer);
			pstmt.setString(3, or.getBuyer());
			int rowAffected = pstmt.executeUpdate();
			if (rowAffected > 0) {
				System.out.println("success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param updatedBTCBalanceSeller
	 * @param updatedUSDBalanceSeller
	 * @param or
	 */
	public void updateSeller(Double updatedBTCBalanceSeller, Double updatedUSDBalanceSeller, OrdersRequest or) {
		// updating SELLER
		try {
			String sqlUpdate1 = "UPDATE users SET btcBalance = ? , usdBalance = ? WHERE username = ?";
			Connection conn1 = ConnectionProvider.getInstance();
			PreparedStatement pstmt1 = conn1.prepareStatement(sqlUpdate1);
			pstmt1.setDouble(1, updatedBTCBalanceSeller);
			pstmt1.setDouble(2, updatedUSDBalanceSeller);
			pstmt1.setString(3, or.getSeller());
			int rowAffected1 = pstmt1.executeUpdate();
			if (rowAffected1 > 0) {
				System.out.println("success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param or
	 */
	public void updateOrder(OrdersRequest or) {
		// UPDATE ORDER
		try {
			String sqlUpdate2 = "UPDATE orders SET status = ? where id = ?";
			Connection conn2 = ConnectionProvider.getInstance();
			PreparedStatement pstmt2 = conn2.prepareStatement(sqlUpdate2);
			pstmt2.setString(1, "SUCCESS");
			pstmt2.setInt(2, Integer.valueOf(or.getOrderId()));
			int rowAffected2 = pstmt2.executeUpdate();
			if (rowAffected2 > 0) {
				System.out.println("success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param or
	 * @return
	 * @throws SQLException
	 */
	public BaseModel placeOrder(OrdersRequest or) throws SQLException {
		HttpGet request = new HttpGet("https://blockchain.info/tobtc?currency=USD&value=1");
		Double btcValue = null;
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
				btcValue = Double.valueOf(result);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (updatingSeller(or, btcValue).equals("success")) {
			System.out.println("success");
			return new BaseModel("SUCCESS", 345);
		} else
			return new BaseModel("ERROR", 345);
	}

	/**
	 * @param or
	 * @param btcValue
	 * @return
	 */
	public String updatingSeller(OrdersRequest or, Double btcValue) {
		try {
			// updating SELLER
			String sqlUpdate1 = "INSERT INTO orders(type, amount, price, username, status,coin) VALUES(?, ?, ?, ?, ?,?);";
			Connection conn1 = ConnectionProvider.getInstance();
			PreparedStatement pstmt1 = conn1.prepareStatement(sqlUpdate1);
			pstmt1.setString(1, or.getType());
			pstmt1.setDouble(2, or.getAmount());
			pstmt1.setDouble(3, (1 / btcValue));
			if (or.getType().equalsIgnoreCase("BUY")) {
				pstmt1.setString(4, or.getBuyer());
			} else if (or.getType().equalsIgnoreCase("SELL")) {
				pstmt1.setString(4, or.getSeller());
			}
			pstmt1.setString(5, "PENDING");
			pstmt1.setString(6, or.getCoin());
			int rowAffected1 = pstmt1.executeUpdate();
			if (rowAffected1 > 0) {
				return "success";
			}
		} catch (Exception e) {
			return "error";
		}
		return "failed";

	}

}
