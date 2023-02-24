package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 모임 후기 조회
	@GetMapping("/groups/{groupId}/review")
	public ResponseEntity<GenericsResponseDto> getReviews(@PathVariable Long groupId) {
		return ResponseEntity.ok().body(new GenericsResponseDto(reviewService.getReviews(groupId)));
	}

	// 모임 후기 작성
	@PostMapping("/groups/{groupId}/review")
	public ResponseEntity<ReviewResponseDto> createReview(@PathVariable Long groupId,
		@Validated @RequestBody ReviewRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(reviewService.createReview(groupId, requestDto, userDetails.getId()));
	}

	// 모임 후기 수정
	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long reviewId,
		@Validated @RequestBody ReviewRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(reviewService.updateReview(reviewId, requestDto, userDetails.getUsername()));
	}

	// 모임 후기 삭제
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<GenericsResponseDto> deleteReview(@PathVariable Long reviewId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		reviewService.deleteReview(reviewId, userDetails.getUsername());
		return new ResponseEntity<>(new GenericsResponseDto("후기 삭제 성공"), HttpStatus.OK);
	}

	//관리자 후기 삭제
	@DeleteMapping("/admin/reviews/{reviewId}")
	public ResponseEntity<GenericsResponseDto> deleteReviewByAdmin(@PathVariable Long reviewId) {
		reviewService.deleteReviewByAdmin(reviewId);
		return new ResponseEntity<>(new GenericsResponseDto("후기 삭제 성공"), HttpStatus.OK);
	}
}
