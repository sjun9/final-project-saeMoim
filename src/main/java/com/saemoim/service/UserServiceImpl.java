package com.saemoim.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Transactional
	@Override
	public StatusResponseDto signUp(SignUpRequestDto requestDto) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto signIn(SignInRequestDto requestDto) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto logout(String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto withdraw(String username) {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public ProfileResponseDto getProfile(Long userId) {
		return null;
	}

	@Transactional
	@Override
	public ProfileResponseDto updateProfile(String username, ProfileRequestDto requestDto) {
		return null;
	}
}
