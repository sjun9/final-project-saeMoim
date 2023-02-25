package com.saemoim.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.saemoim.domain.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
	private Long id;
	private String name;
	private final List<CategoryResponseDto> categories = new ArrayList<>();

	public CategoryResponseDto(Category category, List<Category> childCategories) {
		this.id = category.getId();
		this.name = category.getName();
		childCategories.stream().filter(c -> c.getParentId().equals(category.getId())).map(c ->
			c.toCategoryResponseDto(childCategories)).toList();
	}
}
