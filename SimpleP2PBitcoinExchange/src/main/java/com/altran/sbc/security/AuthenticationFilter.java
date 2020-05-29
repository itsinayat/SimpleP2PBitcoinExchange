package com.altran.sbc.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.altran.sbc.dao.ConnectionProvider;
import com.altran.sbc.model.BaseModel;

public class AuthenticationFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
		System.out.println();
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Authenticating...");
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
		String token = httpRequest.getHeader("Authorization");
		if (token == null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(httpServletResponse.getWriter(), new BaseModel("Authorization header missing", 403));
		}
		String[] x = token.split(":");
		String username = x[0];
		Connection con = null;
		try {
			con = ConnectionProvider.getInstance();
			String QUERY = "select * from users where username ='" + username + "' and token = '" + token + "'";
			PreparedStatement preparedStatement = con.prepareStatement(QUERY);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				chain.doFilter(req, resp);
			} else {
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(httpServletResponse.getWriter(), new BaseModel("AUTHENTICATION FAILED", 403));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void destroy() {
	}
}