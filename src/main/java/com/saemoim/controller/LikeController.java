package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.LikeService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class LikeController {
	private final LikeService likeService;

	// 게시글 좋아요 여부 확인
	// @GetMapping("/posts/{postId}/like")
	// public ResponseEntity<GenericsResponseDto> checkLike(@PathVariable Long postId,
	// 	@AuthenticationPrincipal UserDetailsImpl userDetails) {
	// 	return ResponseEntity.ok().body(new GenericsResponseDto(likeService.checkLike(postId, userDetails.getId())));
	// }

	// 게시글 좋아요
	@PostMapping("/posts/{postId}/like")
	public ResponseEntity<GenericsResponseDto> addLike(@PathVariable Long postId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		likeService.addLike(postId, userDetails.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericsResponseDto("게시글에 좋아요를 하였습니다."));
	}

	// 게시글 좋아요 취소
	@DeleteMapping("/posts/{postId}/like")
	public ResponseEntity<GenericsResponseDto> deleteLike(@PathVariable Long postId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		likeService.deleteLike(postId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("게시글에 좋아요가 취소 되었습니다."));
	}
}
