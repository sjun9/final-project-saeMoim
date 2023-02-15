package com.saemoim.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailAuthCodeResponseDto {
	private String authCode;

	@Builder
	public EmailAuthCodeResponseDto(String authCode) {
		this.authCode = authCode;
	}
}
