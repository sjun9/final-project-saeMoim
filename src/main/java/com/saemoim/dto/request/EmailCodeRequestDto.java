package com.saemoim.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailCodeRequestDto {
	private String email;
	private String code;

	public EmailCodeRequestDto(String email, String code) {
		this.email = email;
		this.code = code;
	}
}
