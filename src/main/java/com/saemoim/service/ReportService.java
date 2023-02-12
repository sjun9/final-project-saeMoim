package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface ReportService {

	List<ReportResponseDto> getReportedUsers();

	StatusResponseDto reportUser(Long subjectUserId, ReportRequestDto requestDto, String username);

}
