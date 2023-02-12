package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	@Transactional(readOnly = true)
	@Override
	public List<PostResponseDto> getAllPosts() {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public PostResponseDto getPost(Long postId) {
		return null;
	}

	@Transactional
	@Override
	public PostResponseDto createPost(Long groupId, PostRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto deletePost(Long postId, String username) {
		return null;
	}
}
