package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.dto.response.UserResponseDto;

public interface AdminService {

	List<UserResponseDto> getAllUsers();

	StatusResponseDto deletePostByAdmin(Long postId);

	StatusResponseDto deleteReviewByAdmin(Long reviewId);

}
