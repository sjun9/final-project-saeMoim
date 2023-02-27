package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Category;
import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	@Override
	public List<CategoryResponseDto> getCategories() {
		List<Category> categories = categoryRepository.findAll();
		List<Category> childCategories = categories.stream().filter(c -> c.getParentId() != null).toList();
		return categories.stream()
			.filter(c -> c.getParentId() == null)
			.map(c -> c.toCategoryResponseDto(childCategories))
			.toList();
	}

	@Transactional
	@Override
	public void createParentCategory(CategoryRequestDto requestDto) {
		_isExistsCategory(requestDto);

		Category category = Category.builder()
			.name(requestDto.getName())
			.build();

		categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void createChildCategory(Long parentId, CategoryRequestDto requestDto) {
		_isExistsCategory(requestDto);

		Category findCategory = _getCategoryById(parentId);
		if (findCategory.getParentId() != null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}

		Category category = Category.builder()
			.name(requestDto.getName())
			.parentId(parentId)
			.build();

		categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void updateCategory(Long categoryId, CategoryRequestDto requestDto) {
		_isExistsCategory(requestDto);

		Category category = _getCategoryById(categoryId);

		category.updateCategory(requestDto.getName());
		categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void deleteCategory(Long categoryId) {
		if (categoryRepository.existsByParentId(categoryId)) {
			throw new IllegalArgumentException(ErrorCode.NOT_EMPTY_CATEGORY.getMessage());
		}

		Category category = _getCategoryById(categoryId);

		categoryRepository.delete(category);
	}

	private Category _getCategoryById(Long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CATEGORY.getMessage())
		);
	}

	private void _isExistsCategory(CategoryRequestDto requestDto) {
		if (categoryRepository.existsByName(requestDto.getName())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_CATEGORY.getMessage());
		}
	}
}
