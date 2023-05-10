package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.EventRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventController {
	private final EventService eventService;

	// 이벤트 조회
	@GetMapping("/event")
	public ResponseEntity<GenericsResponseDto> getEvents() {
		return ResponseEntity.ok().body(new GenericsResponseDto(eventService.getEvents()));
	}

	// 이벤트 생성
	@PostMapping("/admin/event")
	public ResponseEntity<GenericsResponseDto> createEvent(@Validated @RequestBody EventRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		eventService.createEvent(requestDto, userDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericsResponseDto("이벤트 생성이 완료 되었습니다."));
	}

	// 이벤트 수정
	@PutMapping("/admin/events/{eventId}")
	public ResponseEntity<GenericsResponseDto> updateEvent(@PathVariable Long eventId,
		@Validated @RequestBody EventRequestDto requestDto) {
		eventService.updateEvent(eventId, requestDto);
		return ResponseEntity.ok().body(new GenericsResponseDto("이벤트 수정이 완료 되었습니다."));
	}

	// 이벤트 종료
	@PatchMapping("/admin/events/{eventId}")
	public ResponseEntity<GenericsResponseDto> finishEvent(@PathVariable Long eventId) {
		eventService.finishEvent(eventId);
		return ResponseEntity.ok().body(new GenericsResponseDto("이벤트가 종료 되었습니다."));
	}
}
