package com.saemoim.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;

public interface PostService {

	Page<PostResponseDto> getAllPostsByGroup(Long group_id, Pageable pageable, Long userId);

	PostResponseDto getPost(Long postId, Long userId);

	PostResponseDto createPost(Long groupId, PostRequestDto requestDto, Long userId, MultipartFile multipartFile);

	PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Long userId);

	void deletePost(Long postId, Long userId);

	void deletePostByAdmin(Long postId);
}
