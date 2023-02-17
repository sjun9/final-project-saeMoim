package com.saemoim.dto.response;

import com.saemoim.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {
	private Long id;
	private String username;
	private String content;

	public ProfileResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.content = user.getContent();
	}
}
