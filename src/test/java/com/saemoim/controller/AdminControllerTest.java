package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.Admin;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.AdminResponseDto;
import com.saemoim.dto.response.AdminTokenResponseDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.AdminServiceImpl;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = AdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AdminControllerTest {
	@MockBean
	private AdminServiceImpl adminService;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	@MockBean
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation)).build();
	}

	@Test
	@DisplayName("관리자 로그인")
	@WithCustomMockUser
	void signInByAdmin() throws Exception {
		//given
		AdminTokenResponseDto responseDto = new AdminTokenResponseDto("adminAccessToken");
		AdminRequestDto requestDto = AdminRequestDto.builder()
			.password("adminPass!1")
			.username("admin")
			.build();

		when(adminService.signInByAdmin(any(AdminRequestDto.class))).thenReturn(responseDto);
		// given(adminService.signInByAdmin(any(AdminRequestDto.class))).willReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/admin/sign-in")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
				.content(new Gson().toJson(requestDto)));//then
		//then
		verify(adminService).signInByAdmin(any(AdminRequestDto.class));
		resultActions
			.andExpect(status().isOk())
			.andExpect(header().string("Authorization", "adminAccessToken"))
			.andExpect(jsonPath("data").value("관리자 로그인 완료"))
			.andDo(document("AdminSignIn",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("username").description("어드민 계정ID").type(JsonFieldType.STRING),
					fieldWithPath("password").description("어드민 패스워드").type(JsonFieldType.STRING)
				),
				responseHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("관리자 목록 조회")
	@WithCustomMockUser(role = UserRoleEnum.ROOT)
	void getAdmins() throws Exception {
		//given
		List<AdminResponseDto> list = new ArrayList<>();
		list.add(new AdminResponseDto(new Admin("admin", "password")));

		when(adminService.getAdmins()).thenReturn(list);
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin"));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$['data'][0]['adminName']").value("admin"));
	}

	@Test
	@DisplayName("관리자 계정 생성")
	@WithCustomMockUser(role = UserRoleEnum.ROOT)
	void createAdmin() throws Exception {
		//given
		GenericsResponseDto responseDto = new GenericsResponseDto("관리자 계정 생성이 완료 되었습니다.");
		AdminRequestDto requestDto = AdminRequestDto.builder()
			.username("장성준")
			.password("asdf1234!")
			.build();

		doNothing().when(adminService).createAdmin(any(AdminRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/admin")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("data").value(responseDto.getData()));
	}

	@Test
	@DisplayName("관리자 계정 삭제")
	@WithCustomMockUser(role = UserRoleEnum.ROOT)
	void deleteAdmin() throws Exception {
		//given
		Long adminId = 1L;

		doNothing().when(adminService).deleteAdmin(adminId);
		//when
		ResultActions resultActions = mockMvc.perform(delete("/admins/{adminId}", adminId)
			.with(csrf()));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data").value("관리자 계정 삭제가 완료 되었습니다."));
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
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", "aaaaa"))
			.andExpect(jsonPath("data").value("토큰 연장이 완료 되었습니다."));
	}
}