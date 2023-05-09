package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.GiftService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GiftController {
	private final GiftService giftService;

	// 사용자 본인 당첨내역 확인
	@GetMapping("/gift")
	public ResponseEntity<GenericsResponseDto> getGifts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(new GenericsResponseDto(giftService.getGifts(userDetails.getId())));
	}

	// 관리자 이벤트 당첨자 목록 확인
	@GetMapping("/admin/events/{eventId}/gift")
	public ResponseEntity<GenericsResponseDto> getGiftsByEventId(@PathVariable Long eventId) {
		return ResponseEntity.ok().body(new GenericsResponseDto(giftService.getGiftsByEventId(eventId)));
	}

	// 사용자 이벤트 신청
	@PostMapping("/gift/events/{eventId}")
	public ResponseEntity<GenericsResponseDto> applyEvent(@PathVariable Long eventId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		giftService.applyEvent(eventId, userDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericsResponseDto("이벤트에 당첨 되었습니다."));
	}

	// 관리자 선물 발송완료 상태변경
	@PatchMapping("/admin/gifts/{giftId}")
	public ResponseEntity<GenericsResponseDto> updateIsSend(@PathVariable Long giftId) {
		giftService.updateSend(giftId);
		return ResponseEntity.ok().body(new GenericsResponseDto("선물 발송 상태 변경이 완료 되었습니다."));
	}
}
