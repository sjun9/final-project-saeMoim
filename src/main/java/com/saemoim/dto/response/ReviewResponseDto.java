package com.saemoim.dto.response;

import com.saemoim.domain.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
	private Long id;
	private Long userId;
	private String username;
	private String comment;

	public ReviewResponseDto(Review review) {
		this.id = review.getId();
		this.userId = review.getUser().getId();
		this.username = review.getUser().getUsername();
		this.comment = review.getContent();
	}
}
