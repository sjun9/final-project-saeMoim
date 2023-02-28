package com.saemoim.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {
	@Size(min = 2, max = 64)
	private String title;
	@Size(min = 2, max = 500)
	private String content;

	public PostRequestDto(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
