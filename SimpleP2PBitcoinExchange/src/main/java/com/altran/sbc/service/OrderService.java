package com.altran.sbc.service;

import java.io.IOException;
import java.sql.SQLException;

import com.altran.sbc.dao.OrdersDao;
import com.altran.sbc.dao.UsersDao;
import com.altran.sbc.model.BaseModel;
import com.altran.sbc.model.CurrencyType;
import com.altran.sbc.model.OrdersRequest;

public class OrderService {
	UsersDao usersDao;
	OrdersDao ordersDao;

	public OrderService() throws ClassNotFoundException, SQLException {
		UsersDao usersDao = new UsersDao();
		OrdersDao ordersDao = new OrdersDao();
		this.usersDao = usersDao;
		this.ordersDao = ordersDao;
	}


	public BaseModel executeOrder(OrdersRequest or) throws IOException, InterruptedException, SQLException {
		
		Double btcValue = 1/or.getPrice();
		Double usdValue = or.getPrice();
		Double amount = or.getAmount();
		System.out.println("USD VALUE " + usdValue);
		System.out.println("BTC VALUE " + btcValue);

			if (or.getCoin().equalsIgnoreCase(CurrencyType.BTC.getCurrencyname())) {
				Double usdRequirement = amount * usdValue;
				Double usd_balance_buyer = Double.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), CurrencyType.USD.getCurrencyname()));
				Double btc_balance_seller = Double.valueOf(usersDao.findBalanceByUserName(or.getSeller(), CurrencyType.BTC.getCurrencyname()));
				if (usd_balance_buyer < usdRequirement) {
					return new BaseModel("NOT ENOUGH USD BALANCE TO BUY", 301);
				}else if(btc_balance_seller < or.getAmount()) {
					return new BaseModel("NOT ENOUGH BTC BALANCE TO SELL", 301);
				}else {
					Boolean result =ordersDao.executeOrder(or, usdValue);
					if(result.equals(true)) {
						return new BaseModel("SUCCESS", 302);
					}else {
						return new BaseModel("FAILED", 302);
					}
					
				}
				
			} else if (or.getCoin().equalsIgnoreCase(CurrencyType.USD.getCurrencyname())) {
				Double btcRequirement = amount * btcValue;
				System.out.println(btcRequirement);
				Double btc_balance_buyer = Double.valueOf(usersDao.findBalanceByUserName(or.getBuyer(), CurrencyType.BTC.getCurrencyname()));
				Double usd_balance_seller = Double.valueOf(usersDao.findBalanceByUserName(or.getSeller(), CurrencyType.USD.getCurrencyname()));
				if (btc_balance_buyer < btcRequirement) {
					return new BaseModel("NOT ENOUGH BTC BALANCE TO BUY", 301);
				} else if (usd_balance_seller <  or.getAmount()){
					return new BaseModel("NOT ENOUGH USD BALANCE TO BUY", 301);
				}else {
					Boolean result = 	ordersDao.executeOrder(or, usdValue);
					if(result.equals(true)) {
						return new BaseModel("SUCCESS", 302);
					}else {
						return new BaseModel("FAILED", 302);
					}
				}

			}else {
				 return new BaseModel("WRONG COIN", 302);
				 
			}
	}



	public BaseModel placeOrder(OrdersRequest or) throws SQLException {
		BaseModel model  = ordersDao.placeOrder(or);
		return model;
	}

}
