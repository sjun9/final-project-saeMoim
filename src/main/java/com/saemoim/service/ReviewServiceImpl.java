package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Participant;
import com.saemoim.domain.Review;
import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ParticipantRepository participantRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ReviewResponseDto> getReviews(Long groupId) {
		return reviewRepository.findAllByGroup_IdOrderByCreatedAtDesc(groupId)
			.stream()
			.map(ReviewResponseDto::new)
			.toList();
	}

	@Transactional
	@Override
	public ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, Long userId) {
		Participant participant = participantRepository.findByGroup_IdAndUser_Id(groupId, userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage())
		);
		Review review = new Review(requestDto, participant);
		reviewRepository.save(review);
		return new ReviewResponseDto(review);
	}

	@Transactional
	@Override
	public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_REVIEW.getMessage())
		);
		if (review.isReviewWriter(username)) {
			review.update(requestDto);
			reviewRepository.save(review);
			return new ReviewResponseDto(review);
		} else {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
	}

	@Transactional
	@Override
	public void deleteReview(Long reviewId, String username) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_REVIEW.getMessage())
		);
		if (review.isReviewWriter(username)) {
			reviewRepository.delete(review);
		} else {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
	}

	@Transactional
	@Override
	public void deleteReviewByAdmin(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_REVIEW.getMessage())
		);

		reviewRepository.delete(review);
	}
}
