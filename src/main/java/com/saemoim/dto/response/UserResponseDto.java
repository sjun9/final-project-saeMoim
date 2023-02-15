package com.saemoim.dto.response;

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
	private Long id;
	private String username;
	private int banCount;
	private UserRoleEnum role;

	public UserResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.banCount = user.getBanCount();
		this.role = user.getRole();
	}
}
