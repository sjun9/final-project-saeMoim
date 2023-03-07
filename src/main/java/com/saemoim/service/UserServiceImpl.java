package com.saemoim.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.UsernameRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.fileUpload.AWSS3Uploader;
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
	private final AWSS3Uploader awsS3Uploader;
	private final String dirName = "profile";

	@Transactional
	@Override
	public void signUp(SignUpRequestDto requestDto) {
		checkEmailDuplication(new EmailRequestDto(requestDto.getEmail()));
		checkUsernameDuplication(new UsernameRequestDto(requestDto.getUsername()));
		User user = new User(requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()),
			requestDto.getUsername(), UserRoleEnum.USER);

		userRepository.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public void checkEmailDuplication(EmailRequestDto requestDto) {
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_EMAIL.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void checkUsernameDuplication(UsernameRequestDto requestDto) {
		if (userRepository.existsByUsername(requestDto.getUsername())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
		}
	}

	@Transactional
	@Override
	public TokenResponseDto signIn(SignInRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalAccessError(ErrorCode.INVALID_PASSWORD.getMessage());
		}

		if (user.isBanned()) {
			throw new IllegalArgumentException(ErrorCode.BANNED_USER.getMessage());
		}

		String accessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getRole());
		String refreshToken = _issueRefreshToken(user.getId(), accessToken);

		return new TokenResponseDto(accessToken, refreshToken);
	}

	@Transactional
	@Override
	public TokenResponseDto reissueToken(String accessToken, String refreshToken) {
		String refreshTokenValue = jwtUtil.resolveToken(refreshToken).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage())
		);

		Long userId = Long.valueOf(jwtUtil.getSubjectFromToken(refreshTokenValue));
		User user = _getUserById(userId);

		if (user.isBanned()) {
			throw new IllegalArgumentException(ErrorCode.BANNED_USER.getMessage());
		}

		if (redisUtil.isExists(refreshToken)) {
			if (!redisUtil.getData(refreshToken).equals(accessToken)) {
				throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
			}
		} else {
			throw new IllegalArgumentException(ErrorCode.INVALID_TOKEN.getMessage());
		}

		String newAccessToken = jwtUtil.createAccessToken(userId, user.getUsername(), user.getRole());
		return new TokenResponseDto(newAccessToken, _issueRefreshToken(userId, newAccessToken));
	}

	@Transactional
	@Override
	public void logout(String refreshToken) {
		_deleteRefreshToken(refreshToken);
	}

	@Transactional
	@Override
	public void withdraw(WithdrawRequestDto requestDto, Long userId, String refreshToken) {
		User user = _getUserById(userId);

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalAccessError(ErrorCode.INVALID_PASSWORD.getMessage());
		}

		_deleteRefreshToken(refreshToken);

		userRepository.delete(user);
	}

	@Transactional(readOnly = true)
	@Override
	public List<UserResponseDto> getAllUsers() {
		return userRepository.findAllByOrderByUsername().stream().map(UserResponseDto::new).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public ProfileResponseDto getProfile(Long userId) {
		User user = _getUserById(userId);
		return new ProfileResponseDto(user);
	}

	@Transactional(readOnly = true)
	@Override
	public ProfileResponseDto getMyProfile(Long userId) {
		return new ProfileResponseDto(_getUserById(userId));
	}

	@Transactional
	@Override
	public ProfileResponseDto updateProfile(Long userId, ProfileRequestDto requestDto, MultipartFile multipartFile) {
		User user = _getUserById(userId);
		String imagePath;
		if (multipartFile == null) {
			user.updateProfile(requestDto.getContent());
		} else {
			try {
				awsS3Uploader.delete(user.getImagePath());
				imagePath = awsS3Uploader.upload(multipartFile, dirName);
				user.updateProfile(requestDto.getContent(), imagePath);
			} catch (IOException e) {
				throw new IllegalArgumentException(ErrorCode.FAIL_IMAGE_UPLOAD.getMessage());
			}
		}
		userRepository.save(user);
		return new ProfileResponseDto(user);
	}

	private void _deleteRefreshToken(String refreshToken) {
		if (redisUtil.isExists(refreshToken)) {
			redisUtil.deleteData(refreshToken);
		}
	}

	private String _issueRefreshToken(Long userId, String accessToken) {
		String refreshToken = jwtUtil.createRefreshToken(userId);

		redisUtil.setData(refreshToken, accessToken, JwtUtil.REFRESH_TOKEN_TIME);

		return refreshToken;
	}

	private User _getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
	}
}
