package com.saemoim.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;

public interface ReportService {

	Page<ReportResponseDto> getReportedUsers(Pageable pageable);

	void reportUser(Long subjectUserId, ReportRequestDto requestDto, String reporterName);

	void deleteReport(Long reportId);
}
