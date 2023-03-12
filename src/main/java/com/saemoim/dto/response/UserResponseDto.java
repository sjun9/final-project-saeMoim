package com.saemoim.dto.response;

import java.time.LocalDateTime;

import com.saemoim.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
	private Long id;
	private String username;
	private String email;
	private int banCount;
	private LocalDateTime createdAt;

	public UserResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.banCount = user.getBanCount();
		this.createdAt = user.getCreatedAt();
	}
}
