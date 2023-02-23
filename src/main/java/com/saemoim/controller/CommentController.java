package com.saemoim.controller;

import java.util.List;

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
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.CommentServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentServiceImpl commentService;
	// 댓글 작성
	@PostMapping("/posts/{postId}/comment")
	public CommentResponseDto createComment(@PathVariable Long postId,
		@Validated @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

		return commentService.createComment(postId, requestDto, userDetails.getId());
	}

	// 댓글 수정
	@PutMapping("/comments/{commentId}")
	public CommentResponseDto updateComment(@PathVariable Long commentId,
		@Validated @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return commentService.updateComment(commentId,requestDto,userDetails.getId());
	}

	// 댓글 삭제
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable Long commentId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		commentService.deleteComment(commentId, userDetails.getId());

		return new ResponseEntity<>(new MessageResponseDto("삭제 완료"), HttpStatus.OK);
	}

	// 댓글 조회
	@GetMapping("/posts/{postId}/comment")
	public List<CommentResponseDto> getComments(@PathVariable Long postId) {
		return commentService.getComments(postId);
	}

}
