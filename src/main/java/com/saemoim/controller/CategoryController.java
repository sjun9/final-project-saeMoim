package com.saemoim.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	// 카테고리 조회
	@GetMapping("/category")
	public List<CategoryResponseDto> getCategories() {
		return null;
	}

	// 카테고리 생성
	@PostMapping("/admin/category")
	public StatusResponseDto createCategory(@Validated @RequestBody CategoryRequestDto requestDto) {
		return null;
	}

	// 자식 카테고리 생성
	@PostMapping("/admin/categories/{parentId}")
	public StatusResponseDto createChildCategory(@PathVariable Long parentId,
		@Validated @RequestBody CategoryRequestDto requestDto) {
		return null;
	}

	// 카테고리 수정
	@PutMapping("/admin/categories/{categoryId}")
	public StatusResponseDto updateCategory(@PathVariable Long categoryId,
		@Validated @RequestBody CategoryRequestDto requestDto) {
		return null;
	}

	// 카테고리 삭제
	@DeleteMapping("/admin/categories/{categoryId}")
	public StatusResponseDto deleteCategory(@PathVariable Long categoryId) {
		return null;
	}
}
