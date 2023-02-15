package com.saemoim.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawRequestDto {
	private String password;

	@Builder
	public WithdrawRequestDto(String password) {
		this.password = password;
	}
}
