package com.saemoim.jwt;

import static com.saemoim.jwt.JwtUtil.*;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.exception.ExceptionResponseDto;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException, IOException {

		Optional<String> token = jwtUtil.resolveToken(request.getHeader(JwtUtil.AUTHORIZATION_HEADER));

		if (token.isPresent()) {
			if (jwtUtil.validateToken(token.get())) {
				Claims info = jwtUtil.getUserInfoFromToken(token.get());
				setAuthentication(Long.valueOf(info.getSubject()), String.valueOf(info.get(AUTHORIZATION_NAME)),
					UserRoleEnum.valueOf((String)info.get(AUTHORIZATION_KEY)));
			}
		}
		filterChain.doFilter(request, response);
	}

	public void setAuthentication(Long id, String username, UserRoleEnum role) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = jwtUtil.createAuthentication(id, username, role);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus statusCode) {
		response.setStatus(statusCode.value());
		response.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new ExceptionResponseDto(statusCode, msg));
			log.error(json);
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}