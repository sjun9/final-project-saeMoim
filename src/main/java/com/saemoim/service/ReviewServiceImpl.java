package com.saemoim.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<ReviewResponseDto> getReviews(Long groupId, Pageable pageable) {
		return reviewRepository.findAllByGroup_IdOrderByCreatedAtDesc(groupId, pageable)
			.map(ReviewResponseDto::new);
	}

	@Transactional
	@Override
	public ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, Long userId) {
		Participant participant = participantRepository.findByGroup_IdAndUser_Id(groupId, userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage())
		);
		if (reviewRepository.existsByGroup_IdAndUser_Id(groupId, userId)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_REVIEW.getMessage());
		}
		Review review = new Review(participant, requestDto.getContent());
		reviewRepository.save(review);
		return new ReviewResponseDto(review);
	}

	@Transactional
	@Override
	public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username) {
		Review review = _getReviewById(reviewId);
		if (!review.isReviewWriter(username)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		review.update(requestDto.getContent());
		reviewRepository.save(review);
		return new ReviewResponseDto(review);
	}

	@Transactional
	@Override
	public void deleteReview(Long reviewId, String username) {
		Review review = _getReviewById(reviewId);
		if (!review.isReviewWriter(username)) {
			throw new IllegalArgumentException(ErrorCode.INVALID_USER.getMessage());
		}
		reviewRepository.delete(review);
	}

	@Transactional
	@Override
	public void deleteReviewByAdmin(Long reviewId) {
		Review review = _getReviewById(reviewId);

		reviewRepository.delete(review);
	}

	private Review _getReviewById(Long reviewId) {
		return reviewRepository.findById(reviewId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_REVIEW.getMessage())
		);
	}
}
