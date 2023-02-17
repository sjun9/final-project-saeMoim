package com.saemoim.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UsernameRequestDto {
	private String username;

	@Builder
	public UsernameRequestDto(String username) {
		this.username = username;
	}
}
