package com.saemoim.service;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Category;
import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	CategoryRepository categoryRepository;

	@InjectMocks
	CategoryServiceImpl categoryService;

	@Test
	void getCategories() {
		//given
		Category category = Category.builder()
			.name("여행")
			.build();

		List<Category> list = new ArrayList<>();

		when(categoryRepository.findByParentIdIsNull()).thenReturn(list);
		when(categoryRepository.findByParentIdIsNotNull()).thenReturn(list);

		//when
		List<CategoryResponseDto> list1 = categoryService.getCategories();

		//then

	}

	@Test
	void createCategory() {
		//given
		CategoryRequestDto requestDto = new CategoryRequestDto("여행");

		//when

		//then
	}

	@Test
	void createChildCategory() {
	}

	@Test
	void updateCategory() {
	}

	@Test
	void deleteCategory() {
	}
}