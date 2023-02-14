package com.saemoim.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRequestDto {
	private String content;
}
