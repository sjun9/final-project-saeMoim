package com.saemoim.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {
	private Long id;
	private String username;
	private String content;
}
