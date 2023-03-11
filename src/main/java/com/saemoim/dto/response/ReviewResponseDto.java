package com.saemoim.dto.response;

import com.saemoim.domain.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
	private Long id;
	private Long userId;
	private String username;
	private String content;

	public ReviewResponseDto(Review review) {
		this.id = review.getId();
		this.userId = review.getUserId();
		this.username = review.getUsername();
		this.content = review.getContent();
	}
}
