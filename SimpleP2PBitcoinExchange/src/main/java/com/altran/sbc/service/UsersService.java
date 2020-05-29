package com.altran.sbc.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.altran.sbc.dao.UsersDao;
import com.altran.sbc.model.BaseModel;
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

}
