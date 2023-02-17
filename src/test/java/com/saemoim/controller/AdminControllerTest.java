package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import com.saemoim.domain.Admin;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.AdminResponseDto;
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
	@WithCustomMockUser
	void signInByAdmin() throws Exception {
		//given
		TokenResponseDto responseDto = mock(TokenResponseDto.class);
		AdminRequestDto requestDto = AdminRequestDto.builder()
			.password("asdf1234!")
			.username("장성준")
			.build();

		when(responseDto.getAccessToken()).thenReturn("aaaaa");
		when(adminService.signInByAdmin(any(AdminRequestDto.class))).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
				.content(new Gson().toJson(requestDto)));//then
		//then
		verify(adminService).signInByAdmin(any(AdminRequestDto.class));
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", "aaaaa"))
			.andExpect(jsonPath("message").value("관리자 로그인 완료"));
	}

	@Test
	@DisplayName("관리자 목록 조회")
	@WithCustomMockUser
	void getAdmins() throws Exception {
		//given
		List<AdminResponseDto> list = new ArrayList<>();
		list.add(new AdminResponseDto(new Admin("admin", "password")));

		when(adminService.getAdmins()).thenReturn(list);
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin"));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$[0]['adminName']").value("admin"));
	}

	@Test
	@DisplayName("관리자 계정 생성")
	@WithCustomMockUser
	void createAdmin() throws Exception {
		//given
		MessageResponseDto responseDto = new MessageResponseDto("관리자 계정 생성 완료");
		AdminRequestDto requestDto = AdminRequestDto.builder()
			.username("장성준")
			.password("asdf1234!")
			.build();

		doNothing().when(adminService).createAdmin(any(AdminRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/admin")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.content(new Gson().toJson(requestDto)));//then
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message").value(responseDto.getMessage()));
	}

	@Test
	@DisplayName("관리자 목록 조회")
	@WithCustomMockUser
	void deleteAdmin() throws Exception {
		//given
		Long adminId = 1L;

		doNothing().when(adminService).deleteAdmin(adminId);
		//when
		ResultActions resultActions = mockMvc.perform(delete("/admin/{adminId}", adminId)
			.with(csrf()));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message").value("관리자 계정 삭제 완료"));
	}

	//수정 필요
	@Test
	@DisplayName("관리자 토큰 연장")
	@WithCustomMockUser
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