package com.saemoim.dto.request;

import lombok.Getter;

@Getter
public class GroupRequestDto {
	private Long categoryId;
	private String content;
	private String location;
	private int recruitNumber;
}
