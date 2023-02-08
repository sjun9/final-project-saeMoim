package com.saemoim.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
	private Long id;
	private Long userId;
	private String username;
	private String comment;
}
