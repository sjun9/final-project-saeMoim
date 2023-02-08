package com.saemoim.service;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public class ReviewServiceImpl implements ReviewService {
	@Override
	public ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, String username) {
		return null;
	}

	@Override
	public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username) {
		return null;
	}

	@Override
	public StatusResponseDto deleteReview(Long reviewId, String username) {
		return null;
	}
}
