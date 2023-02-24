package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.PostRequestDto;
//import com.saemoim.dto.response.PostListResponseDto;
import com.saemoim.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
	List<PostResponseDto> getAllPosts();

	List<PostResponseDto> getAllGroupPosts(Long group_id, Pageable pageable);

	PostResponseDto getPost(Long postId, Long userId);

	PostResponseDto createPost(Long groupId, PostRequestDto requestDto, Long userId);

	PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Long userId);

	void deletePost(Long postId, Long userId);

	void deletePostByAdmin(Long postId);

	Long getAllGroupPostsCount(Long groupId);
}
