package com.saemoim.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
	private Long id;
	private String name;
	private List<CategoryResponseDto> categories;
}
