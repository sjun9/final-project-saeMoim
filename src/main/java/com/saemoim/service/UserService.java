package com.saemoim.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.UsernameRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;

public interface UserService {
	void signUp(SignUpRequestDto requestDto);

	void checkEmailDuplication(EmailRequestDto requestDto);

	void checkUsernameDuplication(UsernameRequestDto requestDto);

	TokenResponseDto signIn(SignInRequestDto requestDto);

	void logout(String refreshToken);

	void withdraw(WithdrawRequestDto requestDto, Long userId, String refreshToken);

	List<UserResponseDto> getAllUsers();

	ProfileResponseDto getProfile(Long userId);

	ProfileResponseDto getMyProfile(Long id);

	ProfileResponseDto updateProfile(Long userId, ProfileRequestDto requestDto);

	TokenResponseDto reissueToken(String accessToken, String refreshToken);

	ProfileResponseDto uploadProfileImage(MultipartFile multipartFile, Long id) throws IOException;
}
