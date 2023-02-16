package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.security.UserDetailsImpl;

public interface ReviewService {
	List<ReviewResponseDto> getReviews(Long groupId);

	ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, Long userId);

	ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username);

	void deleteReview(Long reviewId, String username);

	void deleteReviewByAdmin(Long reviewId);

	void reviewLike(Long reviewId, Long userId);
}
