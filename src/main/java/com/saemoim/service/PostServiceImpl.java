package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public class PostServiceImpl implements PostService {
	@Override
	public List<PostResponseDto> getAllPosts() {
		return null;
	}

	@Override
	public PostResponseDto getPost(Long postId) {
		return null;
	}

	@Override
	public PostResponseDto createPost(Long groupId, PostRequestDto requestDto, String username) {
		return null;
	}

	@Override
	public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, String username) {
		return null;
	}

	@Override
	public StatusResponseDto deletePost(Long postId, String username) {
		return null;
	}
}
