package com.saemoim.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.TagResponseDto;
import com.saemoim.service.TagServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TagController {

	private final TagServiceImpl tagService;

	// 모든 태그 조회
	@GetMapping("/tag")
	public List<TagResponseDto> getTags() {
		return tagService.getTags();
	}

}
