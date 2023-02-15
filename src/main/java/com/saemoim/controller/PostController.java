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

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.PostListResponseDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	// 전체 게시글 조회
	@GetMapping("/post")
	public List<PostListResponseDto> getAllPosts() {
		return postService.getAllPosts();
	}

	// 선택한 게시글 조회
	@GetMapping("/posts/{postId}")
	public PostResponseDto getPost(@PathVariable Long postId) {
		return postService.getPost(postId);
	}

	// 게시글 생성
	@PostMapping("/groups/{groupId}/post")
	public PostResponseDto createPost(@PathVariable Long groupId, @Validated @RequestBody PostRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return postService.createPost(groupId,requestDto,userDetails.getId());
	}

	// 게시글 수정
	@PutMapping("/posts/{postId}")
	public PostResponseDto updatePost(@PathVariable Long postId,
		@Validated @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

		return postService.updatePost(postId,requestDto, userDetails.getId());
	}

	// 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<MessageResponseDto> deletePost(@PathVariable Long postId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		postService.deletePost(postId, userDetails.getId());
		return new ResponseEntity<>(new MessageResponseDto("삭제 성공"), HttpStatus.OK);
	}
}
