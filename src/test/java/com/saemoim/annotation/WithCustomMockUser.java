package com.saemoim.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

import com.saemoim.domain.enums.UserRoleEnum;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {
	long id() default 1L;

	String username() default "test";

	UserRoleEnum role() default UserRoleEnum.USER;
}
