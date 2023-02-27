package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

	private final ApplicationService applicationService;

	// 참가자가 신청한 모임내역 조회
	@GetMapping("/participant/application")
	public ResponseEntity<GenericsResponseDto> getMyApplications(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok()
			.body(new GenericsResponseDto(applicationService.getMyApplications(userDetails.getId())));
	}

	// 참가자가 모임 신청
	@PostMapping("/groups/{groupId}/application")
	public ResponseEntity<GenericsResponseDto> applyGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		applicationService.applyGroup(groupId, userDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericsResponseDto("모임 신청이 완료 되었습니다."));
	}

	// 참가자가 모임 신청 취소
	@DeleteMapping("applications/{applicationId}")
	public ResponseEntity<GenericsResponseDto> cancelApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		applicationService.deleteApplication(applicationId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("모임 신청 취소가 완료되었습니다."));
	}

	// 리더가 신청받은 모임내역 조회
	@GetMapping("/leader/application")
	public ResponseEntity<GenericsResponseDto> getApplications(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok()
			.body(new GenericsResponseDto(applicationService.getApplications(userDetails.getId())));
	}

	// 리더가 모임 신청 승인
	@PutMapping("/applications/{applicationId}/permit")
	public ResponseEntity<GenericsResponseDto> permitApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		applicationService.permitApplication(applicationId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("신청 승인이 완료 되었습니다."));
	}

	// 리더가 모임 신청 거절
	@PutMapping("/applications/{applicationId}/reject")
	public ResponseEntity<GenericsResponseDto> rejectApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		applicationService.rejectApplication(applicationId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("신청 거절이 완료 되었습니다."));
	}

}
