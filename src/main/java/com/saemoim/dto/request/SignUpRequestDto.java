package com.saemoim.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
	@Email
	@NotBlank
	private String email;

	@Size(min = 8, max = 15, message = "최소 8자, 최대 15글자까지 입력 가능합니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()])[A-Za-z0-9!@#$%^&*()]*$" , message = "영문, 숫자, 특수문자를 혼합 해주세요.")
	private String password;

	@Size(min = 2, max = 15, message = "최소 2자, 최대 15글자까지 입력 가능합니다.")
	private String username;
}
