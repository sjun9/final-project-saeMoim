package com.saemoim.service;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender emailSender;
	private String authCode;

	public void createCode() {

		Random random = new Random();
		StringBuilder code = new StringBuilder();

		for (int i = 0; i < 12; i++) {
			int index = random.nextInt(3);
			switch (index) {
				case 0 -> code.append((char)((int)random.nextInt(26) + 97));
				case 1 -> code.append((char)((int)random.nextInt(26) + 65));
				case 2 -> code.append(random.nextInt(9));
			}
		}
		authCode = code.toString();
	}

	public MimeMessage createEmailForm(String email) throws MessagingException {

		createCode();

		String fromEmail = "dpevent@naver.com";
		String toEmail = email; // 받는 사람
		String title = "회원가입 인증 번호 입니다.";
		String content = "인증 번호는 " + authCode + " 입니다.\n" +
			"해당 인증번호를 인증번호 확인란에 기입하여 주세요.";

		MimeMessage message = emailSender.createMimeMessage();    // 마임 메세지 -
		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);    //
		message.setSubject(title);    // 메일 제목
		message.setFrom(fromEmail);    // 메일을 보내는 주체(이메일) 설정
		message.setText(content);    // 메일 내용 설정. "utf-8" , "html" 등으로 추가 설정 가능

		return message;
	}

	@Override
	public String sendEmail(String toEmail) throws MessagingException {
		MimeMessage emailForm = createEmailForm(toEmail);
		emailSender.send(emailForm);
		return authCode;
	}
}
