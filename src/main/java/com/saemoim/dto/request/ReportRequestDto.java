package com.saemoim.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequestDto {
	private String content;

	@Builder
	public ReportRequestDto(String content) {
		this.content = content;
	}
}
