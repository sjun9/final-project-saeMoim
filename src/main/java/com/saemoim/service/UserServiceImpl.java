package com.saemoim.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.redis.RedisUtil;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	@Transactional
	@Override
	public void signUp(SignUpRequestDto requestDto) {
		String email = requestDto.getEmail();
		String password = passwordEncoder.encode(requestDto.getPassword());
		String username = requestDto.getUsername();    // 유일한 닉네임임.
		UserRoleEnum role = UserRoleEnum.USER;

		// 중복 가입 검증
		if (userRepository.existsByEmail(email)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_EMAIL.getMessage());
		}

		if (userRepository.existsByUsername(username)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
		}

		User user = new User(email, password, username, role);
		userRepository.save(user);
	}

	@Transactional
	@Override
	public TokenResponseDto signIn(SignInRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalAccessError(ErrorCode.INVALID_PASSWORD.getMessage());
		}

		if (user.getRole().equals(UserRoleEnum.REPORT)) {
			throw new IllegalArgumentException(ErrorCode.BANNED_USER.getMessage());
		}

		String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getId(), user.getRole());
		String refreshToken = issueRefreshToken(user.getUsername(), accessToken);

		return new TokenResponseDto(accessToken, refreshToken);
	}

	@Transactional
	@Override
	public TokenResponseDto reissueToken(String accessToken, String refreshToken) {
		String accessTokenValue = jwtUtil.resolveToken(accessToken).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage())
		);
		String refreshTokenValue = jwtUtil.resolveToken(refreshToken).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage())
		);

		String username = jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject();
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		if (user.getRole().equals(UserRoleEnum.REPORT)) {
			throw new IllegalArgumentException(ErrorCode.BANNED_USER.getMessage());
		}

		if (redisUtil.isExists(refreshTokenValue)) {
			if (!redisUtil.getData(refreshTokenValue).equals(accessTokenValue)) {
				throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
			}
		} else {
			throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
		}

		return new TokenResponseDto(jwtUtil.createAccessToken(username, user.getId(), user.getRole()),
			issueRefreshToken(username, accessToken));
	}

	@Transactional
	@Override
	public String issueRefreshToken(String username, String accessToken) {
		String refreshToken = jwtUtil.createRefreshToken(username);
		String refreshTokenValue = refreshToken.substring(7);
		String accessTokenValue = jwtUtil.resolveToken(accessToken).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage())
		);

		redisUtil.setData(refreshTokenValue, accessTokenValue, JwtUtil.REFRESH_TOKEN_TIME);

		return refreshToken;
	}

	@Transactional
	@Override
	public void logout(String refreshToken) {
		String refreshTokenValue = jwtUtil.resolveToken(refreshToken).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage())
		);

		if (redisUtil.isExists(refreshTokenValue)) {
			redisUtil.deleteData(refreshTokenValue);
		}
	}

	@Transactional
	@Override
	public void withdraw(String username) {

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
