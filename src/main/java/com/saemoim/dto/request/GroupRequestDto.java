package com.saemoim.dto.request;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class GroupRequestDto {
	@NotNull
	private Long categoryId;

	private List<String> tagNames;

	@NotBlank
	@Size(min = 2, max = 10)
	private String name;

	@NotBlank
	@Size(min = 10, max = 500)
	private String content;

	@NotBlank
	private String address;

	@NotBlank
	private String firstRegion;
	@NotBlank
	private String secondRegion;

	@NotBlank
	private String latitude;

	@NotBlank
	private String longitude;

	@Min(1)
	@Max(100)
	private int recruitNumber;
}
