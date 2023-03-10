package com.saemoim.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.UsernameRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final AWSS3Uploader awss3Uploader;

	// 회원 가입
	@PostMapping("/sign-up")
	public ResponseEntity<GenericsResponseDto> signUp(@Validated @RequestBody SignUpRequestDto requestDto) {
		userService.signUp(requestDto);
		return ResponseEntity.ok().body(new GenericsResponseDto("회원가입이 완료 되었습니다."));
	}

	// 이메일 중복 확인
	@PostMapping("/sign-up/email")
	public ResponseEntity<GenericsResponseDto> checkEmailDuplication(
		@Validated @RequestBody EmailRequestDto requestDto) {
		userService.checkEmailDuplication(requestDto);
		return ResponseEntity.ok().body(new GenericsResponseDto("이메일 중복 검사가 완료 되었습니다."));
	}

	// 이름 중복 확인
	@PostMapping("/sign-up/username")
	public ResponseEntity<GenericsResponseDto> checkUsernameDuplication(
		@Validated @RequestBody UsernameRequestDto requestDto) {
		userService.checkUsernameDuplication(requestDto);
		return ResponseEntity.ok().body(new GenericsResponseDto("이름 중복 검사가 완료 되었습니다."));
	}

	// 로그인
	@PostMapping("/sign-in")
	public ResponseEntity<GenericsResponseDto> signIn(@RequestBody SignInRequestDto requestDto) {
		// 로그인 성공 시
		// 토큰 발급
		TokenResponseDto tokenResponseDto = userService.signIn(requestDto);
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, tokenResponseDto.getRefreshToken());
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		return ResponseEntity.ok().headers(headers).body(new GenericsResponseDto("로그인이 완료 되었습니다."));
	}

	@PostMapping("/log-out")
	public ResponseEntity<GenericsResponseDto> logout(HttpServletRequest request) {
		userService.logout(request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER));
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, "");
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, "");
		return ResponseEntity.ok().headers(headers).body(new GenericsResponseDto("로그아웃이 완료 되었습니다."));
	}

	// 회원 탈퇴
	@DeleteMapping("/withdrawal")
	public ResponseEntity<GenericsResponseDto> withdraw(@RequestBody WithdrawRequestDto requestDto,
		HttpServletRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		userService.withdraw(requestDto, userDetails.getId(), request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER));
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, "");
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, "");
		return ResponseEntity.ok().headers(headers).body(new GenericsResponseDto("회원탈퇴가 완료 되었습니다."));
	}

	// 전체 회원 조회
	@GetMapping("/admin/user")
	public ResponseEntity<GenericsResponseDto> getAllUsers() {
		return ResponseEntity.ok().body(new GenericsResponseDto(userService.getAllUsers()));
	}

	// 다른 회원 프로필 조회
	@GetMapping("/profile/users/{userId}")
	public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
		return ResponseEntity.ok().body(userService.getProfile(userId));
	}

	// 내 정보 조회 - 마이페이지
	@GetMapping("/profile")
	public ResponseEntity<ProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(userService.getMyProfile(userDetails.getId()));
	}

	@GetMapping("/user")
	public ResponseEntity<GenericsResponseDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(new GenericsResponseDto(userDetails.getId()));
	}

	// 내 정보 수정 - 마이페이지
	@PutMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ProfileResponseDto> updateProfile(
		@Validated @RequestPart ProfileRequestDto requestDto,
		@RequestPart(required = false, name = "img") MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(userService.updateProfile(userDetails.getId(), requestDto, multipartFile));
	}

	// 리프레쉬 토큰 재발급
	@PostMapping("/reissue")
	public ResponseEntity<GenericsResponseDto> reissue(HttpServletRequest request) {
		TokenResponseDto tokenResponseDto = userService.reissueToken(
			request.getHeader(JwtUtil.AUTHORIZATION_HEADER),
			request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER));
		HttpHeaders headers = new HttpHeaders();
		headers.set(JwtUtil.AUTHORIZATION_HEADER, tokenResponseDto.getAccessToken());
		headers.set(JwtUtil.REFRESH_TOKEN_HEADER, tokenResponseDto.getRefreshToken());
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return ResponseEntity.ok().headers(headers).body(new GenericsResponseDto("토큰 재발급이 완료 되었습니다."));
	}
}
