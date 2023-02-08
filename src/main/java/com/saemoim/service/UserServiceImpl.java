package com.saemoim.service;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public class UserServiceImpl implements UserService {
	@Override
	public StatusResponseDto signUp(SignUpRequestDto requestDto) {
		return null;
	}

	@Override
	public StatusResponseDto signIn(SignInRequestDto requestDto) {
		return null;
	}

	@Override
	public StatusResponseDto logout(String username) {
		return null;
	}

	@Override
	public StatusResponseDto withdraw(String username) {
		return null;
	}

	@Override
	public ProfileResponseDto getProfile(Long userId) {
		return null;
	}

	@Override
	public ProfileResponseDto updateProfile(String username, ProfileRequestDto requestDto) {
		return null;
	}

	@Override
	public StatusResponseDto reportUser(Long subjectUserId, ReportRequestDto requestDto, String username) {
		return null;
	}
}
