package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.service.BlackListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BlackListController {
	private final BlackListService blackListService;

	// 블랙리스트 목록 조회
	@GetMapping("/blacklist")
	public ResponseEntity<GenericsResponseDto> getBlacklists() {
		return ResponseEntity.ok().body(new GenericsResponseDto(blackListService.getBlacklists()));
	}

	// 블랙리스트 등록
	@PostMapping("/blacklist/users/{userId}")
	public ResponseEntity<GenericsResponseDto> addBlacklist(@PathVariable Long userId) {
		blackListService.addBlacklist(userId);
		return ResponseEntity.status(HttpStatus.CREATED).body((new GenericsResponseDto("블랙리스트 등록이 완료 되었습니다.")));
	}

	// 영구 블랙리스트 등록
	@PatchMapping("/blacklists/{blacklistId}")
	public ResponseEntity<GenericsResponseDto> imposePermanentBan(@PathVariable Long blacklistId) {
		blackListService.imposePermanentBan(blacklistId);
		return ResponseEntity.ok().body(new GenericsResponseDto("영구 블랙리스트 등록이 완료 되었습니다."));
	}

	// 블랙리스트 해제
	@DeleteMapping("/blacklists/{blacklistId}")
	public ResponseEntity<GenericsResponseDto> deleteBlacklist(@PathVariable Long blacklistId) {
		blackListService.deleteBlacklist(blacklistId);
		return ResponseEntity.ok().body(new GenericsResponseDto("블랙리스트 해제가 완료 되었습니다."));
	}
}
