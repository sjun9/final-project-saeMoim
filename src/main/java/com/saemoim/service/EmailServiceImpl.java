package com.saemoim.service;

import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.User;
import com.saemoim.dto.request.EmailCodeRequestDto;
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

	private String tempCode;

	private void createTempCode() {
		UUID uid = UUID.randomUUID();
		tempCode = uid.toString().substring(0, 12);
	}

	private MimeMessage createAuthCodeEmailForm(String email) throws MessagingException {

		createTempCode();

		String fromEmail = "dpevent@naver.com";
		String toEmail = email; // 받는 사람
		String title = "새모임 회원가입 인증 번호 안내 이메일입니다.";
		String content = "안녕하세요. 새모임 인증 번호를 발급해드립니다.\n 인증 번호는 " + tempCode + " 입니다.\n" +
			"해당 인증번호를 인증번호 확인란에 기입하여 주세요.";

		MimeMessage message = emailSender.createMimeMessage();    // 마임 메세지 -
		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);    //
		message.setSubject(title);    // 메일 제목
		message.setFrom(fromEmail);    // 메일을 보내는 주체(이메일) 설정
		message.setText(content);    // 메일 내용 설정. "utf-8" , "html" 등으로 추가 설정 가능

		return message;
	}

	private MimeMessage createTempPasswordEmailForm(String email) throws MessagingException {
		createTempCode();

		String fromEmail = "dpevent@naver.com";
		String toEmail = email;
		String title = "새모임 임시 비밀번호 안내 이메일입니다.";
		String content = "안녕하세요. 새모임 임시 비밀번호를 발급해드립니다.\n 회원님의 임시 비밀번호는 " + tempCode + " 입니다.\n" +
			"로그인 후에 비밀번호를 변경해주세요.";

		MimeMessage message = emailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
		message.setSubject(title);
		message.setFrom(fromEmail);
		message.setText(content);

		return message;
	}

	@Override
	public void getEmailAuthCode(EmailCodeRequestDto requestDto) {
		if (!redisUtil.isExists(requestDto.getEmail())) {
			throw new IllegalArgumentException(ErrorCode.NOT_FOUND_AUTH_CODE.getMessage());
		}

		if (!redisUtil.getData(requestDto.getEmail()).equals(requestDto.getCode())) {
			throw new IllegalArgumentException(ErrorCode.NOT_MATCH_CODE.getMessage());
		}
	}

	@Override
	public void sendEmail(String toEmail) throws MessagingException {
		MimeMessage emailForm = createAuthCodeEmailForm(toEmail);
		emailSender.send(emailForm);
		redisUtil.setData(toEmail, tempCode, 3 * 60 * 1000L);
	}

	@Transactional
	@Override
	public void sendTempPasswordAndChangePassword(String email) throws MessagingException {
		User user = userRepository.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		MimeMessage emailForm = createTempPasswordEmailForm(email);
		emailSender.send(emailForm);

		String encodingTempPwd = passwordEncoder.encode(tempCode);
		user.updatePasswordToTemp(encodingTempPwd);
		userRepository.save(user);
	}
}
