package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface CategoryService {
	List<CategoryResponseDto> getCategories();

	StatusResponseDto createCategory(CategoryRequestDto requestDto);

	StatusResponseDto createChildCategory(Long parentId, CategoryRequestDto requestDto);

	StatusResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto);

	StatusResponseDto deleteCategory(Long categoryId);
}
