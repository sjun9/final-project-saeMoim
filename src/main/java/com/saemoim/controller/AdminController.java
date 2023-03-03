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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.AdminTokenResponseDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AdminController {
	private final AdminService adminService;

	@PostMapping("/admin/sign-in")
	public ResponseEntity<GenericsResponseDto> signInByAdmin(@Validated @RequestBody AdminRequestDto requestDto) {
		AdminTokenResponseDto tokenResponseDto = adminService.signInByAdmin(requestDto);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		return ResponseEntity.ok().headers(headers).body(new GenericsResponseDto("관리자 로그인 완료"));
	}

	@PostMapping("/admin/log-out")
	public ResponseEntity<GenericsResponseDto> logoutByAdmin() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, "");
		return ResponseEntity.ok().headers(headers).body(new GenericsResponseDto("로그아웃이 완료 되었습니다."));
	}

	@GetMapping("/admin")
	public ResponseEntity<GenericsResponseDto> getAdmins() {
		return ResponseEntity.ok().body(new GenericsResponseDto(adminService.getAdmins()));
	}

	@PostMapping("/admin")
	public ResponseEntity<GenericsResponseDto> createAdmin(@Validated @RequestBody AdminRequestDto requestDto) {
		adminService.createAdmin(requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericsResponseDto("관리자 계정 생성이 완료 되었습니다."));
	}

	@DeleteMapping("/admins/{adminId}")
	public ResponseEntity<GenericsResponseDto> deleteAdmin(@PathVariable Long adminId) {
		adminService.deleteAdmin(adminId);
		return ResponseEntity.ok().body(new GenericsResponseDto("관리자 계정 삭제가 완료 되었습니다."));
	}

	@PostMapping("/admin/reissue")
	public ResponseEntity<GenericsResponseDto> reissueAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String accessToken = adminService.issueToken(userDetails.getId(), userDetails.getUsername(),
			userDetails.getRole());
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return ResponseEntity.ok().headers(headers).body(new GenericsResponseDto("토큰 연장이 완료 되었습니다."));
	}
}
