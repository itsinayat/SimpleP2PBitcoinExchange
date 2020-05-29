package com.altran.sbc.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.altran.sbc.model.BaseModel;
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

}