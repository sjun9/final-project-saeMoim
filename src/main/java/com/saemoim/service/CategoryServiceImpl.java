package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public class CategoryServiceImpl implements CategoryService {
	@Override
	public List<CategoryResponseDto> getCategories() {
		return null;
	}

	@Override
	public StatusResponseDto createCategory(CategoryRequestDto requestDto) {
		return null;
	}

	@Override
	public StatusResponseDto createChildCategory(Long parentId, CategoryRequestDto requestDto) {
		return null;
	}

	@Override
	public StatusResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto) {
		return null;
	}

	@Override
	public StatusResponseDto deleteCategory(Long categoryId) {
		return null;
	}
}
