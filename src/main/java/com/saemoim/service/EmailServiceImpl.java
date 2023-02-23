package com.saemoim.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.User;
import com.saemoim.exception.ErrorCode;
import com.saemoim.redis.RedisUtil;
import com.saemoim.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender emailSender;
	private final RedisUtil redisUtil;

	private String authCode;
	private String tempPassword;

	// 회원가입 시 메일 발송
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

	public String getEmailAuthCode(String email) {
		if (!redisUtil.isExists(email.replace("@", ""))) {
			throw new IllegalArgumentException(ErrorCode.NOT_FOUND_AUTH_CODE.getMessage());
		}
		return redisUtil.getData(email.replace("@", ""));
	}

	@Override
	public void sendEmail(String toEmail) throws MessagingException {
		MimeMessage emailForm = createEmailForm(toEmail);
		emailSender.send(emailForm);
		redisUtil.setData(toEmail.replace("@", ""), authCode, 3 * 60 * 1000L);
	}

	// 비밀번호 찾기 시 메일 발송
	public void createTempPassword() {
		UUID uid = UUID.randomUUID();
		tempPassword = uid.toString().substring(0, 10);
	}

	public MimeMessage createTempPasswordEmailForm(String email) throws MessagingException {
		createTempPassword();

		String fromEmail = "dpevent@naver.com";
		String toEmail = email;
		String title = "새모임 임시 비밀번호 안내 이메일입니다.";
		String content = "안녕하세요. 새모임 임시 비밀번호를 발급해드립니다.\n 회원님의 임시 비밀번호는 " + tempPassword + " 입니다.\n" +
			"로그인 후에 비밀번호를 변경해주세요.";

		MimeMessage message = emailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
		message.setSubject(title);
		message.setFrom(fromEmail);
		message.setText(content);

		return message;
	}

	@Transactional
	@Override
	public void sendTempPasswordAndChangePassword(String email) throws MessagingException {
		User user = userRepository.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		MimeMessage emailForm = createTempPasswordEmailForm(email);
		emailSender.send(emailForm);

		String encodingTempPwd = passwordEncoder.encode(tempPassword);
		user.updatePasswordToTemp(encodingTempPwd);
		userRepository.save(user);
	}
}
