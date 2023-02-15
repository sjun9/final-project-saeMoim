package com.saemoim.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminRequestDto {
	@Size(min = 2, max = 10)
	private String username;
	@Size(min = 8, max = 15)
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()])[A-Za-z0-9!@#$%^&*()]*$")
	private String password;

	@Builder
	public AdminRequestDto(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
