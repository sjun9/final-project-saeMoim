package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.service.AdminServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AdminControllerTest {
	@MockBean
	private AdminServiceImpl adminService;
	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("관리자 로그인")
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	void signInByAdmin() throws Exception {
		//given
		TokenResponseDto responseDto = mock(TokenResponseDto.class);

		when(responseDto.getAccessToken()).thenReturn("aaaaa");
		when(adminService.signInByAdmin(any(AdminRequestDto.class))).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
				.content(new Gson().toJson(any(AdminRequestDto.class))));//then
		//then
		verify(adminService).signInByAdmin(any(AdminRequestDto.class));
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", "aaaaa"))
			.andExpect(jsonPath("message").value("관리자 로그인 완료"));
	}

	@Test
	@DisplayName("관리자 계정 생성")
	@WithCustomMockUser(role = UserRoleEnum.ROOT)
	void createAdmin() throws Exception {
		//given
		MessageResponseDto responseDto = new MessageResponseDto("관리자 계정 생성 완료");

		doNothing().when(adminService).createAdmin(any(AdminRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/admin")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.content(new Gson().toJson(any(AdminRequestDto.class))));//then
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message").value(responseDto.getMessage()));
	}

	//수정 필요
	@Test
	@DisplayName("관리자 토큰 연장")
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	void reissueAdmin() throws Exception {
		//given
		String accessToken = "aaaaa";

		when(adminService.issueToken(anyLong(), anyString(), any(UserRoleEnum.class))).thenReturn(accessToken);

		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/reissue").with(csrf()));

		//then
		verify(adminService).issueToken(anyLong(), anyString(), any(UserRoleEnum.class));
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", "aaaaa"))
			.andExpect(jsonPath("message").value("토큰 연장 완료"));
	}
}