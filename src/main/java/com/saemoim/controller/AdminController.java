package com.saemoim.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final AdminServiceImpl adminService;

	@PostMapping("/sign-in")
	public ResponseEntity<MessageResponseDto> signInByAdmin(AdminRequestDto requestDto) {
		TokenResponseDto tokenResponseDto = adminService.signInByAdmin(requestDto);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		return new ResponseEntity<>(new MessageResponseDto("관리자 로그인 완료"), headers, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<MessageResponseDto> createAdmin(AdminRequestDto requestDto) {
		adminService.createAdmin(requestDto);
		return new ResponseEntity<>(new MessageResponseDto("관리자 계정 생성 완료"), HttpStatus.OK);
	}

	@PostMapping("/reissue")
	public ResponseEntity<MessageResponseDto> reissueAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String accessToken = adminService.issueToken(userDetails.getId(), userDetails.getUsername(),
			userDetails.getRole());
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return new ResponseEntity<>(new MessageResponseDto("토큰 연장 완료"), headers, HttpStatus.OK);
	}

	// 전체 회원 조회
	@GetMapping("/user")
	public List<UserResponseDto> getAllUsers() {
		return null;
	}

	// 관리자 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<MessageResponseDto> deletePostByAdmin(@PathVariable Long postId) {
		return null;
	}

	//관리자 후기 삭제
	@DeleteMapping("/review/{reviewId}")
	public ResponseEntity<MessageResponseDto> deleteReviewByAdmin(@PathVariable Long reviewId) {
		return null;
	}
}
