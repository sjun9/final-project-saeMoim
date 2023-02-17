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

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.CurrentPasswordRequestDto;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
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
		SignUpRequestDto requestDto = SignUpRequestDto.builder()
			.email("aaaaa@naver.com")
			.password("asdf1234!")
			.username("장성준")
			.build();
		when(passwordEncoder.encode(anyString())).thenReturn("aaaa");
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		//when
		userService.signUp(requestDto);
		//then
		verify(userRepository).save(any(User.class));
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
		when(jwtUtil.resolveToken(anyString())).thenReturn(Optional.of("accessTokenValue"));
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
		User user = mock(User.class);

		when(jwtUtil.resolveToken(anyString())).thenReturn(Optional.of("tokenValue"));
		//when(jwtUtil.getUserInfoFromToken().getSubject()).thenReturn("1");
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(user.isBanned()).thenReturn(false);
		when(redisUtil.isExists(anyString())).thenReturn(true);
		when(redisUtil.getData(anyString())).thenReturn("tokenValue");
		when(jwtUtil.createAccessToken(anyLong(), anyString(), any(UserRoleEnum.class))).thenReturn("accessToken");
		when(jwtUtil.createRefreshToken(anyLong())).thenReturn("refreshToken");
		doNothing().when(redisUtil).setData(anyString(), anyString(), anyLong());

		//when
		TokenResponseDto responseDto = userService.reissueToken("accessToken", "refreshToken");
		//then
		assertThat(responseDto.getAccessToken()).isEqualTo("accessToken");
		assertThat(responseDto.getRefreshToken()).isEqualTo("refreshToken");
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
		when(jwtUtil.resolveToken(anyString())).thenReturn(Optional.of("refreshTokenValue"));
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
		when(userRepository.findAll()).thenReturn(list);
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
		var passwordRequest = mock(CurrentPasswordRequestDto.class);
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("name");
		when(user.getPassword()).thenReturn("encoding");
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(passwordRequest.getPassword()).thenReturn("pass");
		when(passwordEncoder.matches(passwordRequest.getPassword(), user.getPassword())).thenReturn(true);
		// when
		ProfileResponseDto response = userService.getMyProfile(userId, passwordRequest);
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
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(request.getPassword()).thenReturn("pass");
		// when
		userService.updateProfile(userId, request);

		// then
		verify(userRepository).save(user);
		verify(user).updateProfile(request, passwordEncoder.encode(request.getPassword()));
	}

	@Test
	@DisplayName("리프레쉬 토큰 삭제")
	void deleteRefreshToken() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		//given
		Method method = userService.getClass().getDeclaredMethod("deleteRefreshToken", String.class);
		method.setAccessible(true);
		when(jwtUtil.resolveToken(anyString())).thenReturn(Optional.of("refreshTokenValue"));
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
		Method method = userService.getClass().getDeclaredMethod("issueRefreshToken", Long.class, String.class);
		method.setAccessible(true);
		when(jwtUtil.createRefreshToken(anyLong())).thenReturn("refreshToken");
		when(jwtUtil.resolveToken(anyString())).thenReturn(Optional.of("accessTokenValue"));
		doNothing().when(redisUtil).setData(anyString(), anyString(), anyLong());
		//when
		String refreshToken = (String)method.invoke(userService, 1L, "accessToken");
		//then
		assertThat(refreshToken).isEqualTo("refreshToken");
	}
}