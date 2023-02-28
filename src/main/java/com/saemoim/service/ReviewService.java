package com.saemoim.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;

public interface ReviewService {
	Page<ReviewResponseDto> getReviews(Long groupId, Pageable pageable);

	ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, Long userId);

	ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username);

	void deleteReview(Long reviewId, String username);

	void deleteReviewByAdmin(Long reviewId);
}
