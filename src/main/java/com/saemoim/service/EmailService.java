package com.saemoim.service;

import com.saemoim.dto.request.EmailCodeRequestDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailService {

	// 회원가입 시 메일 발송
	void createCode();

	MimeMessage createEmailForm(String email) throws MessagingException;

	void getEmailAuthCode(EmailCodeRequestDto requestDto);

	void sendEmail(String email) throws MessagingException;

	// 비밀번호 찾기 시 메일 발송
	void createTempPassword();

	MimeMessage createTempPasswordEmailForm(String email) throws MessagingException;

	void sendTempPasswordAndChangePassword(String email) throws MessagingException;
}
