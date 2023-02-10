package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	@Transactional(readOnly = true)
	@Override
	public List<CategoryResponseDto> getCategories() {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto createCategory(CategoryRequestDto requestDto) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto createChildCategory(Long parentId, CategoryRequestDto requestDto) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto deleteCategory(Long categoryId) {
		return null;
	}
}
