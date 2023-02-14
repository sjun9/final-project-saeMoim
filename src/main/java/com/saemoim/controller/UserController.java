package com.saemoim.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원 가입
	@PostMapping("/sign-up")
	public ResponseEntity<MessageResponseDto> signUp(@Validated @RequestBody SignUpRequestDto requestDto) {
		userService.signUp(requestDto);
		return new ResponseEntity<>(new MessageResponseDto("회원가입 완료"), HttpStatus.OK);
	}

	// 로그인
	@PostMapping("/sign-in")
	public ResponseEntity<MessageResponseDto> signIn(@RequestBody SignInRequestDto requestDto) {
		// 로그인 성공 시
		// 토큰 발급
		TokenResponseDto tokenResponseDto = userService.signIn(requestDto);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, tokenResponseDto.getRefreshToken());
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		return new ResponseEntity<>(new MessageResponseDto("로그인 완료"), headers, HttpStatus.OK);
	}

	// @AuthenticationPrincipal
	// Authentication = 인증객체의 principal 부분의 값을 가져옴
	// principal 에는 userDetails 가 있고, 이것은 user, username, password 가 들어있다.)
	// 로그아웃
	@PostMapping("/log-out")
	public ResponseEntity<MessageResponseDto> logout(HttpServletRequest request) {
		userService.logout(request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER));
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, "");
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, "");
		return new ResponseEntity<>(new MessageResponseDto("로그아웃 완료"), headers, HttpStatus.OK);
	}

	// 회원 탈퇴
	@DeleteMapping("/withdrawal")
	public ResponseEntity<MessageResponseDto> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
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

	// 리프레쉬 토큰 재발급
	@PostMapping("/reissue")
	public ResponseEntity<MessageResponseDto> reissue(HttpServletRequest request) {
		TokenResponseDto tokenResponseDto = userService.reissueToken(
			request.getHeader(JwtUtil.AUTHORIZATION_HEADER),
			request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER));
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, tokenResponseDto.getRefreshToken());
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return new ResponseEntity<>(new MessageResponseDto("토큰 재발급 완료"), headers, HttpStatus.OK);
	}
}
