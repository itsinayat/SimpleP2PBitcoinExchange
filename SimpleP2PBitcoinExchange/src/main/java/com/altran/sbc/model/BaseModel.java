package com.altran.sbc.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BaseModel {
	public BaseModel(String message, int code) {
		super();
		this.message = message;
		this.code = code;
	}

	public BaseModel(String message, int code, User user) {
		super();
		this.message = message;
		this.code = code;
		this.user = user;
	}

	private String message;
	private int code;
	private User user;
	private String token;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}
