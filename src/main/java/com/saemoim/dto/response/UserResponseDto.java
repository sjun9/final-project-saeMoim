package com.saemoim.dto.response;

import com.saemoim.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
	private Long id;
	private String username;
	private String email;
	private Integer banCount;
	private String createdAt;

	public UserResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.banCount = user.getBanCount();
		this.createdAt = user.getCreatedAt().toString();
	}
}
