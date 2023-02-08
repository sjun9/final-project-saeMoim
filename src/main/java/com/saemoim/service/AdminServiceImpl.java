package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.dto.response.UserResponseDto;

public class AdminServiceImpl implements AdminService {
	@Override
	public List<UserResponseDto> getAllUsers() {
		return null;
	}

	@Override
	public List<ReportResponseDto> getReportedUsers() {
		return null;
	}

	@Override
	public List<BlackListResponseDto> getBlacklists() {
		return null;
	}

	@Override
	public StatusResponseDto addBlacklist(Long userId) {
		return null;
	}

	@Override
	public StatusResponseDto deleteBlacklist(Long userId) {
		return null;
	}

	@Override
	public StatusResponseDto deletePostByAdmin(Long postId) {
		return null;
	}

	@Override
	public StatusResponseDto deleteReviewByAdmin(Long reviewId) {
		return null;
	}
}
