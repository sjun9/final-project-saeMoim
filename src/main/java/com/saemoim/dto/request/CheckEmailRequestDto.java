package com.saemoim.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckEmailRequestDto {
	private String email;

	@Builder
	public CheckEmailRequestDto(String email) {
		this.email = email;
	}
}
