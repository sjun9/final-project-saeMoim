package com.saemoim.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.dto.response.UserResponseDto;

public class AdminServiceImpl implements AdminService {
	@Transactional(readOnly = true)
	@Override
	public List<UserResponseDto> getAllUsers() {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto deletePostByAdmin(Long postId) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto deleteReviewByAdmin(Long reviewId) {
		return null;
	}
}
