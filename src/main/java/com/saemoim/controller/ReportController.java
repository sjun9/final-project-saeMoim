package com.saemoim.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.ReportServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReportController {
	private final ReportServiceImpl reportService;

	// 관리자가 신고된 사용자 조회
	@GetMapping("/admin/report")
	public List<ReportResponseDto> getReportedUsers() {
		return reportService.getReportedUsers();
	}

	// 회원이 사용자 신고
	@PostMapping("/report/users/{subjectUserId}")
	public ResponseEntity<MessageResponseDto> reportUser(@PathVariable Long subjectUserId,
		@Validated @RequestBody ReportRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		reportService.reportUser(subjectUserId, requestDto, userDetails.getUsername());
		return new ResponseEntity<>(new MessageResponseDto("사용자 신고 완료"), HttpStatus.OK);
	}

	// 관리자가 신고내역 삭제
	@DeleteMapping("/admin/reports/{reportId}")
	public ResponseEntity<MessageResponseDto> deleteReport(@PathVariable Long reportId) {
		reportService.deleteReport(reportId);
		return new ResponseEntity<>(new MessageResponseDto("신고 삭제 완료"), HttpStatus.OK);
	}
}
