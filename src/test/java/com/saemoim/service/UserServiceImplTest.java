package com.saemoim.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.saemoim.jwt.JwtUtil;
import com.saemoim.redis.RedisUtil;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private RedisUtil redisUtil;

	@Test
	@DisplayName("회원 가입")
	void signUp() {
		//given
		SignUpRequestDto requestDto = new SignUpRequestDto("aaaaa@naver.com", "asdf1234!", "장성준");

		when(passwordEncoder.encode(anyString())).thenReturn("aaaa");
		//when
		userService.signUp(requestDto);
		//then
		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("이메일 중복 확인")
	void checkEmailDuplication() {
		//given
		EmailRequestDto requestDto = EmailRequestDto.builder()
			.email("aaaaa@naver.com")
			.build();
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		//when
		userService.checkEmailDuplication(requestDto);
		//then
		verify(userRepository).existsByEmail("aaaaa@naver.com");
	}

	@Test
	@DisplayName("이름 중복 확인")
	void checkUsernameDuplication() {
		//given
		UsernameRequestDto requestDto = UsernameRequestDto.builder()
			.username("장성준")
			.build();
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		//when
		userService.checkUsernameDuplication(requestDto);
		//then
		verify(userRepository).existsByUsername("장성준");
	}

	@Test
	@DisplayName("로그인")
	void signIn() {
		//given
		SignInRequestDto requestDto = SignInRequestDto.builder()
			.email("aaaaa@naver.com")
			.password("asdf1234!")
			.build();
		User user = mock(User.class);
		when(user.getRole()).thenReturn(UserRoleEnum.USER);
		when(user.getId()).thenReturn(1L);
		when(user.getUsername()).thenReturn("장성준");
		when(user.getPassword()).thenReturn("encodingPassword");
		when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(true);
		when(user.isBanned()).thenReturn(false);
		when(jwtUtil.createAccessToken(anyLong(), anyString(), any(UserRoleEnum.class))).thenReturn("accessToken");
		when(jwtUtil.createRefreshToken(anyLong())).thenReturn("refreshToken");
		doNothing().when(redisUtil).setData(anyString(), anyString(), anyLong());
		//when
		TokenResponseDto responseDto = userService.signIn(requestDto);
		//then
		assertThat(responseDto.getAccessToken()).isEqualTo("accessToken");
		assertThat(responseDto.getRefreshToken()).isEqualTo("refreshToken");
	}

	@Test
	@DisplayName("토큰 재발급")
	void reissueToken() {
		//given
		String getSubject = "1";
		Long userId = Long.valueOf(getSubject);
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		String accessTokenValue = "accessTokenValue";
		String refreshTokenValue = "refreshTokenValue";
		String username = "장성준";
		UserRoleEnum role = UserRoleEnum.USER;
		User user = mock(User.class);

		when(jwtUtil.resolveToken(refreshToken)).thenReturn(Optional.of(refreshTokenValue));
		when(jwtUtil.getSubjectFromToken(refreshTokenValue)).thenReturn(getSubject);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(user.isBanned()).thenReturn(false);
		when(user.getUsername()).thenReturn(username);
		when(user.getRole()).thenReturn(role);
		when(redisUtil.isExists(refreshToken)).thenReturn(true);
		when(redisUtil.getData(refreshToken)).thenReturn(accessToken);
		when(jwtUtil.createAccessToken(userId, username, role)).thenReturn(accessToken);
		when(jwtUtil.createRefreshToken(userId)).thenReturn(refreshToken);
		doNothing().when(redisUtil)
			.setData(refreshToken, accessToken, JwtUtil.REFRESH_TOKEN_TIME);

		//when
		TokenResponseDto responseDto = userService.reissueToken(accessToken, refreshToken);
		//then
		assertThat(responseDto.getAccessToken()).isEqualTo(accessToken);
		assertThat(responseDto.getRefreshToken()).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("회원 탈퇴")
	void withdraw() {
		//given
		User user = mock(User.class);
		WithdrawRequestDto requestDto = mock(WithdrawRequestDto.class);
		when(requestDto.getPassword()).thenReturn("password");
		when(user.getPassword()).thenReturn("encodingPassword");
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
		when(redisUtil.isExists(anyString())).thenReturn(false);
		//when
		userService.withdraw(requestDto, 1L, "refreshToken");
		//then
		verify(userRepository).delete(user);
	}

	@Test
	@DisplayName("전체 사용자 조회")
	void getAllUsers() {
		//given
		User user = mock(User.class);
		List<User> list = new ArrayList<>();
		list.add(user);
		when(user.getUsername()).thenReturn("장성준");
		when(userRepository.findAllByOrderByUsername()).thenReturn(list);
		//when
		List<UserResponseDto> responseDtoList = userService.getAllUsers();
		//then
		assertThat(responseDtoList.get(0).getUsername()).isEqualTo("장성준");
	}

	@Test
	@DisplayName("사용자 프로필 조회")
	void getProfile() {
		// given
		var userId = 1L;
		var user = mock(User.class);
		when(user.getUsername()).thenReturn("name");
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		// when
		ProfileResponseDto response = userService.getProfile(userId);
		// then
		assertThat(response.getUsername()).isEqualTo("name");
	}

	@Test
	@DisplayName("내 정보 조회")
	void getMyProfile() {
		// given
		var userId = 1L;
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("name");
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		// when
		ProfileResponseDto response = userService.getMyProfile(userId);
		// then
		assertThat(response.getUsername()).isEqualTo("name");
	}

	@Test
	@DisplayName("내 정보 수정")
	void updateProfile() {
		// given
		var userId = 1L;
		var request = mock(ProfileRequestDto.class);
		var user = mock(User.class);
		var image = mock(MultipartFile.class);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		// when
		userService.updateProfile(userId, request, image);

		// then
		verify(userRepository).save(user);
		verify(user).updateProfile(request.getContent());
	}

	@Test
	@DisplayName("리프레쉬 토큰 삭제")
	void deleteRefreshToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		//given
		Method method = userService.getClass().getDeclaredMethod("_deleteRefreshToken", String.class);
		method.setAccessible(true);
		when(redisUtil.isExists(anyString())).thenReturn(false);
		//when
		method.invoke(userService, "refreshToken");
		//then
		verify(redisUtil).isExists(anyString());
	}

	@Test
	@DisplayName("리프레쉬 토큰 발급")
	void issueRefreshToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		//given
		Method method = userService.getClass().getDeclaredMethod("_issueRefreshToken", Long.class, String.class);
		method.setAccessible(true);
		when(jwtUtil.createRefreshToken(anyLong())).thenReturn("refreshToken");
		doNothing().when(redisUtil).setData(anyString(), anyString(), anyLong());
		//when
		String refreshToken = (String)method.invoke(userService, 1L, "accessToken");
		//then
		assertThat(refreshToken).isEqualTo("refreshToken");
	}
}