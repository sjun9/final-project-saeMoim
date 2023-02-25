package com.saemoim.controller;

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
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReportController {
	private final ReportService reportService;

	// 관리자가 신고된 사용자 조회
	@GetMapping("/admin/report")
	public ResponseEntity<GenericsResponseDto> getReportedUsers() {
		return ResponseEntity.ok().body(new GenericsResponseDto(reportService.getReportedUsers()));
	}

	// 회원이 사용자 신고
	@PostMapping("/report/users/{subjectUserId}")
	public ResponseEntity<GenericsResponseDto> reportUser(@PathVariable Long subjectUserId,
		@Validated @RequestBody ReportRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		reportService.reportUser(subjectUserId, requestDto, userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericsResponseDto("사용자 신고가 완료 되었습니다."));
	}

	// 관리자가 신고내역 삭제
	@DeleteMapping("/admin/reports/{reportId}")
	public ResponseEntity<GenericsResponseDto> deleteReport(@PathVariable Long reportId) {
		reportService.deleteReport(reportId);
		return ResponseEntity.ok().body(new GenericsResponseDto("신고 삭제가 완료 되었습니다."));
	}
}
