package com.saemoim.service;

import com.saemoim.dto.request.EmailCodeRequestDto;

import jakarta.mail.MessagingException;

public interface EmailService {
	void getEmailAuthCode(EmailCodeRequestDto requestDto);

	void sendEmail(String email) throws MessagingException;

	// 비밀번호 찾기 시 메일 발송
	void sendTempPasswordAndChangePassword(String email) throws MessagingException;
}
