package com.altran.sbc.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.altran.sbc.model.BaseModel;
import com.altran.sbc.model.OrdersResponse;
import com.altran.sbc.model.User;
import com.altran.sbc.service.UsersService;

@Path("/users")
public class UsersController {
	UsersService usersService;;

	public UsersController() throws ClassNotFoundException, SQLException {
		UsersService usersService = new UsersService();
		this.usersService = usersService;
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(User user) throws ClassNotFoundException, NoSuchAlgorithmException {
		BaseModel resp = usersService.register(user);
		return Response.status(200).entity(resp).build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user) throws Exception {
		BaseModel resp = usersService.login(user);
		return Response.status(200).entity(resp).build();
	}
	
	@GET
	@Path("/getBalance/{id}/{coin}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getBalance(@PathParam("coin") String coin,@PathParam("id") String id) throws Exception {
		String balance = usersService.getBalance(coin,id);
		return Response.status(200).entity(balance).build();
	}
	
	@GET
	@Path("/getOrderTable")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderTable(@PathParam("coin") String coin,@PathParam("id") String id) throws Exception {
		List<OrdersResponse> response = usersService.getOrderTable();
		return Response.status(200).entity(response).build();
	}
	
	

}