package com.saemoim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.EmailCodeRequestDto;
import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {
	private final EmailService emailService;

	@PostMapping("/email/auth-code")
	public ResponseEntity<GenericsResponseDto> checkEmailAuthCode(
		@RequestBody @Validated EmailCodeRequestDto requestDto) {
		emailService.getEmailAuthCode(requestDto);
		return ResponseEntity.ok().body(new GenericsResponseDto("이메일 인증 코드가 확인 되었습니다."));
	}

	@PostMapping("/email")
	public ResponseEntity<GenericsResponseDto> sendEmailAuthCode(
		@RequestBody @Validated EmailRequestDto requestDto) throws
		MessagingException {
		emailService.sendEmail(requestDto.getEmail());
		return ResponseEntity.ok().body(new GenericsResponseDto("이메일로 인증 코드가 발송 되었습니다."));
	}

	// 비밀번호 찾기
	@PutMapping("/email/password")
	public ResponseEntity<GenericsResponseDto> sendTempPassword(
		@RequestBody @Validated EmailRequestDto emailRequestDto) throws MessagingException {
		emailService.sendTempPasswordAndChangePassword(emailRequestDto.getEmail());
		return ResponseEntity.ok().body(new GenericsResponseDto("해당 이메일로 임시번호가 발송되었습니다."));
	}
}
