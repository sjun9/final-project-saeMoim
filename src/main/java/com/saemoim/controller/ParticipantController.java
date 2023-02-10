package com.saemoim.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ParticipantController {
	// 모임 탈퇴
	@DeleteMapping("/group/participant/{participantId}")
	public StatusResponseDto withdrawGroup(@PathVariable Long participantId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
