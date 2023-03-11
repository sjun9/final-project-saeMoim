package com.saemoim.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;

public interface ReviewService {
	Page<ReviewResponseDto> getReviews(Long groupId, Pageable pageable);

	void createReview(Long groupId, ReviewRequestDto requestDto, Long userId);

	void updateReview(Long reviewId, ReviewRequestDto requestDto, Long userId);

	void deleteReview(Long reviewId, Long userId);

	void deleteReviewByAdmin(Long reviewId);
}
