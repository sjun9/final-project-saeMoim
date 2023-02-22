package com.saemoim.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequestDto {

	@Size(min = 8, max = 15, message = "8자 이상 15자 이하여야 합니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()])[A-Za-z0-9!@#$%^&*()]*$", message = "알파벳 대소문자, 숫자, 특수문자(!@#$%^&*())가 모두 포함되어야 합니다.")
	private String password;
	@NotBlank
	private String content;

}
