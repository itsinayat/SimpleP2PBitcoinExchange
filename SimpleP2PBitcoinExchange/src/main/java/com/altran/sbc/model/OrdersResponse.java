package com.altran.sbc.model;

public class OrdersResponse {
	public OrdersResponse(int id, String type, Double amount, Double price, String username, String status,String coin) {
		super();
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.price = price;
		this.username = username;
		this.status = status;
		this.coin = coin;
	}
	String coin;
	int id;
	String type;
	Double amount;
	Double price;
	String username;
	String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = coin;
	}

}
