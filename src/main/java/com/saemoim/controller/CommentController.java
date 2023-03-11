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

import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	// 댓글 조회
	@GetMapping("/posts/{postId}/comment")
	public ResponseEntity<GenericsResponseDto> getComments(@PathVariable Long postId) {
		return ResponseEntity.ok().body(new GenericsResponseDto(commentService.getComments(postId)));
	}

	// 댓글 작성
	@PostMapping("/posts/{postId}/comment")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
		@Validated @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(commentService.createComment(postId, requestDto, userDetails.getId()));
	}

	// 댓글 수정
	@PutMapping("/commen제ts/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
		@Validated @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(commentService.updateComment(commentId, requestDto, userDetails.getId()));
	}

	// 댓글 삭제
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<GenericsResponseDto> deleteComment(@PathVariable Long commentId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		commentService.deleteComment(commentId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("삭제 완료"));
	}
}
