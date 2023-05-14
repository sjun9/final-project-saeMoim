package com.saemoim.service;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;

public interface CategoryService {
	GenericsResponseDto getCategories();

	void createParentCategory(CategoryRequestDto requestDto);

	void createChildCategory(Long parentId, CategoryRequestDto requestDto);

	void updateCategory(Long categoryId, CategoryRequestDto requestDto);

	void deleteCategory(Long categoryId);
}
