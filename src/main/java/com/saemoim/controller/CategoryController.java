package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	// 카테고리 조회
	@GetMapping("/category")
	public ResponseEntity<GenericsResponseDto> getCategories() {
		return ResponseEntity.ok().body(new GenericsResponseDto(categoryService.getCategories()));
	}

	// 카테고리 생성
	@PostMapping("/admin/category")
	public ResponseEntity<GenericsResponseDto> createCategory(@Validated @RequestBody CategoryRequestDto requestDto) {
		categoryService.createCategory(requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new GenericsResponseDto(requestDto.getName() + " 카테고리 생성이 완료 되었습니다."));
	}

	// 자식 카테고리 생성
	@PostMapping("/admin/categories/{parentId}")
	public ResponseEntity<GenericsResponseDto> createChildCategory(@PathVariable Long parentId,
		@Validated @RequestBody CategoryRequestDto requestDto) {
		categoryService.createChildCategory(parentId, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new GenericsResponseDto(requestDto.getName() + " 카테고리 생성이 완료 되었습니다."));
	}

	// 카테고리 수정
	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<GenericsResponseDto> updateCategory(@PathVariable Long categoryId,
		@Validated @RequestBody CategoryRequestDto requestDto) {
		categoryService.updateCategory(categoryId, requestDto);
		return ResponseEntity.ok().body(new GenericsResponseDto(requestDto.getName() + " 카테고리 수정이 완료 되었습니다."));
	}

	// 카테고리 삭제
	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<GenericsResponseDto> deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.ok().body(new GenericsResponseDto("카테고리 삭제가 완료 되었습니다."));
	}
}
