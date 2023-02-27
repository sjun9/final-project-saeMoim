package com.saemoim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TagController {

	private final TagService tagService;

	// 모든 태그 조회
	@GetMapping("/tag")
	public ResponseEntity<GenericsResponseDto> getTags() {
		return ResponseEntity.ok().body(new GenericsResponseDto(tagService.getTags()));
	}

}
