package com.saemoim.dto.request;

import jakarta.validation.constraints.Email;
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
	// @Pattern()
	// 이메일 인증 기능은 추후에, 일단 돌아가는 것부터 확인

	private String email;
	private String password;
	// 닉네임 임.
	private String username;


}
