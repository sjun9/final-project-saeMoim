package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.ReviewService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ReviewControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ReviewService reviewService;
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
	@DisplayName("모임 후기 조회")
	void getReviews() throws Exception {
		// given
		var groupId = 1L;
		var response = ReviewResponseDto.builder()
			.id(1L)
			.userId(1L)
			.username("writer")
			.content("content of post")
			.build();
		List<ReviewResponseDto> list = new ArrayList<>();
		list.add(response);
		Page<ReviewResponseDto> page = new PageImpl<>(list);

		when(reviewService.getReviews(anyLong(), any(Pageable.class))).thenReturn(page);

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/groups/{groupId}/review", groupId));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("모임 id")
				),
				responseFields(
					subsectionWithPath("data").description("결과값"),
					subsectionWithPath("data.content").description("게시글 목록"),
					fieldWithPath("data.content.[].id").description("게시글 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].userId").description("작성자 id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].username").description("작성자 닉네임").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].content").description("게시글 내용").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("후기작성")
	void createReview() throws Exception {
		// given
		var groupId = 1L;
		ReviewRequestDto request = ReviewRequestDto.builder().content("so good").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/groups/{groupId}/review", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("review/create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("모임 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				requestFields(
					fieldWithPath("content").description("후기 내용").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("후기 수정")
	void updateReview() throws Exception {
		// given
		var reviewId = 1L;
		ReviewRequestDto request = ReviewRequestDto.builder().content("so so good").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/reviews/{reviewId}", reviewId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("후기 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				requestFields(
					fieldWithPath("content").description("후기 수정 내용").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("후기 삭제")
	void deleteReview() throws Exception {
		// given
		var reviewId = 1L;

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/reviews/{reviewId}", reviewId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("후기 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}

	@Test
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	@DisplayName("관리자 - 후기 삭제")
	void deleteReviewByAdmin() throws Exception {
		// given
		var reviewId = 1L;

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/admin/reviews/{reviewId}", reviewId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/delete-admin",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("후기 id")
				),
				requestHeaders(
					headerWithName("Authorization").description("엑세스토큰")
				),
				responseFields(
					fieldWithPath("data").description("결과메세지")
				)
			));
	}
}