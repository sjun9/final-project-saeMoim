package com.saemoim.controller;

import org.springframework.data.domain.Pageable;
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
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 모임 후기 조회
	@GetMapping("/groups/{groupId}/review")
	public ResponseEntity<GenericsResponseDto> getReviews(@PathVariable Long groupId, Pageable pageable) {
		return ResponseEntity.ok().body(new GenericsResponseDto(reviewService.getReviews(groupId, pageable)));
	}

	// 모임 후기 작성
	@PostMapping("/groups/{groupId}/review")
	public ResponseEntity<GenericsResponseDto> createReview(@PathVariable Long groupId,
		@Validated @RequestBody ReviewRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		reviewService.createReview(groupId, requestDto, userDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new GenericsResponseDto("후기 작성이 완료 되었습니다."));
	}

	// 모임 후기 수정
	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<GenericsResponseDto> updateReview(@PathVariable Long reviewId,
		@Validated @RequestBody ReviewRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		reviewService.updateReview(reviewId, requestDto, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("후기 수정이 완료 되었습니다."));
	}

	// 모임 후기 삭제
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<GenericsResponseDto> deleteReview(@PathVariable Long reviewId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		reviewService.deleteReview(reviewId, userDetails.getId());
		return new ResponseEntity<>(new GenericsResponseDto("후기 삭제가 성공 하였습니다."), HttpStatus.OK);
	}

	//관리자 후기 삭제
	@DeleteMapping("/admin/reviews/{reviewId}")
	public ResponseEntity<GenericsResponseDto> deleteReviewByAdmin(@PathVariable Long reviewId) {
		reviewService.deleteReviewByAdmin(reviewId);
		return new ResponseEntity<>(new GenericsResponseDto("후기 삭제가 성공 하였습니다."), HttpStatus.OK);
	}
}
