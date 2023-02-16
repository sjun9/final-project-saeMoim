package com.saemoim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.dto.response.EmailAuthCodeResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.service.EmailServiceImpl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {
	private final EmailServiceImpl emailService;

	@PostMapping("/email")
	public ResponseEntity<EmailAuthCodeResponseDto> sendEmail(@RequestBody @Validated EmailRequestDto requestDto) throws
		MessagingException {
		String authCode = emailService.sendEmail(requestDto.getEmail());
		return new ResponseEntity<>(new EmailAuthCodeResponseDto(authCode), HttpStatus.OK);
	}

	// 비밀번호 찾기
	@PutMapping("/email/password")
	public ResponseEntity<MessageResponseDto> sendTempPassword(
		@RequestBody @Validated EmailRequestDto emailRequestDto) throws MessagingException {
		emailService.sendTempPasswordAndChangePassword(emailRequestDto.getEmail());
		return new ResponseEntity<>(new MessageResponseDto("해당 이메일로 임시번호가 발송되었습니다."), HttpStatus.OK);
	}
}
