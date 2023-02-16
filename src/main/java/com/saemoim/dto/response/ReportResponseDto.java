package com.saemoim.dto.response;

import com.saemoim.domain.Report;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportResponseDto {
	private Long id;
	private Long subjectUserId;
	private String subjectUsername;
	private String reporterName;
	private String content;

	@Builder
	public ReportResponseDto(Report report) {
		this.id = report.getId();
		this.subjectUserId = report.getSubjectId();
		this.subjectUsername = report.getSubjectUsername();
		this.reporterName = report.getReporterName();
		this.content = report.getContent();
	}
}
