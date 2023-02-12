package com.saemoim.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.AuthenticatedUserDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	@Transactional
	public StatusResponseDto signUp(SignUpRequestDto requestDto) {
		String email = requestDto.getEmail();
		String password = passwordEncoder.encode(requestDto.getPassword());
		String username = requestDto.getUsername();	// 유일한 닉네임임.
		UserRoleEnum role = UserRoleEnum.USER;

		// 중복 가입 검증
		userRepository
			.findByEmail(email).ifPresent((user)-> {throw new IllegalArgumentException(ErrorCode.DUPLICATED_EMAIL.getMessage());});

		userRepository
			.findByUsername(username).ifPresent((user)-> {throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());});

		User user = new User(email,password,username, role);
		userRepository.save(user);



		// 회원가입 후 반환할 값은? 메세지 정도?
		return new StatusResponseDto(HttpStatus.OK,"회원가입 완료");

	}

	@Transactional
	@Override
	public AuthenticatedUserDto signIn(SignInRequestDto requestDto) {
		User savedUser = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new IllegalArgumentException(" 해당 유저 없음"));

		if(!passwordEncoder.matches(requestDto.getPassword(),savedUser.getPassword())){
			throw new IllegalAccessError("비밀번호 불일치");
		}

		//return new StatusResponseDto(HttpStatus.OK,"로그인 성공");
		String email = savedUser.getEmail();
		UserRoleEnum role = savedUser.getRole();
		return new AuthenticatedUserDto(email, role);
	}

	@Transactional
	@Override
	public StatusResponseDto logout(String username) {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto withdraw(String username) {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public ProfileResponseDto getProfile(Long userId) {
		return null;
	}

	@Transactional
	@Override
	public ProfileResponseDto updateProfile(String username, ProfileRequestDto requestDto) {
		return null;
	}
}
