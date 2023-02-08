package com.saemoim.dto.response;

import com.saemoim.domain.enums.UserRoleEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
	private Long id;
	private String username;
	private int reportCount;
	private UserRoleEnum role;
}
