package com.saemoim.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
//import com.saemoim.dto.response.PostListResponseDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.PostServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostServiceImpl postService;

	// 전체 게시글 조회
	@GetMapping("/allPost") // 타 모임의 게시글까지 전부 조회되므로, admin 전용 기능으로 옮기거나 없애야 할 듯
	public List<PostResponseDto> getAllPosts() {
		return postService.getAllPosts();
	}

	// 그룹 전체 게시글 갯수 조회 (페이지 버튼 생성용)
	@GetMapping("/posts/groups/{groupId}/count")
	public Long getAllGroupPostsCount(@PathVariable Long groupId) {
		return postService.getAllGroupPostsCount(groupId);
	}

	// 그룹 전체 게시글 조회
	@GetMapping("/posts/groups/{groupId}")
	public List<PostResponseDto> getAllGroupPosts(@PathVariable Long groupId,
												  @PageableDefault(size = 10, page = 0) Pageable pageable) {
		return postService.getAllGroupPosts(groupId, pageable);
	}

	// 선택한 게시글 조회
	@GetMapping("/posts/{postId}")
	public PostResponseDto getPost(@PathVariable Long postId ,@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return postService.getPost(postId, userDetails.getId());
	}

	// 게시글 생성
	@PostMapping("/posts/groups/{groupId}")
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

	// 관리자 게시글 삭제
	@DeleteMapping("/admin/posts/{postId}")
	public ResponseEntity<MessageResponseDto> deletePostByAdmin(@PathVariable Long postId) {
		postService.deletePostByAdmin(postId);
		return new ResponseEntity<>(new MessageResponseDto("게시글 삭제 완료"), HttpStatus.OK);
	}
}
