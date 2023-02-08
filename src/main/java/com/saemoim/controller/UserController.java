package com.saemoim.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	// 회원 가입
	@PostMapping("/sign-up")
	public StatusResponseDto signUp(@Validated @RequestBody SignUpRequestDto requestDto) {
		return null;
	}

	// 로그인
	@PostMapping("/sign-in")
	public StatusResponseDto signIn(@Validated @RequestBody SignInRequestDto requestDto) {
		return null;
	}

	// 로그아웃
	@PostMapping("/logout")
	public StatusResponseDto logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 회원 탈퇴
	@DeleteMapping("/withdrawal")
	public StatusResponseDto withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 프로필 조회
	@GetMapping("/profile/users/{userId}")
	public ProfileResponseDto getProfile(@PathVariable Long userId) {
		return null;
	}

	// 프로필 수정
	@PatchMapping("/profile")
	public ProfileResponseDto updateProfile(@Validated @RequestBody ProfileRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 회원 신고
	@PostMapping("/report/users/{subjectUserId}")
	public StatusResponseDto reportUser(@PathVariable Long subjectUserId,
		@Validated @RequestBody ReportRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
