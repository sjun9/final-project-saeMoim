package com.saemoim.annotation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.saemoim.security.UserDetailsImpl;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
	@Override
	public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
		final SecurityContext context = SecurityContextHolder.createEmptyContext();
		UserDetailsImpl userDetails = new UserDetailsImpl(annotation.id(), annotation.username(), annotation.role());
		final Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null,
			userDetails.getAuthorities());
		context.setAuthentication(auth);
		return context;
	}
}
