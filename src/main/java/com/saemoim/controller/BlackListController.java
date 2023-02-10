package com.saemoim.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blacklist")
public class BlackListController {
	// 블랙리스트 목록 조회
	@GetMapping("")
	public List<BlackListResponseDto> getBlacklists() {
		return null;
	}

	// 블랙리스트 추가
	@PostMapping("/users/{userId}")
	public StatusResponseDto addBlacklist(@PathVariable Long userId) {
		return null;
	}

	// 블랙리스트 해제
	@DeleteMapping("/users/{userId}")
	public StatusResponseDto deleteBlacklist(@PathVariable Long userId) {
		return null;
	}
}
