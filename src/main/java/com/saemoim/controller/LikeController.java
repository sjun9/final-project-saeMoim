package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.LikeService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class LikeController {
	private final LikeService likeService;
	// 게시글 좋아요
	@PostMapping("/post/{postId}/like")
	public ResponseEntity<MessageResponseDto> addLike(@PathVariable Long postId ,@AuthenticationPrincipal UserDetailsImpl userDetails) {
		likeService.addLike(postId, userDetails.getId());
		return new ResponseEntity<>(new MessageResponseDto("좋아요 성공"), HttpStatus.OK);
	}
	// 게시글 좋아요 취소
	@PostMapping("/post/{postId}")
	public ResponseEntity<MessageResponseDto> deleteLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		likeService.deleteLike(postId,userDetails.getId());
		return new ResponseEntity<>(new MessageResponseDto("좋아요 취소"), HttpStatus.OK);
	}
}
