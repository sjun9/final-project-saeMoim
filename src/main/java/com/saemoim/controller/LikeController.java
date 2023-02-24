package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.LikeService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class LikeController {
	private final LikeService likeService;

	// 게시글 좋아요 여부 확인
	@GetMapping("/posts/{postId}/likeCheck")
	public ResponseEntity<MessageResponseDto> checkLike(@PathVariable Long postId ,@AuthenticationPrincipal UserDetailsImpl userDetails) {
		MessageResponseDto isChecked = likeService.checkLike(postId, userDetails.getId());
		return new ResponseEntity<>(isChecked, HttpStatus.OK);
	}
	// 게시글 좋아요
	@PostMapping("/posts/{postId}/like")
	public ResponseEntity<MessageResponseDto> addLike(@PathVariable Long postId ,@AuthenticationPrincipal UserDetailsImpl userDetails) {
		likeService.addLike(postId, userDetails.getId());
		return new ResponseEntity<>(new MessageResponseDto("좋아요 성공"), HttpStatus.OK);
	}
	// 게시글 좋아요 취소
	@DeleteMapping("/posts/{postId}/like")
	public ResponseEntity<MessageResponseDto> deleteLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		likeService.deleteLike(postId,userDetails.getId());
		return new ResponseEntity<>(new MessageResponseDto("좋아요 취소"), HttpStatus.OK);
	}
}
