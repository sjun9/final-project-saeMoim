package com.saemoim.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequestDto {
	@Email
	private String email;

	@Builder
	public EmailRequestDto(String email) {
		this.email = email;
	}
}
