package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;

public interface AdminService {

	TokenResponseDto signInByAdmin(AdminRequestDto requestDto);

	List<UserResponseDto> getAllUsers();

	void deletePostByAdmin(Long postId);

	void deleteReviewByAdmin(Long reviewId);

}
