package com.altran.sbc.dao;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.altran.sbc.model.User;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConnectionProvider.class })
public class UsersDAOTest {
	@Mock
	private UsersDao usersDAO;

	@Mock
	Connection con;
	
	@Mock
	PreparedStatement preparedStatement;

	@Mock
	ResultSet resultSet;
	
	@Mock
	User user;


	@Test
	public void loginTest()throws Exception {
		user.setId(56851L);
		user.setPassword("WeRG123");
		user.setName("Parul");
		PowerMockito.mockStatic(ConnectionProvider.class);
		PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
		PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
		PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		MockitoAnnotations.initMocks(this);  
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		usersDAO.login(user);
		
	}
	
	@Test
	public void findBalanceByUserNameUSDTest()throws Exception {
		PowerMockito.mockStatic(ConnectionProvider.class);
		PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
		PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
		PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		MockitoAnnotations.initMocks(this);  
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		usersDAO.findBalanceByUserName("Parul","USD");
	}
	@Test
	public void findBalanceByUserNameBTCTest()throws Exception {
		PowerMockito.mockStatic(ConnectionProvider.class);
		PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
		PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
		PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		MockitoAnnotations.initMocks(this);  
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		usersDAO.findBalanceByUserName("Parul","BTC");
	}
	@Test
	public void findBalanceByUserNameWrongCOinTest()throws Exception {
		PowerMockito.mockStatic(ConnectionProvider.class);
		PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
		PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
		PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		MockitoAnnotations.initMocks(this);  
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		usersDAO.findBalanceByUserName("Parul","YHGFC");
	}
	@Test
	public void findBalanceByUserNameNotFOundTest()throws Exception {
		PowerMockito.mockStatic(ConnectionProvider.class);
		PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
		PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
		PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		MockitoAnnotations.initMocks(this);  
		usersDAO.findBalanceByUserName("XYZ","YHGFC");
	}


}
