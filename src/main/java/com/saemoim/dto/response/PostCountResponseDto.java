package com.saemoim.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCountResponseDto {
	private Long postCount;

	public PostCountResponseDto(Long postCount) {
		this.postCount = postCount;
	}
}
