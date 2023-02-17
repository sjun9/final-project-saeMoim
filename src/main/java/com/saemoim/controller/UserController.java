package com.saemoim.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

import com.saemoim.dto.request.CurrentPasswordRequestDto;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl userService;

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
	public ResponseEntity<MessageResponseDto> withdraw(@RequestBody WithdrawRequestDto requestDto,
		HttpServletRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		userService.withdraw(requestDto, userDetails.getId(), request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER));
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, "");
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, "");
		return new ResponseEntity<>(new MessageResponseDto("회원탈퇴 완료"), headers, HttpStatus.OK);
	}

	// 전체 회원 조회
	@GetMapping("/admin/user")
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

	// 다른 회원 프로필 조회
	@GetMapping("/profile/users/{userId}")
	public ProfileResponseDto getProfile(@PathVariable Long userId) {
		return userService.getProfile(userId);
	}

	// 내 정보 조회 - 마이페이지
	@GetMapping("/profile")
	public ProfileResponseDto getMyProfile(@RequestBody CurrentPasswordRequestDto passwordRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return userService.getMyProfile(userDetails.getId(), passwordRequestDto);
	}

	// 내 정보 수정 - 마이페이지
	@PatchMapping("/profile")
	public ResponseEntity<MessageResponseDto> updateProfile(@RequestBody ProfileRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		userService.updateProfile(userDetails.getId(), requestDto);
		return new ResponseEntity<>(new MessageResponseDto("회원 정보 수정 완료"), HttpStatus.OK);
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
