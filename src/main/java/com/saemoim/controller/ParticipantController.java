package com.saemoim.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ParticipantResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.ParticipantServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

	private final ParticipantServiceImpl participantServiceImpl;

	// 특정 모임의 참여자 조회
	@GetMapping("/group/{groupId}/participant")
	public List<ParticipantResponseDto> getParticipants(@PathVariable Long groupId) {
		return participantServiceImpl.getParticipants(groupId);
	}

	// 모임 탈퇴
	@DeleteMapping("/group/participant/{participantId}")
	public ResponseEntity<MessageResponseDto> withdrawGroup(@PathVariable Long participantId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
