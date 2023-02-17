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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.AdminResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AdminController {
	private final AdminServiceImpl adminService;

	@PostMapping("/admin/sign-in")
	public ResponseEntity<MessageResponseDto> signInByAdmin(@RequestBody AdminRequestDto requestDto) {
		TokenResponseDto tokenResponseDto = adminService.signInByAdmin(requestDto);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		return new ResponseEntity<>(new MessageResponseDto("관리자 로그인 완료"), headers, HttpStatus.OK);
	}

	@GetMapping("/admin")
	public List<AdminResponseDto> getAdmins() {
		return adminService.getAdmins();
	}

	@PostMapping("/admin")
	public ResponseEntity<MessageResponseDto> createAdmin(@RequestBody AdminRequestDto requestDto) {
		adminService.createAdmin(requestDto);
		return new ResponseEntity<>(new MessageResponseDto("관리자 계정 생성 완료"), HttpStatus.OK);
	}

	@DeleteMapping("/admin/{adminId}")
	public ResponseEntity<MessageResponseDto> deleteAdmin(@PathVariable Long adminId) {
		adminService.deleteAdmin(adminId);
		return new ResponseEntity<>(new MessageResponseDto("관리자 계정 삭제 완료"), HttpStatus.OK);
	}

	@PostMapping("/admin/reissue")
	public ResponseEntity<MessageResponseDto> reissueAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String accessToken = adminService.issueToken(userDetails.getId(), userDetails.getUsername(),
			userDetails.getRole());
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return new ResponseEntity<>(new MessageResponseDto("토큰 연장 완료"), headers, HttpStatus.OK);
	}
}
