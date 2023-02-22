package com.saemoim.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UsernameRequestDto {
	@NotBlank
	private String username;

	@Builder
	public UsernameRequestDto(String username) {
		this.username = username;
	}
}
