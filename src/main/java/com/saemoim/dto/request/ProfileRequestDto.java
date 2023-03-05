package com.saemoim.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileRequestDto {

	@NotBlank
	private String content;

	@Builder
	public ProfileRequestDto(String content) {
		this.content = content;
	}
}
