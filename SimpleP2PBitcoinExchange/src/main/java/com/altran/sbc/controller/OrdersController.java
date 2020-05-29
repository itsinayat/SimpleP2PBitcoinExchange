package com.altran.sbc.controller;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.altran.sbc.service.UsersService;

@Path("/secure")
public class OrdersController {

	UsersService usersService;;

	public OrdersController() throws ClassNotFoundException, SQLException {
		UsersService usersService = new UsersService();
		this.usersService = usersService;
	}

	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String remove() {
		return "WELCOME";
	}



}
