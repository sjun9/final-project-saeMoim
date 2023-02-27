package com.saemoim.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequestDto {
	@Size(min = 2, max = 500)
	private String content;

	@Builder
	public ReportRequestDto(String content) {
		this.content = content;
	}
}
