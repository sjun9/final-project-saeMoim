package com.saemoim.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saemoim.exception.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse,
		AuthenticationException ex) throws IOException, ServletException {

		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.UNAUTHORIZED);
		response.put("message", ErrorCode.INVALID_TOKEN.getMessage());

		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		OutputStream out = httpServletResponse.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
		out.flush();
	}
}
