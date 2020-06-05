package com.altran.sbc.model;

public enum CurrencyType {
	BTC("BTC"),
	USD("USD");
	String currencyname;

	public String getCurrencyname() {
		return currencyname;
	}

	public void setCurrencyname(String currencyname) {
		this.currencyname = currencyname;
	}

	private CurrencyType(String currencyname) {
		this.currencyname = currencyname;
	}
}
