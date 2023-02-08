package com.saemoim.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportResponseDto {
	private Long id;
	private Long subjectUserId;
	private String subjectUsername;
	private String reporter;
	private String content;
}
