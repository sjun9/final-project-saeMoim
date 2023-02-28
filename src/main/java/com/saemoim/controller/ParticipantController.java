package com.saemoim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.ParticipantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

	private final ParticipantService participantService;

	// 특정 모임의 참여자 조회
	@GetMapping("/participant/groups/{groupId}")
	public ResponseEntity<GenericsResponseDto> getParticipants(@PathVariable Long groupId) {
		return ResponseEntity.ok().body(new GenericsResponseDto(participantService.getParticipants(groupId)));
	}

	// 모임 탈퇴
	@DeleteMapping("/participant/groups/{groupId}")
	public ResponseEntity<GenericsResponseDto> withdrawGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		participantService.withdrawGroup(groupId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("모임 탈퇴가 완료 되었습니다."));
	}
}
