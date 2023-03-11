package com.saemoim.controller;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.ApplicationStatusEnum;
import com.saemoim.dto.response.ApplicationResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.ApplicationService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = ApplicationController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ApplicationControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ApplicationService applicationService;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	@MockBean
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	@MockBean
	private CustomOAuth2UserService oAuth2UserService;
	@MockBean
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation)).build();
	}

	@Test
	@WithCustomMockUser
	@DisplayName("나의 참여신청 목록 조회")
	void getMyApplications() throws Exception {
		// given
		ApplicationResponseDto response = ApplicationResponseDto.builder()
			.id(1L)
			.groupId(1L)
			.groupName("saemoim")
			.leaderName("leader")
			.userId(1L)
			.username("participant")
			.status(ApplicationStatusEnum.WAIT)
			.build();
		List<ApplicationResponseDto> list = new ArrayList<>();
		list.add(response);
		when(applicationService.getMyApplications(anyLong())).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/participant/application")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("application/getAllMine",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					subsectionWithPath("data").description("신청목록"),
					fieldWithPath("data.[].id").description("신청서 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].groupId").description("모임 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.[].leaderName").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.[].userId").description("신청자 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].username").description("신청자 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.[].status").description("신청 상태(WAIT, PERMIT, REJECT)")
						.type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("모임 신청하기")
	void applyGroup() throws Exception {
		// given
		var groupId = 1L;
		doNothing().when(applicationService).applyGroup(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/groups/{groupId}/application", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("application/apply",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				pathParameters(
					parameterWithName("groupId").description("참여 신청할 모임 id")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
						.type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("모임 참여신청 취소")
	void cancelApplication() throws Exception {
		// given
		var applicationId = 1L;
		doNothing().when(applicationService).deleteApplication(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/applications/{applicationId}", applicationId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("application/cancel",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				pathParameters(
					parameterWithName("applicationId").description("취소할 신청 id")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
						.type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("리더가 받은 신청 목록 조회")
	void getApplications() throws Exception {
		// given
		ApplicationResponseDto response = ApplicationResponseDto.builder()
			.id(1L)
			.groupId(1L)
			.groupName("saemoim")
			.leaderName("leader")
			.userId(1L)
			.username("participant")
			.status(ApplicationStatusEnum.WAIT)
			.build();
		List<ApplicationResponseDto> list = new ArrayList<>();
		list.add(response);
		when(applicationService.getApplications(anyLong())).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/leader/application")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("application/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					subsectionWithPath("data").description("신청목록"),
					fieldWithPath("data.[].id").description("신청서 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].groupId").description("모임 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].groupName").description("모임 이름").type(JsonFieldType.STRING),
					fieldWithPath("data.[].leaderName").description("모임 리더 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.[].userId").description("신청자 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].username").description("신청자 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.[].status").description("신청 상태(WAIT, PERMIT, REJECT)")
						.type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("신청 승인")
	void permitApplication() throws Exception {
		// given
		var applicationId = 1L;
		doNothing().when(applicationService).permitApplication(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/applications/{applicationId}/permit", applicationId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("application/permit",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				pathParameters(
					parameterWithName("applicationId").description("승인할 신청 id")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
						.type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("신청 거절")
	void rejectApplication() throws Exception {
		// given
		var applicationId = 1L;
		doNothing().when(applicationService).rejectApplication(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/applications/{applicationId}/reject", applicationId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("application/reject",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				pathParameters(
					parameterWithName("applicationId").description("거절할 신청 id")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
						.type(JsonFieldType.STRING)
				)
			));
	}
}