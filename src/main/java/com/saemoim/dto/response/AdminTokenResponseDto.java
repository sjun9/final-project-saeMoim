package com.saemoim.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminTokenResponseDto extends TokenResponseDto {
	private String accessToken;

	public AdminTokenResponseDto(String accessToken) {
		this.accessToken = accessToken;
	}
}
