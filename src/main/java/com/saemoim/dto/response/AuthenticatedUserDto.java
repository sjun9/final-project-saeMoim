package com.saemoim.dto.response;

import com.saemoim.domain.enums.UserRoleEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthenticatedUserDto {

	private String email;
	private UserRoleEnum role;
	public AuthenticatedUserDto(String email, UserRoleEnum role) {
		this.email = email;
		this.role = role;
	}
}
