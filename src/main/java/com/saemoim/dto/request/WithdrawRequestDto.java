package com.saemoim.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawRequestDto {
	@Size(min = 8, max = 15, message = "8자 이상 15자 이하여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()])[A-Za-z0-9!@#$%^&*()]*$", message = "영문 대/소문자, 숫자, 특수문자(!@#$%^&*())를 혼합 해주세요.")
	private String password;

	@Builder
	public WithdrawRequestDto(String password) {
		this.password = password;
	}
}
