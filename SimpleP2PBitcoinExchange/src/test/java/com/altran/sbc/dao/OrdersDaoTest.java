package com.altran.sbc.dao;


import java.sql.SQLException;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.altran.sbc.model.OrdersRequest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConnectionProvider.class })
public class OrdersDaoTest {

	@Mock
	private OrdersDao ordersDAO;
	
	@Mock
	private OrdersRequest ordersRequest;
	
	@Mock
	Connection con;
	
	@Mock
	OrdersRequest or;

	@Mock
	PreparedStatement preparedStatement;

	@Mock
	ResultSet resultSet;
	
	@Test
	public void updateBuyerTest() throws SQLException {
		PowerMockito.mockStatic(ConnectionProvider.class);
		PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
		PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
		PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		MockitoAnnotations.initMocks(this);  
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		Double updatedUSDBalanceBuyer=2000.00;
		Double updatedBTCBalanceBuyer=20.00;
		ordersDAO.updateBuyer(updatedBTCBalanceBuyer,updatedUSDBalanceBuyer,or);
	}

	@Test
	public void updateSellerTest() throws SQLException {
		PowerMockito.mockStatic(ConnectionProvider.class);
		PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
		PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
		PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		MockitoAnnotations.initMocks(this);  
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		Double updatedUSDBalanceBuyer=2000.00;
		Double updatedBTCBalanceBuyer=20.00;
		ordersDAO.updateSeller(updatedBTCBalanceBuyer,updatedUSDBalanceBuyer,or);
	}
	
	@Test
	public void updateOrderTest() throws SQLException
	{
	PowerMockito.mockStatic(ConnectionProvider.class);
	PowerMockito.when(ConnectionProvider.getInstance()).thenReturn(con);
	PowerMockito.when(con.prepareStatement(anyString())).thenReturn(preparedStatement);
	PowerMockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
	MockitoAnnotations.initMocks(this);  
	when(resultSet.next()).thenReturn(true).thenReturn(false);
	ordersDAO.updateOrder(or);
		
	}

	@Test
	public void executeOrderTest() throws SQLException {
		Double btcPrice=55.00;
		ordersRequest.setCoin("BTC");
		ordersDAO.executeOrder(ordersRequest,btcPrice);
	}
	@Test
	public void placeOrderTest() throws SQLException
	{
		ordersDAO.placeOrder(or);
	}
	@Test
	public void updatingSellerTest()
	{
		ordersDAO.updatingSeller(or,12.34);
	}


}
