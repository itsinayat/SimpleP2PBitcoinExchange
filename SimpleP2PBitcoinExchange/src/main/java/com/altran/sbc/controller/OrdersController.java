package com.altran.sbc.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.altran.sbc.model.BaseModel;
import com.altran.sbc.model.OrdersRequest;
import com.altran.sbc.service.OrderService;

@Path("/secure")
public class OrdersController {

	OrderService orderService;;

	public OrdersController() throws ClassNotFoundException, SQLException {
		OrderService orderService = new OrderService();
		this.orderService = orderService;
	}

	@POST
	@Path("/executeOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response executeOrder(OrdersRequest or) throws IOException, InterruptedException, SQLException {
		BaseModel result  = orderService.executeOrder(or);
		return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("/placeOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response placeOrder(OrdersRequest or) throws IOException, InterruptedException, SQLException {
		BaseModel result  = orderService.placeOrder(or);
		return Response.status(200).entity(result).build();
	}
}
