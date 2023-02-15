package com.saemoim.service;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;

public interface UserService {
	void signUp(SignUpRequestDto requestDto);

	TokenResponseDto signIn(SignInRequestDto requestDto);

	void logout(String refreshToken);

	void withdraw(WithdrawRequestDto requestDto, Long userId, String refreshToken);

	ProfileResponseDto getProfile(Long userId);

	ProfileResponseDto updateProfile(Long userId, ProfileRequestDto requestDto);

	TokenResponseDto reissueToken(String accessToken, String refreshToken);

	String issueRefreshToken(Long userId, String accessToken);
}
