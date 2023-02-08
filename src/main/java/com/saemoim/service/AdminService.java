package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.dto.response.UserResponseDto;

public interface AdminService {

	List<UserResponseDto> getAllUsers();

	List<ReportResponseDto> getReportedUsers();

	List<BlackListResponseDto> getBlacklists();

	StatusResponseDto addBlacklist(Long userId);

	StatusResponseDto deleteBlacklist(Long userId);

	StatusResponseDto deletePostByAdmin(Long postId);

	StatusResponseDto deleteReviewByAdmin(Long reviewId);
	
}
