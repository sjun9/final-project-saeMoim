package com.saemoim.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {
	@NotBlank
	private String title;
	@Size(min = 2, max = 500)
	private String content;

}
