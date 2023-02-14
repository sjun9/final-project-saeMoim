package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Group;
import com.saemoim.domain.Review;
import com.saemoim.domain.User;
import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.GroupRepository;
import com.saemoim.repository.ReviewRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final GroupRepository groupRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ReviewResponseDto> getReviews(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);

		return reviewRepository.findAllByGroupOrderByCreatedAtDesc(group)
			.stream()
			.map(ReviewResponseDto::new)
			.toList();
	}

	@Transactional
	@Override
	public ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, String username) {
		Group group = groupRepository.findById(groupId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_GROUP.getMessage())
		);
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		Review review = new Review(requestDto, group, user);
		return new ReviewResponseDto(review);
	}

	@Transactional
	@Override
	public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public void deleteReview(Long reviewId, String username) {

	}
}
