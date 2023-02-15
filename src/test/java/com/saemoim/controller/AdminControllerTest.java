package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

	@InjectMocks
	private AdminController adminController;

	@Mock
	private AdminServiceImpl adminService;

	private MockMvc mockMvc;

	@BeforeEach
	void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
	}

	@Test
	@DisplayName("관리자 로그인")
	void signInByAdmin() throws Exception {
		//given
		TokenResponseDto responseDto = mock(TokenResponseDto.class);

		when(responseDto.getAccessToken()).thenReturn("aaaaa");
		when(adminService.signInByAdmin(any(AdminRequestDto.class))).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(any(AdminRequestDto.class))));//then
		//then
		verify(adminService).signInByAdmin(any(AdminRequestDto.class));
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", "aaaaa"))
			.andExpect(jsonPath("message", "관리자 로그인 완료").exists());
	}

	@Test
	@DisplayName("관리자 계정 생성")
	void createAdmin() throws Exception {
		//given
		MessageResponseDto responseDto = new MessageResponseDto("관리자 계정 생성 완료");
		AdminRequestDto requestDto = mock(AdminRequestDto.class);

		doNothing().when(adminService).createAdmin(any(AdminRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/admin")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(any(AdminRequestDto.class))));//then
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}

	//수정 필요
	@Test
	//@WithMockUser(roles = "ADMIN")
	@DisplayName("관리자 토큰 연장")
	void reissueAdmin() throws Exception {
		//given
		String accessToken = "aaaaa";

		UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

		when(adminService.issueToken(null, null, null)).thenReturn(accessToken);
		// when(userDetails.getId()).thenReturn(1L);
		// when(userDetails.getUsername()).thenReturn("jun");
		// when(userDetails.getRole()).thenReturn(UserRoleEnum.ADMIN);

		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/reissue"));
		// .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

		//then
		//verify(adminService).issueToken(anyLong(), anyString(), any(UserRoleEnum.class));
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", "aaaaa"))
			.andExpect(jsonPath("message", "토큰 연장 완료").exists());
	}
}