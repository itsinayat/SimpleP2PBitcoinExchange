package com.altran.sbc.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;

import com.altran.sbc.dao.UsersDao;
import com.altran.sbc.model.BaseModel;
import com.altran.sbc.model.OrdersResponse;
import com.altran.sbc.model.User;

public class UsersService {
	UsersDao dao;

	public UsersService() throws ClassNotFoundException, SQLException {
		this.dao = new UsersDao();
	}

	public BaseModel register(User user) throws NoSuchAlgorithmException {
		BaseModel response = dao.register(user);
		return response;

	}

	public BaseModel login(User user) throws Exception {
		BaseModel response = dao.login(user);
		return response;
	}

	public String getBalance(String coin, String username) throws SQLException {
		String bal = dao.findBalanceByUserName(username,coin);
		return bal;
	}

	public List<OrdersResponse> getOrderTable() throws SQLException {
		List<OrdersResponse> response = dao.getOrderTable();
		return response;
	}

}
