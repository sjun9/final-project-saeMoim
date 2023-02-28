package com.saemoim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.saemoim.domain.Admin;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
	@InjectMocks
	private AdminServiceImpl adminService;
	@Mock
	private AdminRepository adminRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("관리자 로그인")
	void signInByAdmin() {
		//given
		Admin admin = mock(Admin.class);
		AdminRequestDto requestDto = mock(AdminRequestDto.class);

		when(adminService.issueToken(anyLong(), anyString(), any(UserRoleEnum.class))).thenReturn("aaaa");
		when(adminRepository.findByUsername(anyString())).thenReturn(Optional.of(admin));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(requestDto.getUsername()).thenReturn("jun");
		when(requestDto.getPassword()).thenReturn("1234");
		when(admin.getId()).thenReturn(1L);
		when(admin.getUsername()).thenReturn("jun");
		when(admin.getRole()).thenReturn(UserRoleEnum.ADMIN);
		when(admin.getPassword()).thenReturn("1234");
		//when
		TokenResponseDto responseDto = adminService.signInByAdmin(requestDto);
		//then
		assertThat(responseDto.getAccessToken()).isEqualTo("aaaa");
	}

	@Test
	@DisplayName("관리자 목록 조회")
	void getAdmins() {
		//given
		List<Admin> list = new ArrayList<>();
		when(adminRepository.findAll()).thenReturn(list);
		//when
		adminService.getAdmins();
		//then
		verify(adminRepository).findAll();
	}

	@Test
	@DisplayName("관리자 계정 생성")
	void createAdmin() {
		//given
		AdminRequestDto requestDto = new AdminRequestDto("jun", "1234");

		when(adminRepository.existsByUsername(anyString())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn(anyString());
		//when
		adminService.createAdmin(requestDto);
		//then
		verify(adminRepository).save(any(Admin.class));
	}

	@Test
	@DisplayName("관리자 계정 삭제")
	void deleteAdmin() {
		//given
		Long adminId = 1L;
		Admin admin = mock(Admin.class);

		when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
		when(admin.isRootAdmin()).thenReturn(false);
		doNothing().when(adminRepository).deleteById(anyLong());
		//when
		adminService.deleteAdmin(adminId);
		//then
		verify(adminRepository).deleteById(adminId);
	}

	@Test
	@DisplayName("액세스 토큰 발급")
	void issueToken() {
		//given
		when(jwtUtil.createAccessToken(anyLong(), anyString(), any(UserRoleEnum.class))).thenReturn("aaa");
		//when
		String token = adminService.issueToken(1L, "jun", UserRoleEnum.ADMIN);
		//then
		verify(jwtUtil).createAccessToken(anyLong(), anyString(), any(UserRoleEnum.class));
		assertThat(token).isEqualTo("aaa");
	}
}